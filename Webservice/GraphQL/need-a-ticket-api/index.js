const express = require('express')
const jwt = require("express-jwt")
const jsonwebtoken = require("jsonwebtoken")
const bcrypt = require("bcryptjs")
const mongoose = require('mongoose')
const { Types } = require('mongoose')
const { User } = require('./models/User')
const { Artist } = require('./models/Artist')
const { Ticket } = require('./models/Ticket')
const { Concert } = require('./models/Concert')
const { Transaction } = require('./models/Transaction')
const { ApolloServer, gql } = require('apollo-server-express')

// Construct a schema, using GraphQL schema language
const typeDefs = gql`
  scalar Date
  type User {
    _id: ID!
    username: String!
    email: String!
    password: String!
  },
  type Artist {
    _id: ID,
    name: String!
  },
  type Concert {
    _id: ID!,
    title: String!,
    date: Date!,
    address: String!,
    capacity: Float!,
    Tickets: [String],
    artist: Artist,
  },
  type Ticket {
    _id: ID!,
    type: String!,
    price: Float!,
    redeemedAt: Date,
    seller: User!,
    buyer: User,
    concert: Concert!,
  }
  type Query {
    users:[User],
    user(id: ID!): User,
    artists: [Artist],
    artist(id: ID!): Artist,
    concerts: [Concert],
    concert(id: ID!): Concert,
    tickets: [Ticket],
    ticket(id: ID!): Ticket,
  },
  type Mutation {
    signup (username: String!, email: String!, password: String!): String
    login (email: String!, password: String!): String
    createArtist (name: String!): Artist
    createConcert (title: String!, date: Date!, address: String!, capacity: Float!, artistId: ID!): Concert
    createTicket (type: String!, price: Float!, sellerId: String!,concertId: String!,redeemedAt: Date, buyerId: String): Ticket
    buy (ticketId: ID!, buyerId: ID!): Boolean
  }
`



// Provide resolver functions for your schema fields
const resolvers = {
  Query: {

    async user(_,{id}) {
      return User.findOne(Types.ObjectId(id))
    },

    async users() {
      return User.find()
    },

    async artist(_,{id}) {
      return Artist.findOne(Types.ObjectId(id))
    },

    async artists() {
      return Artist.find()
    },

    async concert(_,{id}) {
      let concert = await Concert.aggregate([
        {$lookup: { from: 'artists',localField:'artistId',foreignField: '_id',as: 'artist'}},
        {$unwind: "$artist"},
        {$match : {_id : Types.ObjectId(id)}},
        {$limit : 1}
      ])
      return concert.shift()
    },

    async concerts(){
      return Concert.aggregate([
        {$lookup: { from: 'artists',localField:'artistId',foreignField: '_id',as: 'artist'}},
        {$unwind: "$artist"}
        ])
    },

    async ticket(_,{id}){
      let ticket =  await Ticket.aggregate([
          {$lookup: { from: 'users',localField:'sellerId',foreignField: '_id',as: 'seller'}},
          {$unwind: "$seller"},
          {$lookup: { from: 'users',localField:'buyerId',foreignField: '_id',as: 'buyer'}},
          {$unwind: "$buyer"},
          {$lookup: { from: 'concerts',localField:'concertId',foreignField: '_id',as: 'concert'}},
          {$unwind: "$concert"},
          {$match : {_id : Types.ObjectId(id)}},
          {$limit : 1}
      ])
      return ticket.shift()
    },

    async tickets(){
      return await Ticket.aggregate([
        {$lookup: { from: 'users',localField:'sellerId',foreignField: '_id',as: 'seller'}},
        {$unwind: "$seller"},
        {$lookup: { from: 'users',localField:'buyerId',foreignField: '_id',as: 'buyer'}},
        {$unwind: "$buyer"},
        {$lookup: { from: 'concerts',localField:'concertId',foreignField: '_id',as: 'concert'}},
        {$unwind: "$concert"},
      ])
    },

  },

  Mutation: {
    async signup(_, { username, email, password }) {
      let user = new User({
        username,
        email,
        password: await bcrypt.hash(password, 10)
      });
      await user.save((err) => {
        if (err) {
          console.error(err);
          // apollo error.
        }
      });

      // Return json web token
      return jsonwebtoken.sign(
        { id: user.id, email: user.email },
        "process.env.JWT_SECRET",
        { expiresIn: '1y' }
      );
    },

    async login(_, { email, password }) {
      const user = await User.findOne({ email: email })

      if (!user) {
        throw new Error('No user with that email')
      }

      const valid = await bcrypt.compare(password, user.password)

      if (!valid) {
        throw new Error('Incorrect password')
      }

      // Return json web token
      return jsonwebtoken.sign(
        { id: user.id, email: user.email },
        "process.env.JWT_SECRET",
        { expiresIn: '1y' }
      )
    },

    async createArtist(_, {name}) {
      let artist = new Artist({name})
      await artist.save((err) => {
        if (err)
          throw err
      })
      return artist
    },

    async createConcert(_,{title,date,address,capacity,artistId}) {
      artistId = Types.ObjectId(artistId)
      let concert = new Concert({
        title,date,address,capacity,artistId
      })
      await concert.save((err) => {
        if(err)
          throw err
      })
      return concert
    },

    async createTicket(_,{type,price,sellerId,concertId,redeemedAt,buyerId}){
      sellerId = Types.ObjectId(sellerId)
      concertId = Types.ObjectId(concertId)
      if(buyerId)
        buyerId = Types.ObjectId(buyerId)
      let ticket = new Ticket({
        type,price,redeemedAt,sellerId,buyerId,concertId
      })
      await ticket.save((err)=>{
        if(err)
          throw err
      })
      return ticket
    },

    async buy(_,{ticketId, buyerId}){
      ticketId = Types.ObjectId(ticketId)
      buyerId = Types.ObjectId(buyerId)

      await Ticket.updateOne(
          { "_id" : ticketId },
          { $set : { buyerId } }
      )
      return true
    },

    async buy(_,{amount,ticketId,payerWalletId,receiverWalletId}){
      payerWalletId = Types.ObjectId(payerWalletId)
      receiverWalletId = Types.ObjectId(receiverWalletId)
      let transaction = new Transaction({
        amount,payerWalletId,receiverWalletId
      })
    }

  }
}


mongoose.connect('mongodb://julian-blaschke:Julian1999@ds247001.mlab.com:47001/need-a-ticket')

const server = new ApolloServer({ typeDefs, resolvers })

// auth middleware
const auth = jwt({
  secret: "process.env.JWT_SECRET",
  credentialsRequired: false
})

const app = express()
app.use(auth)

server.applyMiddleware({ app })

app.listen({ port: 4000 }, () =>
  console.log(`🚀 Server ready at http://localhost:4000${server.graphqlPath}`)
)

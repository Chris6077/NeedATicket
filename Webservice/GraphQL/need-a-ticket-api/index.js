const express = require('express')
const jwt = require("express-jwt")
const jsonwebtoken = require("jsonwebtoken")
const bcrypt = require("bcryptjs")
const mongoose = require('mongoose')
const config = require('./config')
const {PasswordMeter} = require('password-meter')
const { logic } = require('./logic')
const { Types } = require('mongoose')
const { User } = require('./models/User')
const { Artist } = require('./models/Artist')
const { Ticket } = require('./models/Ticket')
const { Concert } = require('./models/Concert')
const { Transaction } = require('./models/Transaction')
const { Wallet } = require('./models/Wallet')
const { ApolloServer, gql, AuthenticationError,ApolloError } = require('apollo-server-express')
const {makeExecutableSchema, addSchemaLevelResolveFunction} = require('graphql-tools')


global.config = config

// Construct a schema, using GraphQL schema language
const typeDefs = gql`
  scalar Date
  type User {
    _id: ID!
    username: String
    email: String!
    wallet: Wallet!
    selling: [Ticket]
    bought: [Ticket]
    redeemed: [Ticket]
    totalSelling: Int
    totalBought: Int
    totalRedeemed: Int
    passwordStrength: PasswordMeter
  }
  type PasswordMeter {
    score: Float!,
    status: String!,
    percent: Float!
  }
  type Artist {
    _id: ID,
    name: String!
  }
  type Concert {
    _id: ID!
    title: String!
    date: Date!
    address: String!
    capacity: Float!
    tickets: [Ticket]
    artist: Artist
  }
  type Ticket {
    _id: ID!
    type: String!
    price: Float!
    redeemed: Boolean!
    redeemedAt: Date
    seller: User!
    buyer: User
    concert: Concert!
  }
  type Wallet {
    _id: ID!
    balance: Float!
  }
  type Transaction {
    _id: ID!
    date: Date!
    amount: Float!
    payer: User!
    receiver: User!
    ticket: Ticket!
  }
  type groupedTicket{
    concert: Concert! 
    price: Float!
    seller: User!
    type: String!
    available: Float!
  }
  type Query {
    me: User
    users:[User]
    user(id: ID!): User
    artists: [Artist]
    artist(id: ID!): Artist
    concerts: [Concert]
    concert(id: ID!): Concert
    tickets: [Ticket]
    ticketsGrouped(concertId: ID): [groupedTicket]
    ticket(id: ID!): Ticket
    transactions: [Transaction]
    transaction(id: ID!): Transaction
  }
  type Mutation {
    signup (username: String, email: String!, password: String!): String
    login (email: String!, password: String!): String
    staffLogin (concertId: ID!): String
    createArtist (name: String!): Artist
    createConcert (title: String!, date: Date!, address: String!, capacity: Float!, artistId: ID!): Concert
    createTicket (type: String!, price: Float!,concertId: String!,redeemedAt: Date, buyerId: String): Ticket
    createTickets (amount: Float!, type: String!, price: Float!, sellerId: String!,concertId: String!,redeemedAt: Date, buyerId: String): [Ticket]
    updateUser (email: String, password: String) : User
    buy (ticketId: ID!): Transaction
    buyBulk (number: Float!, concertId: ID!, sellerId: ID!, price: Float!): Transaction 
    deposit (amount: Float!): Wallet
    redeem (ticketId: ID!): Ticket
  }
`



// Provide resolver functions for your schema fields
const resolvers = {
  Query: {
    async me(_,{},context){
      return await logic.User.findOne({id:context.user.id})
    },

    async user(_,{id}) {
      return await logic.User.findOne({id})
    },

    async users() {
      return await logic.User.find()
    },

    async artist(_,{id}) {
      return await logic.Artist.findOne({id})
    },

    async artists() {
      return await logic.Artist.find()
    },

    async concert(_,{id}) {
      return await logic.Concert.findOne({id})
    },

    async concerts(){
      return await logic.Concert.find()
    },

    async ticket(_,{id}){
      return logic.Ticket.findOne({id})
    },

    async tickets(){
      return logic.Ticket.find()
    },

    async ticketsGrouped(_,{concertId}){
      return logic.Ticket.findAndGroup({concertId})
    },

    async transaction(_,{id}){
      return logic.Transaction.findOne({id})  
    },

    async transactions(){
      return logic.Transaction.find()
    },
  },

  Mutation: {
    async signup(_, { username, email, password }) {
      return logic.User.signup({username,email,password})
    },

    async login(_, { email, password }) {
      return logic.User.login({email,password})
    },

    async staffLogin(_, { concertId }) {
      return logic.User.loginStaff({concertId})
    },

    async createArtist(_, {name}) {
      return logic.Artist.insertOne({name})
    },

    async createConcert(_,{title,date,address,capacity,artistId},context) {
      return logic.Concert.insertOne({title,date,address,capacity,artistId,sellerId: context.user.id})
    },

    async createTicket(_,{type,price,concertId,redeemedAt,buyerId},context){
      return await logic.Ticket.insertOne({type,price,concertId,redeemedAt,buyerId,sellerId: context.user.id})
    },

    async createTickets(_,{amount,type,price,concertId,redeemedAt,buyerId},context){
      return logic.Ticket.insertMany({amount,type,price,concertId,redeemedAt,buyerId,sellerId: context.user.id})
    },

    async updateUser(_,{email,password},context){
      return logic.User.updateOne({email,password,userId:context.user.id})
    },

    async buy(_,{ticketId},context){
      //need to create transaction / update both receiver and payer Wallet / and update ticket
      payerId = Types.ObjectId(context.user.id)
      ticketId = Types.ObjectId(ticketId)
      date = new Date()

      let ticket = await Ticket.findOne(ticketId)
      let receiverId = Types.ObjectId(ticket.sellerId)
      let amount = ticket.price 
      let payer = await User.findOne(payerId)
      let receiver = await User.findOne(receiverId)

      
      //decrease payer wallet
      await Wallet.updateOne(
        { "_id" : payer.walletId },
        { $inc : { balance: amount } }
      )

      //increase receiver wallet
      await Wallet.updateOne(
        { "_id" : receiver.walletId },
        { $inc : { balance: -amount } }
      )
      
      //create the transaction
      let transaction = new Transaction({
        amount,date,payerId,receiverId,ticketId
      })

      await transaction.save()

      //update buyer in ticket
      await Ticket.updateOne(
          { "_id" : ticketId },
          { $set : { buyerId: payerId } }
      )

      return transaction
    },

    async buyBulk(_,{number,concertId,sellerId,price},context){
      //need to create transaction / update both receiver and payer Wallet / and update ticket
      payerId = Types.ObjectId(context.user.id)
      concertId = Types.ObjectId(concertId)
      sellerId = Types.ObjectId(sellerId)
      //buy tickets
      date = new Date()
      
      let receiverId = Types.ObjectId(sellerId)
      let payer = await User.findOne(payerId)
      let receiver = await User.findOne(receiverId)
      let amount = price * number

      //decrease payer wallet
      await Wallet.updateOne(
          { "_id" : payer.walletId },
          { $inc : { balance: amount } }
      )

      //increase receiver wallet
      await Wallet.updateOne(
          { "_id" : receiver.walletId },
          { $inc : { balance: -amount } }
      )

      //create the transaction
      let transaction = new Transaction({
        amount,date,payerId,receiverId,concertId
      })

      await transaction.save()

      //update buyer in ticket
      let tickets = await Ticket.find({
        concertId,sellerId,price
      }).limit(number)

      await tickets.forEach( async (el) => { 
        el.buyerId = payerId
        await Ticket.collection.save(el)
      })

      return transaction
    },

    async deposit(_,{amount},context){
      userId = Types.ObjectId(context.user.id)

      let user = await User.findOne(userId)

      await Wallet.updateOne(
        { "_id" : user.walletId },
        { $inc : { balance: amount } }
      )

      return Wallet.findOne(user.walletId)
    },

    async redeem(_,{ticketId},context){
      ticketId = Types.ObjectId(ticketId)
      let redeemedAt = new Date()
      let ticket = await Ticket.findOne(ticketId)
      if(context.user.role != "staff")
        throw new AuthenticationError("your not logged in as staff")
      if(ticket.concertId +"" !=  Types.ObjectId(context.user.id)+"")
        throw new AuthenticationError("you cannot redeem tickets for other concerts")
      if(!ticket)
        throw new ApolloError("ticket not found", 404)
      if(ticket.redeemed)
        throw new ApolloError("ticket already redeemed.",400)
      await Ticket.updateOne(
        { "_id" : ticketId },
        { $set : { redeemed: true, redeemedAt } }
      )

      ticket = await Ticket.aggregate([
          {$lookup: { from: 'users',localField:'sellerId',foreignField: '_id',as: 'seller'}},
          {$unwind: "$seller"},
          {$lookup: { from: 'users',localField:'buyerId',foreignField: '_id',as: 'buyer'}},
          {$unwind: { path:"$buyer", preserveNullAndEmptyArrays: true}},
          {$lookup: { from: 'concerts',localField:'concertId',foreignField: '_id',as: 'concert'}},
          {$unwind: "$concert"},
          {$match : {_id : ticketId}},
          {$limit : 1}
      ])
      return ticket.shift()
    }

  }
}

//database
mongoose.connect('mongodb://julian-blaschke:Julian1999@ds247001.mlab.com:47001/need-a-ticket', {useNewUrlParser: true})

//auth exception middleware
const schema = makeExecutableSchema({typeDefs, resolvers})
addSchemaLevelResolveFunction(schema, (root, args, context, info) => {
  if(!context.user)
    if(info.fieldName !== 'login' && info.fieldName !== 'signup' && info.fieldName !== "staffLogin")
      throw new AuthenticationError("not authenticated.")  
})

//create apollo server
const server = new ApolloServer({ schema, introspection: true, playground: true, context: ({ req }) => ({
    user: req.user
  })
})

const app = express()

//auth middleware
const auth = jwt({
  secret: global.config.secret,
  credentialsRequired: false,
})


//apply middleware
app.use(auth)

server.applyMiddleware({ app })

app.listen({ port: process.env.PORT || 4000 }, () =>
  console.log(`🚀 Server ready at http://localhost:4000${server.graphqlPath}`)
)

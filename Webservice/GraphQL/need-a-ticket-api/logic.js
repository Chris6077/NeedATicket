const express = require('express')
const jwt = require("express-jwt")
const jsonwebtoken = require("jsonwebtoken")
const bcrypt = require("bcryptjs")
const mongoose = require('mongoose')
const config = require('./config')
const {PasswordMeter} = require('password-meter')
const { Types } = require('mongoose')
const { User } = require('./models/User')
const { Artist } = require('./models/Artist')
const { Ticket } = require('./models/Ticket')
const { Concert } = require('./models/Concert')
const { Transaction } = require('./models/Transaction')
const { Wallet } = require('./models/Wallet')
const { ApolloServer, gql, AuthenticationError,ApolloError } = require('apollo-server-express')

mongoose.connect('mongodb://julian-blaschke:Julian1999@ds247001.mlab.com:47001/need-a-ticket', {useNewUrlParser: true})

//********************************************************* queries *********************************************************
//		find an user and add all attributes
async function findOneUser({id}){
	if(!id)
		throw new ApolloError("no id passed")
	let _id = Types.ObjectId(id)
	let totalselling = await Ticket.find({
		sellerId: _id
	}).countDocuments()
	let totalredeemed = await Ticket.find({
		buyerId: _id,
		redeemed: true
	}).countDocuments()
	let totalbought = await Ticket.find({
		buyerId: _id
	}).countDocuments()
	let selling = await Ticket.find({
		sellerId: _id
	})
	let redeemed = await Ticket.find({
		buyerId: _id,
		redeemed: true
	})
	let bought = await Ticket.find({
		buyerId: _id
	})
	let user = await User.aggregate([
		{$lookup: { from: 'wallets',localField:'walletId',foreignField: '_id',as: 'wallet'}},
		{$unwind: "$wallet"},
		{$match : {_id }},
		{$limit : 1}
	])
	user = user.shift()
	user.totalSelling = totalselling
	user.totalBought = totalbought
	user.totalRedeemed = totalredeemed;
	user.selling = selling
	user.bought = bought
	user.redeemed = redeemed
	return user
}
// 		find all users and add no attributes
async function findAllUsers() {
 	return User.aggregate([
	    {$lookup: { from: 'wallets',localField:'walletId',foreignField: '_id',as: 'wallet'}},
	    {$unwind: "$wallet"},
	    {$lookup: { from: 'tickets',localField:'_id',foreignField: 'sellerId',as: 'selling'}},
	    {$lookup: { from: 'tickets',localField:'_id',foreignField: 'buyerId',as: 'bought'}},
	])
}
//		find an artist
async function findOneArtist ({id}){
	return Artist.findOne(Types.ObjectId(id))
}
//		find all artists
async function findAllArtists(){
	return Artist.find()
}
// 		find a concert and add all atributes
async function findOneConcert({id}){
	let concert = await Concert.aggregate([
        {$lookup: { from: 'artists',localField:'artistId',foreignField: '_id',as: 'artist'}},
        {$unwind: "$artist"},
        {$lookup: { from: 'tickets', localField: '_id', foreignField: 'concertId' , as : 'tickets' }},
        {$match : {_id : Types.ObjectId(id)}},
        {$limit : 1}
	])
	concert = concert.shift()
	await concert.tickets.forEach( async(ticket) => {
		ticket.seller = findOneUser({id : ticket.sellerId})
		if(ticket.buyerId)
				ticket.buyer = await findOneUser({id : ticket.buyerId})	
	})
	return concert
}
//		find all concerts and add all attributes
async function findAllConcerts(){
	let concerts = await Concert.aggregate([
		{$lookup: { from: 'artists',localField:'artistId',foreignField: '_id',as: 'artist'}},
		{$unwind: "$artist"},
		{$lookup: { from: 'tickets', localField: '_id', foreignField: 'concertId' , as : 'tickets' }}
	])
	await concerts.forEach(	async(concert) => {
		await concert.tickets.forEach( async(ticket) => {
			ticket.seller =  findOneUser({id: ticket.sellerId})	
			if(ticket.buyerId)
				ticket.buyer = await findOneUser({id: ticket.buyerId})	
		})
	})
	return concerts
}
//		find an ticket and aggregate it
async function findOneTicket({id}){
	let ticket =  await Ticket.aggregate([
		{$lookup: { from: 'users',localField:'sellerId',foreignField: '_id',as: 'seller'}},
		{$unwind: "$seller"},
		{$lookup: { from: 'users',localField:'buyerId',foreignField: '_id',as: 'buyer'}},
		{$unwind: { path:"$buyer", preserveNullAndEmptyArrays: true}},
		{$lookup: { from: 'concerts',localField:'concertId',foreignField: '_id',as: 'concert'}},
		{$unwind: "$concert"},
		{$match : {_id : Types.ObjectId(id)}},
		{$limit : 1}
	])	
	return ticket.shift()
}
//		find all tickets and aggregate them
async function findAllTickets(){
	let tickets =  await Ticket.aggregate([
		{$lookup: { from: 'users',localField:'sellerId',foreignField: '_id',as: 'seller'}},
		{$unwind: "$seller"},
		{$lookup: { from: 'users',localField:'buyerId',foreignField: '_id',as: 'buyer'}},
		{$unwind: { path:"$buyer", preserveNullAndEmptyArrays: true}},
		{$lookup: { from: 'concerts',localField:'concertId',foreignField: '_id',as: 'concert'}},
		{$unwind: "$concert"}
	])
	return tickets
}
//		find all tickets and group them by concert, seller and price
async function findAndGroupTickets({concertId}){
	if(concertId){
		return await Ticket.aggregate([
			{$match: {buyerId: {$exists: true}} },
			{$group: {_id: {concertId: '$concertId', sellerId: "$sellerId", price: '$price', type: '$type' }, count: {$sum: 1}}},
			{$lookup: { from: 'users',localField:'_id.sellerId',foreignField: '_id',as: 'seller'}},
			{$unwind: "$seller"},
			{$lookup: { from: 'concerts',localField:'_id.concertId',foreignField: '_id',as: 'concert'}},
			{$unwind: "$concert"},
			{$project: {concert: "$concert", seller: "$seller", price: '$_id.price', available: "$count", type: "$_id.type", _id : 0 }},
			{$match : {"concert._id" : Types.ObjectId(concertId)}},
		])
	}
	return await Ticket.aggregate([
		{$match: {buyerId: {$exists: true}} },
		{$group: {_id: {concertId: '$concertId', sellerId: "$sellerId", price: '$price', type: '$type' }, count: {$sum: 1}}},
		{$lookup: { from: 'users',localField:'_id.sellerId',foreignField: '_id',as: 'seller'}},
		{$unwind: "$seller"},
		{$lookup: { from: 'concerts',localField:'_id.concertId',foreignField: '_id',as: 'concert'}},
		{$unwind: "$concert"},
		{$project: {concert: "$concert", seller: "$seller", price: '$_id.price', available: "$count", type: "$_id.type", _id : 0 }}
	])
}
//		find one transaction and aggregate it
async function findOneTransaction({id}){
	let transaction = await Transaction.aggregate([
		{$lookup: { from: 'users',localField:'payerId',foreignField: '_id',as: 'payer'}},
		{$unwind: "$payer"},
		{$lookup: { from: 'users',localField:'receiverId',foreignField: '_id',as: 'receiver'}},
		{$unwind: "$receiver"},
		{$lookup: { from: 'tickets',localField:'ticketId',foreignField: '_id',as: 'ticket'}},
		{$unwind: "$ticket"},
		{$match : {_id : Types.ObjectId(id)}},
		{$limit : 1}
	])
	return transaction.shift()	
}
//		find all transactions and aggregate them
async function findAllTransactions(){
	return await Transaction.aggregate([
		{$lookup: { from: 'users',localField:'payerId',foreignField: '_id',as: 'payer'}},
		{$unwind: "$payer"},
		{$lookup: { from: 'users',localField:'receiverId',foreignField: '_id',as: 'receiver'}},
		{$unwind: "$receiver"},
		{$lookup: { from: 'tickets',localField:'ticketId',foreignField: '_id',as: 'ticket'}},
		{$unwind: "$ticket"},
	])
}
//********************************************************* mutations *********************************************************
//		sign an user up with at least email and password (username optional) --> passwordhash, passwordstregth and wallet are being generated --> returns an jwt
async function signUp ({username,email,password}){
	if(email)
		if(await User.findOne({email}))
			throw new ApolloError("an user with this email already exists")
	let wallet = new Wallet({
		balance: 0
	})
	let passwordStrength = await new PasswordMeter({},{
		"50": "very weak",  // 001 <= x <  040
		"100": "weak",  // 040 <= x <  080
		"150": "average", // 080 <= x <  120
		"200": "strong", // 120 <= x <  180
		"_": "very strong"   //        x >= 200
	}).getResult(password)

	await wallet.save()

	let user = new User({
		username,
		email,
		password: await bcrypt.hash(password, 10),
		walletId: wallet._id,
		passwordStrength
	})
	await user.save()

	// Return json web token
	return jsonwebtoken.sign(
		{ id: user.id, email: user.email },
		global.config.secret,
		{ expiresIn: '1y' }
	)
}
//		loggs in an user with email and password --> returns an jwt
async function login ({email,password}){
	const user = await User.findOne({ email: email })
	if (!user) 
		throw new Error('No user with that email')
	const valid = await bcrypt.compare(password, user.password)

	if (!valid)
	throw new Error('Incorrect password')

	// Return json web token
	return await jsonwebtoken.sign(
		{ id: user.id, email: user.email },
		global.config.secret,
		{ expiresIn: '1y' }
	)
}

const logic = {
	User: {
		findOne: findOneUser,
		find: findAllUsers,
		signup: signUp,
		login: login
	},
	Artist: {
		findOne: findOneArtist,
		find: findAllArtists
	},
	Concert: {
		findOne: findOneConcert,
		find: findAllConcerts
	},
	Ticket: {
		findOne: findOneTicket,
		find: findAllTickets,
		findAndGroup: findAndGroupTickets
	},
	Transaction: {
		findOne: findOneTransaction,
		find: findAllTransactions
	}
}
exports.logic = logic
const mongoose = require('mongoose')
const {Types} = require('mongoose')
const {User} = require('./User')

const TicketSchema = new mongoose.Schema({
    type: String,
    price: Number,
    sellerId: Types.ObjectId,
    buyerId: Types.ObjectId,
    concertId: Types.ObjectId,
}, {timestamps: true});

exports.User = mongoose.model('Ticket', TicketSchema);
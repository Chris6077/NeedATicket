const mongoose = require('mongoose');

const ConcertSchema = new mongoose.Schema({
    title: String,
    date: Date,
    address: String,
    genre: String,
    artistId: String,
}, {timestamps: true});

exports.Concert = mongoose.model('Concert', ConcertSchema);
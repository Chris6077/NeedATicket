const mongoose = require('mongoose');

const TransactionSchema = new mongoose.Schema({
    amount: Number,
    date: Date,
    payerWalletId: mongoose.Types.ObjectId,
    receiverWalletId: mongoose.Types.ObjectId,
    ticketId: mongoose.Types.ObjectId,
}, {timestamps: true});

exports.Transaction = mongoose.model('Concert', TransactionSchema);
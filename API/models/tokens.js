const mongoose = require('mongoose');

const tokenSchema = new mongoose.Schema({
  token: {
    type: String,
    required: true,
  },
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
  },
  isWhitelisted: {
    type: Boolean,
    default: false,
  },
  isBlacklisted: {
    type: Boolean,
    default: false,
  },
});

const Token = mongoose.model('Token', tokenSchema);

module.exports = Token;
var mongoose = require('mongoose');

var accountSchema = new mongoose.Schema({
  "userId": String,
  "accountId": String,
  "accountTitle": String,
  "accountType": String,
  "year": String,
  "month": String,
  "day": String,
  "moneyType": String,
  "money": Number,
  "beizhu": String

});


module.exports = mongoose.model("Account", accountSchema);

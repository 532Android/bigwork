var mongoose = require('mongoose');

var riChengSchema = new mongoose.Schema({
  "userId": String,
  "eventId": String,
  "eventTitle": String,
  "eventDate": String,
  "startTime": String,
  "endTime": String,
  "place": String,
  "beizhu": String
});

module.exports = mongoose.model("Richeng", riChengSchema);
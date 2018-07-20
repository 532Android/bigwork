var mongoose = require('mongoose');

var riChengSchema = new mongoose.Schema({
  "userId": String,
  "eventId": String,
  "eventTitle": String,
  "eventDate": String,
  "eventType": String,
  "startTime": String,
  "endTime": String,
  "priority": String,
  "place": String,
  "beizhu": String,
  "liuyan": String,
  "clockList": [
    {
      "clockId": String,
      "alertDate": String,
      "alertTime": String,
      "choosedSong": String,
      "isOpen": Boolean
    }
  ]
});

module.exports = mongoose.model("Richeng", riChengSchema);
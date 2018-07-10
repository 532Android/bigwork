var mongoose = require('mongoose');

var userSchema = new mongoose.Schema({
  "userId": String,
  "userName": String,
  "userPwd": String,
});

// 第三个参数是指定MongoDB中对应的集合名
// 若没有第三个参数,会把第一个参数加s当做集合名
module.exports = mongoose.model("User", userSchema);

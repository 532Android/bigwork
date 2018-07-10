var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var User = require('../models/user');

// 连接数据库
mongoose.connect('mongodb://127.0.0.1:27017/android');

mongoose.connection.on('connected', function () {
  console.log('success');
});
mongoose.connection.on('error', function () {
  console.log('failed')
});
mongoose.connection.on('disconnected', function () {
  console.log('disconnected')
});

// 注册账号接口
router.post('/register', function (req, res, next) {
  var userId = req.body.userId;
  var userPwd = req.body.userPwd;
  var userName = req.body.userName;
  var userDoc = {
    userId: userId,
    userPwd: userPwd,
    userName: userName,
  };
  var myModel = mongoose.model('user', User.schema);
  var doc = new myModel(userDoc);
  User.findOne({
    userId: userId
  }, function (error, cdoc) {
    if (error) {
      res.json({
        status: '0',
        msg: error.message
      });
    } else {
      // 若用户已存在，返回提示信息，用户不存在，存入到数据库
      if (cdoc) {
        res.json({
          status: '0',
          msg: '',
          result: 'user has been registered!'
        });
      } else {
        doc.save(function (err, result) {
          console.log(result)
          if (err) {
            res.json({
              status: '0',
              msg: err.message
            });
          } else {
            res.json({
              status: '1',
              msg: '',
              result: 'register success!'
            });
          }
        });
      }
    }
  })
});


// 登录接口
router.post('/login', function (req, res, next) {
  var userId = req.body.userId;
  var userPwd = req.body.userPwd;
  User.findOne({
    userId: userId,
    userPwd: userPwd
  }, function (err, doc) {
    console.log(doc)
    if (err) {
      res.json({
        status: '0',
        msg: err.message,
      });
    } else {
      if (doc) {
        res.json({
          status: '1',
          msg: '',
          result: 'user exists'
        });
      } else {
        res.json({
          status: '0',
          msg: '',
          result: 'user not exists or password is not correct'
        })
      }
    }
  });
});

module.exports = router;

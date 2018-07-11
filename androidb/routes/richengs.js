var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
require('./../util/util.js');
var Richeng = require('../models/richeng');

// 添加日程接口
router.post('/addEvent', function (req, res, next) {
  var userId = req.body.userId;
  var eventTitle = req.body.eventTitle;
  var eventDate = req.body.eventDate;
  var startTime = req.body.startTime;
  var endTime = req.body.endTime;
  var place = req.body.place;
  var beizhu = req.body.beizhu;
  // 随机生成eventId
  var platform = 'Eve';
  // 创建两个随机数
  var ran1 = Math.floor(Math.random() * 10);
  var ran2 = Math.floor(Math.random() * 10);
  var sysDate = new Date().Format('yyyyMMddhhmmss');
  var createDate = new Date().Format('yyyy-MM-dd hh:mm:ss');
  var eventId = platform + ran1 + sysDate + ran2;
  var eventDoc = {
    userId: userId,
    eventId: eventId,
    eventTitle: eventTitle,
    eventDate: eventDate,
    startTime: startTime,
    endTime: endTime,
    place: place,
    beizhu: beizhu
  };
  var myModel = mongoose.model('richeng', Richeng.schema);
  var event = new myModel(eventDoc);
  event.save(function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc) {
        res.json({
          status: '1',
          msg: '',
          result: 'Add event success!'
        });
      } else {
        res.json({
          status: '2000',
          msg: '',
          result: 'Add Falied!'
        });
      }
    }
  })
});

// 查询日程接口
router.post('/searchEvent', function (req, res, next) {
  var userId = req.body.userId;
  var eventDate = req.body.eventDate;
  Richeng.find({
    userId: userId,
    eventDate: eventDate
  }, function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc.length != 0) {
        res.json({
          status: '1',
          msg: '',
          result: doc
        });
      } else {
        res.json({
          status: '2001',
          msg: '',
          result: 'Have no data!'
        })
      }
    }
  });
})

module.exports = router;
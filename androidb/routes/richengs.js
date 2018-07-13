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
  var eventType = req.body.eventType;
  var startTime = req.body.startTime;
  var endTime = req.body.endTime;
  var priority = req.body.priority;
  var place = req.body.place;
  var beizhu = req.body.beizhu;
  var liuyan = req.body.liuyan;
  // 随机生成eventId
  var platform = 'Eve';
  // 创建两个随机数
  var ran1 = Math.floor(Math.random() * 10);
  var ran2 = Math.floor(Math.random() * 10);
  var sysDate = new Date().Format('yyyyMMddhhmmss');
  var eventId = platform + ran1 + sysDate + ran2;
  var eventDoc = {
    userId: userId,
    eventId: eventId,
    eventTitle: eventTitle,
    eventDate: eventDate,
    eventType: eventType,
    startTime: startTime,
    endTime: endTime,
    priority: priority,
    place: place,
    beizhu: beizhu,
    liuyan: liuyan
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
  var userId = req.body.userId || "";
  var sort = req.body.sort || -1;
  var eventDate = req.body.eventDate;
  var eventType = req.body.eventType;
  var priority = req.body.priority;
  var eventId = req.body.eventId;
  var findEvent = {
    userId: userId,
    eventId: eventId,
    eventDate: eventDate,
    eventType: eventType,
    priority: priority,
  }
  // 遍历对象属性，删除掉值为undefined的属性
  for (var key in findEvent) {
    if (key != userId) {
      if (findEvent[key] == undefined) {
        delete findEvent[key];
      }
    }
  }
  var rModel =  Richeng.find(findEvent).sort({eventDate: sort})
  rModel.exec( function (err, doc) {
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

// 删除日程接口
router.post('/deleteEvent', function (req, res, next) {
  var eventId = req.body.eventId;
  Richeng.remove({
    eventId: eventId
  }, function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      res.json({
        status: '1',
        msg: '',
        result: 'delete event success!'
      });
    }
  })
})

// 编辑日程接口
router.post('/editEvent', function (req, res, next) {
  var eventId = req.body.eventId;
  var eventTitle = req.body.eventTitle;
  var eventDate = req.body.eventDate;
  var eventType = req.body.eventType;
  var startTime = req.body.startTime;
  var endTime = req.body.endTime;
  var priority = req.body.priority;
  var place = req.body.place;
  var beizhu = req.body.beizhu;
  var liuyan = req.body.liuyan;
  Richeng.update({
    eventId: eventId
  },{
    eventTitle: eventTitle,
    eventDate: eventDate,
    eventType: eventType,
    startTime: startTime,
    endTime: endTime,
    priority: priority,
    place: place,
    beizhu: beizhu,
    liuyan: liuyan
  }, function (err, doc) {
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
          result: 'edit success!'
        });
      } else {
        res.json({
          status: '2002',
          msg: '',
          result: 'edit failed'
        })
      }
    }
  })
});

module.exports = router;
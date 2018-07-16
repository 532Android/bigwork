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

  // 随机生成clockId
  var clockform = 'Clo';
  var clockId = clockform + ran1 + sysDate + ran2;

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
    liuyan: liuyan,
    clockList: [{
      clockId: clockId,
      alertDate: eventDate,
      alertTime: startTime,
      choosedSong: "0"
    }]
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
          result: 'Add event Falied!'
        });
      }
    }
  });
});

// 查询日程接口
router.post('/searchEvent', function (req, res, next) {
  var userId = req.body.userId || "";
  var sort = req.body.sort || -1;
  var eventId = req.body.eventId;
  var eventDate = req.body.eventDate;
  var eventType = req.body.eventType;
  var priority = req.body.priority;
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

  if (findEvent.priority != undefined) {
    var pGt = '';
    var pLte = '';
    switch (findEvent.priority) {
      case '1':
        pGt = '0';
        pLte = '2';
        break;
      case '2':
        pGt = '2';
        pLte = '4';
        break;
      case '3':
        pGt = '4';
        pLte = '6';
        break;
    }
    findEvent.priority = {
      $gt: pGt,
      $lte: pLte
    }
  }

  var rModel = Richeng.find(findEvent).sort({
    eventDate: sort
  })
  rModel.exec(function (err, doc) {
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
      if (doc.n == 1){
        res.json({
          status: '1',
          msg: '',
          result: 'delete event success!'
        });
      } else {
        res.json({
          status: '2004',
          msg: '',
          result: 'delete event failed!'
        });
      }
    }
  });
});

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
  }, {
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
      if (doc.n == 1) {
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

// 查询日程接口(只返回时间、用于表示日历上的日期)
router.get('/findAllEvent', function (req, res, next) {
  var userId = req.query.userId;
  Richeng.find({
    userId: userId
  }, function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc) {
        var returnTime = [];
        doc.forEach(function (item) {
          returnTime.push(item.eventDate);
        });
        // Set对象解构赋值到数组
        var newArr = [...new Set(returnTime)];
        var arr = new Array(newArr.length);
        // 将新数组的每个元素初值设为0
        for (let k = 0; k < arr.length; k++) {
          arr[k] = 0;
        }
        for (let i = 0; i < newArr.length; i++) {
          for (let j = 0; j < returnTime.length; j++) {
            if (newArr[i] == returnTime[j]) {
              arr[i]++;
            }
          }
        }
        console.log(newArr);
        console.log(arr);
        res.json({
          status: '1',
          msg: '',
          result: newArr,
          count: arr
        })
      } else {
        res.json({
          status: '2003',
          msg: '',
          result: 'This user has no events!'
        })
      }
    }
  })
});

// 添加闹钟接口
router.post('/addClock', function (req, res, next) {
  var userId = req.body.userId;
  var eventId = req.body.eventId;
  var alertDate = req.body.alertDate;
  var alertTime = req.body.alertTime;
  var choosedSong = req.body.choosedSong;

  // 随机生成clockId
  var platform = 'Clo';
  // 创建两个随机数
  var ran1 = Math.floor(Math.random() * 10);
  var ran2 = Math.floor(Math.random() * 10);
  var sysDate = new Date().Format('yyyyMMddhhmmss');
  var clockId = platform + ran1 + sysDate + ran2;

  var newClockList = {
    clockId: clockId,
    alertDate: alertDate,
    alertTime: alertTime,
    choosedSong: choosedSong
  }

  Richeng.update({
    userId: userId,
    eventId: eventId
  }, {
    $push: {
      clockList: newClockList
    }
  }, function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc.n == 1) {
        res.json({
          status: '1',
          msg: '',
          result: 'Add clock success!'
        })
      } else {
        res.json({
          status: '3001',
          msg: '',
          result: 'Add clock failed!'
        })
      }
    }
  })
});

// 查询用户的所有闹钟接口
router.get('/findAllClock', function (req, res, next) {
  var userId = req.query.userId;
  Richeng.find({
    userId: userId
  }).sort({
    eventDate: -1
  }).exec(function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc.length != 0) {
        var returnResult = [];
        doc.forEach((item) => {
          item.clockList.forEach((clockItem) => {
            let temp = {
              eventId: item.eventId,
              eventTitle: item.eventTitle,
              eventDate: item.eventDate,
              startTime: item.startTime,
              clockId: clockItem.clockId,
              alertDate: clockItem.alertDate,
              alertTime: clockItem.alertTime,
              choosedSong: clockItem.choosedSong
            }
            returnResult.push(temp);
          })
        })
        res.json({
          status: '1',
          msg: '',
          result: returnResult
        })
      } else {
        res.json({
          status: '3002',
          msg: '',
          result: 'No clock find!'
        })
      }
    }
  })
});

// 编辑闹钟接口
router.post("/editClock", function (req, res, next) {
  var userId = req.body.userId;
  var eventId = req.body.eventId;
  var clockId = req.body.clockId;
  var alertDate = req.body.alertDate;
  var alertTime = req.body.alertTime;
  var choosedSong = req.body.choosedSong;

  Richeng.update({
    userId: userId,
    eventId: eventId,
    clockList: {
      "$elemMatch": {
        clockId: clockId
      }
    }
  }, {
    "clockList.$.alertDate": alertDate,
    "clockList.$.alertTime": alertTime,
    "clockList.$.choosedSong": choosedSong
  }, function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc.n == 1) {
        res.json({
          status: '1',
          msg: '',
          result: 'edit clock success'
        });
      } else {
        res.json({
          status: '3003',
          msg: '',
          result: 'edit clock failed'
        });
      }
    }
  })
});

// 删除闹钟接口
router.post('/deleteClock', function (req, res, next) {
  var userId = req.body.userId;
  var eventId = req.body.eventId;
  var clockId = req.body.clockId;

  Richeng.update({
    userId: userId,
    eventId: eventId,
  },{
    $pull: {
      'clockList': {
        clockId: clockId
      }
    }
  }, function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc.n == 1) {
        res.json({
          status: '1',
          msg: '',
          result: 'delete clock success!'
        });
      } else {
        res.json({
          status: '3004',
          msg: '',
          result: 'delete clock failed!'
        })
      }
    }
  });
});

// 查询具体闹钟接口
router.post('/searchClock', function (req, res, next) {
  var userId = req.body.userId;
  var eventId = req.body.eventId;
  var clockId = req.body.clockId;

  Richeng.findOne({
    userId: userId,
    eventId: eventId,
    clockList: {
      "$elemMatch": {
        clockId: clockId
      }
    }
  }, function (err, doc) {
    if (err) {
      res.json({
        status: '0',
        msg: err.message
      });
    } else {
      if (doc.length != 0) {
        var returnResult = {
          eventId: doc.eventId,
          eventTitle: doc.eventTitle,
          eventDate: doc.eventDate,
          startTime: doc.startTime,
          endTime: doc.endTime,
          clockId: doc.clockList[0].clockId,
          alertDate: doc.clockList[0].alertDate,
          alertTime: doc.clockList[0].alertTime,
          choosedSong: doc.clockList[0].choosedSong
        };
        res.json({
          status: '1',
          msg: '',
          result: returnResult
        });
      } else {
        res.json({
          status: '3005',
          msg: '',
          result: 'have no such a clock!'
        });
      }
    }
  });
});

module.exports = router;
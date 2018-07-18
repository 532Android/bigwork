var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
require('./../util/util.js');
var Account = require('../models/account');

// 查询账单接口
router.post('/searchAccount', function (req, res, next) {
  var userId = req.body.userId;
  var accountId = req.body.accountId;
  var accountType = req.body.accountType;
  var year = req.body.year;
  var month = req.body.month;
  var day = req.body.day;
  
  var findAccount = {
    userId: userId,
    accountId: accountId,
    accountType: accountType,
    year: year,
    month: month,
    day: day
  }
  
  // 遍历对象属性，删除掉值为undefined的属性
  for (var key in findAccount) {
    if (key != userId) {
      if (findAccount[key] == undefined) {
        delete findAccount[key];
      }
    }
  }

  Account.find(findAccount).sort({
    day: -1
  }).exec(function (err, doc) {
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
        })
      } else {
        res.json({
          status: '4001',
          msg: '',
          result: 'Have no account data!'
        });
      }
    }
  });
});

// 添加账单接口
router.post('/addAccount', function (req, res, next) {
  var userId = req.body.userId;
  var accountTitle = req.body.accountTitle;
  var accountType = req.body.accountType;
  var year = req.body.year;
  var month = req.body.month;
  var day = req.body.day;
  var moneyType = req.body.moneyType;
  var money = req.body.money;
  var beizhu = req.body.beizhu || '';

  // 随机生成accountId
  var platform = 'Acc';
  // 创建两个随机数
  var ran1 = Math.floor(Math.random() * 10);
  var ran2 = Math.floor(Math.random() * 10);
  var sysDate = new Date().Format('yyyyMMddhhmmss');
  var accountId = platform + ran1 + sysDate + ran2;

  var accountDoc = {
    userId: userId,
    accountId: accountId,
    accountTitle: accountTitle,
    accountType: accountType,
    year: year,
    month: month,
    day: day,
    moneyType: moneyType,
    money: money.toFixed(2),
    beizhu: beizhu
  }
  var accountModel = mongoose.model('account', Account.schema);
  var newAccount = new accountModel(accountDoc);

  newAccount.save(function (err, doc) {
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
          result: 'Add account success!'
        });
      } else {
        res.json({
          status: '4002',
          msg: '',
          result: 'Add account Falied!'
        });
      }
    }
  });
});

// 删除账单接口
router.post('/deleteAccount', function (req, res, next) {
  var accountId = req.body.accountId;
  Account.remove({
    accountId: accountId
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
          result: 'delete account success!'
        });
      } else {
        res.json({
          status: '4003',
          msg: '',
          result: 'delete account failed!'
        });
      }
    }
  });
});


// 编辑账单接口
router.post('/editAccount', function (req, res, next) {
  var userId = req.body.userId;
  var accountId = req.body.accountId;
  var accountTitle = req.body.accountTitle;
  var accountType = req.body.accountType;
  var year = req.body.year;
  var month = req.body.month;
  var day = req.body.day;
  var moneyType = req.body.moneyType;
  var money = req.body.money;
  var beizhu = req.body.beizhu;

  Account.update({
    userId: userId,
    accountId: accountId
  }, {
    accountTitle: accountTitle,
    accountType: accountType,
    year: year,
    month: month,
    day: day,
    moneyType: moneyType,
    money: money.toFixed(2),
    beizhu: beizhu
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
          result: 'edit account success!'
        });
      } else {
        res.json({
          status: '4004',
          msg: '',
          result: 'edit account failed'
        })
      }
    }
  });
});
module.exports = router;
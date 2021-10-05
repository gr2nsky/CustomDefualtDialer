const mysql = require('mysql')
const dbconfig = require('../config/database.js')
const connection = mysql.createConnection(dbconfig)
const Person = require('../dto/person.js');

var moment = require('moment');

module.exports.downloadTaskMain = function(req, res){
    //#1. 단말로부터 phone number를 받음
    var bodyByJSON =  JSON.parse(req.body["device_data"]) //web
    // var bodyByJSON = req.body["device_data"] //local
  
    var phone = bodyByJSON.phone
    console.log("phone : " + phone);


    let personsJson = new Array()
    var query = 'SELECT * FROM person WHERE pOwner = ' + phone

  connection.query(query, (error, rows) => {
    if(error) throw error;
    console.log(rows)
    console.log("-------------------------------------")
    for (var i in rows){
      let person = new Object()
      person.pName = rows[i].pName
      person.pPhoneNumber = rows[i].pPhoneNumber
      person.pImagePath = rows[i].pImagePath
      person.pEmail = rows[i].pEmail
      person.pResidence = rows[i].pResidence
      person.pMemo = rows[i].pMemo
      var thisMoment = moment(rows[i].pUpdateDate);
      var day = thisMoment.format("YYYY-MM-DD HH:mm:ss")
      person.pUpdateDate = day

      personsJson.push(person)
    }

    console.log(personsJson)
    res.json(personsJson)
  })
}
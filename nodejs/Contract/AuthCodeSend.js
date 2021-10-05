const mysql = require('mysql')
const dbconfig = require('../config/database.js')
const connection = mysql.createConnection(dbconfig)
const AuthCodeMake = require('./AuthCodemake.js')
/*
    1. phone을 받는다
    2. auth code를 만든다
    3. UPDATE user SET auth_code = [this_auth_code] WHERE phone = [tihs_request_phone]
    4. 작업의 결과를 리턴 
*/
module.exports.authCodeSend = function(req, res){
    var bodyByJSON = JSON.parse(req.body["device_data"]) //web
    // var bodyByJSON = req.body["device_data"] //local
    
    var phone = bodyByJSON.phone
    var authCode = AuthCodeMake.authCodeMake() 
    console.log("phone : " + phone + ", atuh code = : " + authCode);

    var query = "UPDATE user SET auth_key = ? WHERE phone = ?"
    var value = [authCode, phone]

    connection.query(query, value, (error, results) => {
        if(error) throw error;
    
        console.log(results)
        res.send(authCode)
    })
}
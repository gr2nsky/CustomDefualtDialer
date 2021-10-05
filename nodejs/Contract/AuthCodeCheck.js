const mysql = require('mysql')
const dbconfig = require('../config/database.js')
const connection = mysql.createConnection(dbconfig)

module.exports.authCodeCheck = async function(req, res){
    console.log("hi")
    var bodyByJSON =  JSON.parse(req.body["device_data"]) //web
    // var bodyByJSON = req.body["device_data"] //local
    
    //#1 phone, uuid, authCode 획득
    var phone = bodyByJSON.phone
    var clientUUID = bodyByJSON.uuid
    var clientAuthCode = bodyByJSON.authCode
    console.log("phone : " + phone + ", clientUUID : " + clientUUID + ", authCode : " + clientAuthCode)


    //#2 phone이 갖는 auth_key 획득
    var serverAuthCode = ""
    await getUserUUIDByServer(phone).then((resolve) => {
        serverAuthCode = resolve
        console.log("server auth_key : " + serverAuthCode);
      })

    //#2 reqeust로 받은 uuid, server의 uuid 비교
    if (clientAuthCode != serverAuthCode){
        return res.send("false")
    }
    //#2.1 같다면, request로 받은 uuid를 phone의 uuid로 세팅
    var query = "UPDATE user SET uuid = ? WHERE phone = ?"
    var value = [clientUUID, phone]

    connection.query(query, value, (error, results) => {
        if(error) throw error;
        res.send("true")
    })
}

function getUserUUIDByServer(phone){
    return new Promise((resolve, rejects) => {
        var query = 'SELECT auth_key FROM user WHERE phone = ' + phone
        var result = ""
        connection.query(query, (error, rows) => {
            if(error) throw error;
    
            if(rows.length == 1){
                result = rows[0].auth_key
            }
            resolve(result)
        })
    })
}
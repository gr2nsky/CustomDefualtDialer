const mysql = require('mysql')
const dbconfig = require('../config/database.js')
const connection = mysql.createConnection(dbconfig)
/*
    1. 단말로부터 phone, uuid를 받음
    2. phone이 user 테이블에 존재하는지 질의
        2.1 없다면, 생성하고 true / new_false 반환
    3. phone으로 찾은 user 튜플의 uuid와 단말기로 받은 uuid가 같은지
        3.1 같다면, return true
    4. 10자리 auth_key를 생성해 튜플에 삽입하고 need_auth 반환
*/
module.exports.checkRegisteredDevice = async function (req, res){
    //#1. 단말로부터 phone, uuid를 받음
    var bodyByJSON =  JSON.parse(req.body["device_data"]) //web
    // var bodyByJSON = req.body["device_data"] //local
    
    var uuid = bodyByJSON.uuid
    var phone = bodyByJSON.phone
    console.log("uuid : " + uuid + ", phone : " + phone);

    //#2. phone이 user 테이블에 존재하는지 질의
    var isRegistered = false
    await isRegisteredUser(phone).then((resolve) => {
        isRegistered = resolve
        console.log("isRegistered : " + isRegistered);
      })

    if (isRegistered){
        //#3. phone으로 찾은 user 튜플의 uuid와 단말기로 받은 uuid가 같은지
        var matchUD = "false"
        await matchUserDeivice(phone, uuid).then((resolve) => {
            matchUD = resolve
            console.log("matchUserDeivice : " + resolve);
        })
        //#3.1 같다면, return true
        if(matchUD == "true"){
            return res.send("true")
        }
        //#4. 10자리 auth_key를 생성해 튜플에 삽입하고 need_auth 반환
        res.send("need_auth")
    }else{
        //#2.1 없다면, 생성하고 true / new_false 반환
        await registerUser(phone, uuid).then((resolve) => {
            console.log("registerUser : " + resolve);
            res.send(resolve);
          })
    }
}

function isRegisteredUser(phone){
    return new Promise((resolve, rejects) => {
        var query = 'SELECT * FROM user WHERE phone = ' + phone
        var result = false
        connection.query(query, (error, rows) => {
            if(error) throw error;
    
            if(rows.length > 0){
                result = true
            }
            resolve(result)
        })
    })
}

async function registerUser(phone, uuid) {
    return new Promise((resolve, rejects) => {
        var query = "INSERT INTO user (phone, uuid) VALUES (?, ?)"
        var value = [phone, uuid]
        var result = false;
      
        connection.query(query, value, (error, results) => {
          if(error) throw error;
          console.log("registerUser result : " + results)
    
          if(results.affectedRows == 1){
            result = true
          }
          resolve(result)
        })
    })
}

function matchUserDeivice(phone, uuid){
    return new Promise((resolve, rejects) => {
        var result = "false"
        var query = 'SELECT * FROM user WHERE phone = ? AND uuid = ?'
        var value = [phone, uuid]

        connection.query(query, value, (error, rows) => {
            if(error) throw error
    
            console.log(rows)
            if(rows.length > 0){
                result = "true"
            } 
            resolve(result)
        });
    })
}

function registerAuthCode(phone){
    
}

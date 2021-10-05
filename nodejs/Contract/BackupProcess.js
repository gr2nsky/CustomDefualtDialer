const mysql = require('mysql')
const dbconfig = require('../config/database.js')
const connection = mysql.createConnection(dbconfig)
const Person = require('../dto/person.js');

//백업 작업의 main
module.exports.backupTaskMain = async function(req, res){
    let postPersons = getRequestPerson(req)
    var getDevicePhone = JSON.parse(req.body["person"]).phone
    let dbPersons = []
    await getDBPerson(getDevicePhone)
      .then((resolve) => {
        dbPersons = resolve
      })
      .catch((rejects) => {
        res.send("false")
        return;
      })

    
    setDatasBeforeSet(dbPersons, postPersons, getDevicePhone)
    console.log("작업 종료")
    res.send("true")
}

//단말에서 받은 data를 배열로 만들어 반환
function getRequestPerson(req){
    var getRequestPersons = JSON.parse(req.body["person"]).person
    var postPersonArray = []
  
    for (i in getRequestPersons){
      var person = new Person(
        getRequestPersons[i].pNo,
        getRequestPersons[i].pName,
        getRequestPersons[i].pPhoneNumber,
        getRequestPersons[i].pImagePath,
        getRequestPersons[i].pEmail,
        getRequestPersons[i].pResidence,
        getRequestPersons[i].pMemo,
        getRequestPersons[i].pUpdateDate
      )
  
      postPersonArray.push(person)
    }

    return sortPerson(postPersonArray)
}

//MySQL 서버에서 data를 받아 배열로 만들어 반환
function getDBPerson(phone){
  return new Promise((resolve, rejects) => {
    var query = 'SELECT * FROM person WHERE pOwner = ' + phone;
    var dbPersonArray = []
    connection.query(query, (error, rows)=>{
      if (error) {
        rejects("error")
        return
      }
      for (i in rows){
        var person = new Person(
          rows[i].pNo,
          rows[i].pName,
          rows[i].pPhoneNumber,
          rows[i].pImagePath,
          rows[i].pEmail,
          rows[i].pResidence,
          rows[i].pMemo,
          rows[i].pUpdateDate
        )
      dbPersonArray.push(person)
    }
    sortPerson(dbPersonArray)
    resolve(dbPersonArray)
    })
  })
}

//배열을 이름 순서에 따라서 정렬
function sortPerson(array){
    var tempArr = array.slice();
    tempArr.sort(function(a, b){
      if(a.pName.length < b.pName.length){
        return -1
      }else if(a.pName.length > b.pName.length) {
        return 1
      } else{
        if (a.pName < b.pName){
          return -1
        }else if (a.pName > b.pName){
          return 1
        }else {
          for(var i = 0; i < a.pPhoneNumber.length; i++){
            var ap = a.pPhoneNumber.charAt(i)
            var bp = b.pPhoneNumber.charAt(i)
            if ( ap < bp){
              return -1
            }else if (ap > bp){
              return 1
            }
          }
        }
        return 1
      } 
    })
  return tempArr;
}

//SQLite의 data와 MySQL의 data를 추합해 넣을 자료를 판단
function setDatasBeforeSet(serverPersons, clientPersons, getDevicePhone){
  var indexCousor = 0
  var pasteToken = 0

  try{
    for(var i in clientPersons){
      for(var j = 0; j < serverPersons.length ; j++){
        console.log("[j : " + j + " ]")
        if(clientPersons[i].pName == serverPersons[j].pName){
          if(clientPersons[i].pPhoneNumber == serverPersons[j].pPhoneNumber){
            console.log("덮어쓸지 여부를 판단합니다.")
            console.log(" client time " + clientPersons[i].pUpdateDate)
            console.log(" server time " + serverPersons[j].pUpdateDate)
            var clientTime = Date.parse(clientPersons[i].pUpdateDate)
            var serverTiem = Date.parse(serverPersons[j].pUpdateDate)
            pasteToken = 1
            //받은 정보가 더 최신이면 덮어쓰고
            if(clientTime > serverTiem){
              console.log("클라이언트의 정보가 더 최신이므로 덮어씁니다.") 
              updatePerson(clientPersons[i], serverPersons[j])
              console.log(clientPersons[i].print())
            }
            console.log("서버가 더 최신이므로 건너뜁니다.")
            //서버 정보가 최신이면 추가작업을 실행하지 않는다.
            break
          }
        }
      }
      if(pasteToken == 0){
        console.log("=================================")
        console.log("추가합니다.")
        console.log(clientPersons[i].print())
        insertPerson(clientPersons[i], getDevicePhone)
      }
      pasteToken = 0
    }
  }catch(error){
    console.log("[error]\n" + error)
    return
  }
}

function updatePerson(cPerson, sPerson){
  var query = "UPDATE person SET pImagePath = ?, " + 
    "pEmail = ?, pResidence = ?, pMemo = ?, pUpdateDate = ?, pOwner = ? WHERE pNo = ?"
  var value = [
    cPerson.pImagePath, 
    cPerson.pEmail, 
    cPerson.pResidence, 
    cPerson.pMemo, 
    cPerson.pUpdateDate,
    sPerson.pNo
  ]

  connection.query(query, value, (error, results) => {
    if(error) throw error;
    console.log(results)
  })
}

function insertPerson(person, getDevicePhone){
  var query = "INSERT INTO person (pName, pPhoneNumber, pImagePath, pEmail, pResidence, pMemo, pUpdateDate, pOwner) " +
  "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
  var value = [
    person.pName, 
    person.pPhoneNumber, 
    person.pImagePath, 
    person.pEmail, 
    person.pResidence, 
    person.pMemo,
    person.pUpdateDate,
    getDevicePhone
  ]

  connection.query(query, value, (error, results) => {
    if(error) throw error;
    console.log(results)
  })
}
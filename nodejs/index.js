const express = require('express')
const path = require('path')
const { resolve } = require('path')
const { rejects } = require('assert')
const { off } = require('process')
const e = require('express')
const { callbackify } = require('util')
const PORT = process.env.PORT || 5000

const app = express();
app.use(express.static(path.join(__dirname, 'public'))) 
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'ejs')
app.listen(PORT, () => console.log(`Listening on ${ PORT }`))

app.use(express.json())
app.use(express.urlencoded({ extended: true}))

// root
app.get("/", (req, res) => {
  res.send("hello")
})

//homework root dir
const contractRoot = "./Contract/"
//person class
const Person = require('./dto/person.js')

///////////////
//back up task
const BackupTaskMain = require('./Contract/BackupProcess.js')
//백업작업을 지시하는 uri 수신
app.post("/backup", (req, res) => {
  BackupTaskMain.backupTaskMain(req, res)
})

///////////////
//download task
const DownloadTaskMain = require('./Contract/DownloadProcess.js')
//연락처 내려받기 uri 수신
app.post("/download", (req, res) => {
  DownloadTaskMain.downloadTaskMain(req, res)
})

///////////////
//download task
const CheckRegisteredDevice = require('./Contract/CheckRegisteredDevice.js')
//기존 사용하던 환경인지 확인
app.post("/CheckValidUserDevice", (req, res) => {
  CheckRegisteredDevice.checkRegisteredDevice(req, res)
})

const AuthCodeSend = require('./Contract/AuthCodeSend.js')
//인증코드 발행 요청
app.post("/requestAuthCodeTask", (req, res) => {
  AuthCodeSend.authCodeSend(req, res)
})

const AuthCodeCheck = require('./Contract/AuthCodeCheck.js')
//인증코드 확인 요청
app.post("/checkValidAuthCodeTask", (req, res) => {
  AuthCodeCheck.authCodeCheck(req, res)
})



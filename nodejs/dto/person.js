module.exports = class Person {
    constructor(pNo, pName, pPhoneNumber, pImagePath, pEmail, pResidence, pMemo, pUpdateDate){
        this.pNo = pNo
        this.pName = pName
        this.pPhoneNumber = pPhoneNumber
        this.pImagePath = pImagePath
        this.pEmail = pEmail
        this.pResidence = pResidence
        this.pMemo = pMemo
        this.pUpdateDate = pUpdateDate
    }
    print(){
        return "pNo : " + this.pNo + ", pName : " +  this.pName + 
            ", pPhone : " + this.pPhoneNumber + ", pUpdateDate : " + this.pUpdateDate 
    }
}
  
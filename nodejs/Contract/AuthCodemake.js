module.exports.authCodeMake = function(){
    var authCode = ""

    for (var i = 0; i < 10; i++) {
        var rIndex = getRandomInt(0, 3)
        switch (rIndex) {
        case 0:
            // a-z
            var ran = getRandomInt(0, 26) + 97
            authCode += String.fromCharCode(ran)
            break;
        case 1:
            // A-Z
            var ran = getRandomInt(0, 26) + 65
            authCode += String.fromCharCode(ran)
            break;
        case 2:
            // 0-9
            var ran = getRandomInt(0, 10)
            authCode += ran
            break;
        }
    }
    console.log(authCode)
    return authCode
}
    
//min ~ max 사이의 임의의 정수 반환
function getRandomInt(min, max) { 
    return Math.floor(Math.random() * (max - min)) + min;
}
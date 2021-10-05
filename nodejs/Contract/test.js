var a = '2021-10-02 07:53:33'
var b = '2021-10-02 07:53:32'

console.log(" a time " + a)
console.log(" a time parse : " + Date.parse(a))
console.log(" b time " + b)
console.log(" b time parse : " + Date.parse(b))

console.log(" string b - a : " + ( b - a))
console.log(" date type a - b : " + ( Date.parse(b) - Date.parse(a)))
console.log(" date type b - a : " + ( Date.parse(b) - Date.parse(a)))


console.log(" a > b ? : " + ( Date.parse(b) < Date.parse(a)))
console.log(" a < b ? : " + ( Date.parse(b) > Date.parse(a)))
console.log(" a == b ? : " + ( Date.parse(b) == Date.parse(a)))
package com.example.myapplication.DTO;

/**
 * @author Yoon
 * @created 2021-09-08
 */
public class PersonDTO implements Comparable<PersonDTO>{

    int no;
    String name;
    String phoneNumber;
    String imagePath;
    String email;
    String residence;
    String memo;
    int isChanged = 1;

    //입력용 :  nullable인 항목들은 setter를 사용해 값이 있을때만 넣도록 적용할 예정
    public PersonDTO(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    //내려받기용
    public PersonDTO(String name, String phoneNumber, String imagePath, String email, String residence, String memo, int isChanged) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imagePath = imagePath;
        this.email = email;
        this.residence = residence;
        this.memo = memo;
        this.isChanged = isChanged;
    }

    //수정용
    public PersonDTO(int no, String name, String phoneNumber) {
        this.no = no;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isChanged = 1;
    }

    //출력용
    public PersonDTO(int no, String name, String phoneNumber, String imagePath,
                     String email, String residence, String memo, int isChanged) {
        this.no = no;
        this.name = name;
        this.imagePath = imagePath;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.residence = residence;
        this.memo = memo;
        this.isChanged = isChanged;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(int isChanged) {
        this.isChanged = isChanged;
    }

    public String pringAll(){
        return "\nint no : " + no + "\n" +
                "String name : " + name + "\n" +
                "String phoneNumber : " + phoneNumber + "\n" +
                "String imagePath : " + imagePath + "\n" +
                "String email : " + email + "\n" +
                "String residence : " + residence + "\n" +
                "String memo : " + memo + "\n" +
                "int isChanged : " + isChanged;
    }

    /*
        정렬 규칙
        1. 전화번호 기준 정렬
            a. String type으로 변환해 같은지 비교
                i : 이름기준 정렬 - 오름차순
            b. 길이 비교
            c. 0번째 index부터 ASCII로 치환해 한글자씩 비교
     */
    @Override
    public int compareTo(PersonDTO other) {
        String thisPhone = this.phoneNumber;
        String otherPhone = other.getPhoneNumber();

        if(isEqualsStr(thisPhone, otherPhone)){
            //이름으로 정렬해 결과 반환, 둘다 같으면 0 반환
       }
        int sbl = sortBylen(thisPhone, otherPhone);
        if( sbl != 0){
            return sbl;
        }

        return sortByASCII(thisPhone, otherPhone);
    }

    public int sortBylen(String thisPhone, String otherPhone){
        if(otherPhone.length() < thisPhone.length()){
            return 1;
        } else if (otherPhone.length() > thisPhone.length()){
            return -1;
        }
        return 0;
    }

    public Boolean isEqualsStr(String thisPhone, String otherPhone){
        if(thisPhone.equals(otherPhone)){
            return true;
        }
        return false;
    }

    public int sortByASCII(String thisPhone, String otherPhone){
        for(int i = 0; i < thisPhone.length(); i++){
            if(otherPhone.charAt(i) < thisPhone.charAt(i)){
                return 1;
            } else if (thisPhone.charAt(i) < otherPhone.charAt(i)){
                return -1;
            }
        }
        return 0;
    }
}

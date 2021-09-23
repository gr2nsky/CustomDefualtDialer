package com.example.myapplication.DTO;

import android.util.Log;

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
    public PersonDTO(String name, String phoneNumber, String imagePath, String email, String residence, String memo) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imagePath = imagePath;
        this.email = email;
        this.residence = residence;
        this.memo = memo;
        this.isChanged = 0;
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
        1. 이름의 첫 글자 비교
        2. 이름의 길이 비교
        3. 이름의 각 문자마다 비교
        4. 전화번호 첫 글자 비교
        5. 전화번호 길이 비교
        4. 전화번호 각 문자마다 비교
     */
    @Override
    public int compareTo(PersonDTO other) {
        String thisName = this.getName();
        String otherName = other.getName();
        String thisPhone = this.phoneNumber;
        String otherPhone = other.getPhoneNumber();

        int result = cimpareCondition(thisName, otherName);
        if(result != 0){
            return result;
        }
        return cimpareCondition(thisPhone, otherPhone);


//        if(thisName.length() < otherName.length()){
//            return -1;
//        }  else if(thisName.length() > otherName.length()) {
//            return 1;
//        } else{
//            int comp = thisName.compareTo(otherName);
//            if( comp != 0){
//                return comp;
//            }else {
//                int cp = compPhone(thisPhone, otherPhone);
//                if(cp != 0){
//                    return cp;
//                }
//            }
//            return 1;
//        }
    }

    private int cimpareCondition(String thisStr, String otherStr){
        if(thisStr.charAt(0) < otherStr.charAt(0)){
            return -1;
        } else if (thisStr.charAt(0) > otherStr.charAt(0)){
            return 1;
        } else {
            //condition 2: text length
            if(thisStr.length() < otherStr.length()){
                return -1;
            }else if(thisStr.length() > otherStr.length()) {
                return 1;
            } else{
                //condition 3: text singly code
                int comp = thisStr.compareTo(otherStr);
                if( comp != 0){
                    return comp;
                }else {
                    return  0;
                }
            }
        }
    }

    private int compPhone(String thisPhone, String otherPhone){

        for(int i = 0; i < thisPhone.length(); i++){
            int ap = thisPhone.charAt(i);
            int bp = otherPhone.charAt(i);
            if ( ap < bp){
                return -1;
            }else if (ap > bp){
                return 1;
            }
        }
        return 0;
    }
}

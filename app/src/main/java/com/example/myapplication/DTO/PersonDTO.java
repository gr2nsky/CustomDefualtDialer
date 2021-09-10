package com.example.myapplication.DTO;

/**
 * @author Yoon
 * @created 2021-09-08
 */
public class PersonDTO {

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
}

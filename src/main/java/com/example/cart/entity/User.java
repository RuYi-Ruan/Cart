package com.example.cart.entity;

public class User {
    private String cardNo;
    private String username;
    private String sex;
    private Integer age;
    private String password;

    public User(){}

    public User(String cardNo, String username, String sex, Integer age, String password) {
        this.username = username;
        this.password = password;
        this.cardNo = cardNo;
        this.age = age;
        this.sex = sex;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [cardNo=" + cardNo + ", username=" + username + "gender=" + sex + "]";
    }
}

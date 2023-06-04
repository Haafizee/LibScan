package com.example.admin_main_page;

import java.util.Map;

public class User {
    private String Name, Email,uid;

    Map<String, Book> Books;
    private int userType;


    public User() {


    }

    public User(String name, String email, int userType) {
        Name = name;
        Email = email;
        this.userType = userType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUserType() {
        return userType;
    }

    public User(String uid, String name, int userType,  Map<String , Book> books, String email) {
        Name = name;
        Email = email;
        Books = books;
        this.uid = uid;
        this.userType = userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

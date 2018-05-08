package com.ngocbich.mediaplayerdemo.Model;

/**
 * Created by Ngoc Bich on 5/3/2018.
 */

public class User {
    private String Name;
    private String Password;

    public User(String name, String password) {
        this.Name = name;
        this.Password = password;
    }

    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}

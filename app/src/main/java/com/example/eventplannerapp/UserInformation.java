package com.example.eventplannerapp;

public class UserInformation {
    public String name;
    public String birthday;
    public String bio;
    public String email;

    public UserInformation(){}
    public UserInformation(String name, String birthday, String bio, String email){
        this.name = name;
        this.birthday = birthday;
        this.bio = bio;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }
    public String getBio() {
        return this.bio;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public String getEmail() {
        return this.email;
    }

}

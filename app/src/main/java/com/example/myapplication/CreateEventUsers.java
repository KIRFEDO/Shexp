package com.example.myapplication;

public class CreateEventUsers {
    public String email;
    public String login;
    public String uid;

    public CreateEventUsers(){};

    public CreateEventUsers(String email, String login, String uid) {
        this.email = email;
        this.login = login;
        this.uid = uid;
    }
}

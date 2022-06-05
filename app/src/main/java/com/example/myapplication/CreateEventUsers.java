package com.example.myapplication;

import java.io.Serializable;

public class CreateEventUsers implements Serializable {
    public String email;
    public String login;
    public String uid;
    public int avatarId;

    public CreateEventUsers(){};

    public CreateEventUsers(String email, String login, String uid, int avatarId) {
        this.email = email;
        this.login = login;
        this.uid = uid;
        this.avatarId = avatarId;
    }
}

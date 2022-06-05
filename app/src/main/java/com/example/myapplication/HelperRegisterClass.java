package com.example.myapplication;

public class HelperRegisterClass {
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public HelperRegisterClass(String login, String email, String uid, int avatarId) {
        this.login = login;
        this.email = email;
        this.uid = uid;
        this.avatarId = avatarId;
    }

    public HelperRegisterClass(){};

    String login;
    String email;
    String uid;
    int avatarId;
}

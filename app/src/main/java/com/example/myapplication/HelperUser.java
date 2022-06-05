package com.example.myapplication;

public class HelperUser {
    int avatarId;
    String name;
    boolean remove_option;

    public HelperUser(int avatarId, String name, boolean remove_option) {
        this.avatarId = avatarId;
        this.name = name;
        this.remove_option = remove_option;
    }

    public HelperUser(CreateEventUsers user, boolean remove_option){
        this.avatarId = user.avatarId;
        this.name = user.login;
        this.remove_option = remove_option;
    }
}

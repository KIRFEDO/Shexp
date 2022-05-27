package com.example.myapplication;

public class HelperUser {
    String avatar;
    String name;
    boolean remove_option;

    public HelperUser(String avatar, String name, boolean remove_option) {
        this.avatar = avatar;
        this.name = name;
        this.remove_option = remove_option;
    }

    public HelperUser(CreateEventUsers user, boolean remove_option){
        this.avatar = avatar;
        this.name = user.login;
        this.remove_option = remove_option;
    }
}

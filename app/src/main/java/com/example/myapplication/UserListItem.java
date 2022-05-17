package com.example.myapplication;

public class UserListItem {
    String avatar;
    String name;
    boolean remove_option;

    public UserListItem(String avatar, String name, boolean remove_option) {
        this.avatar = avatar;
        this.name = name;
        this.remove_option = remove_option;
    }

    public UserListItem(CreateEventUsers user, boolean remove_option){
        this.avatar = avatar;
        this.name = user.login;
        this.remove_option = remove_option;
    }
}

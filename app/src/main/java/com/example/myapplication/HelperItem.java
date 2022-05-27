package com.example.myapplication;

import java.util.ArrayList;

public class HelperItem {
    String owner_uid;
    String owner_login;
    Integer amount;

    public String getOwner_uid() {
        return owner_uid;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public String getOwner_login() {
        return owner_login;
    }

    public void setOwner_login(String owner_login) {
        this.owner_login = owner_login;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public HelperItem(String owner_uid, String owner_login, Integer amount) {
        this.owner_uid = owner_uid;
        this.owner_login = owner_login;
        this.amount = amount;
    }


    public HelperItem(){};


}

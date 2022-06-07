package com.example.myapplication;

public class HelperItem {
    String owner_uid;
    String owner_login;
    float amount;
    String image_key;

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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getImage_key() {
        return image_key;
    }

    public void setImage_key(String image_key) {
        this.image_key = image_key;
    }

    public HelperItem(String owner_uid, String owner_login, float amount, String image_key) {
        this.owner_uid = owner_uid;
        this.owner_login = owner_login;
        this.amount = amount;
        this.image_key = image_key;
    }

    public HelperItem(){};
}

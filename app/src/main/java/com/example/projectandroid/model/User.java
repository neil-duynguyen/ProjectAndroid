package com.example.projectandroid.model;

public class User {

    private String name;
    private String email;
    private String image;
    private long wallet;
    private String role ;

    private String phone;


    public User() {
    }

    public User(String name, String email,long wallet, String image,String Role,String Phone) {
        this.name = name;
        this.email = email;
        this.wallet = wallet;
        this.image = image;
        this.role = Role;
        this.phone = Phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getWallet() {
        return wallet;
    }

    public void setWallet(long wallet) {
        this.wallet = wallet;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone;
    }
}

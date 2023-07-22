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

    public User(String name, String email, String image, long wallet, String role, String phone) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.wallet = wallet;
        this.role = role;
        this.phone = phone;
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
        this.phone = phone;
    }
}

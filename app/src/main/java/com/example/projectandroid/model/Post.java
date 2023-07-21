package com.example.projectandroid.model;

public class Post {
    public String location;
    public long price;
    public String description;
    public String shortDescription;
    public String image;
    public String id;
    public String userID;
    public String address;

    public Post() {
    }

    public Post(String location, long price, String description, String shortDescription, String image, String id, String userID, String address) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.image = image;
        this.id = id;
        this.userID = userID;
        this.address = address;
    }
}

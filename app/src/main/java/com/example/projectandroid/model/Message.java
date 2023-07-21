package com.example.projectandroid.model;
public class Message {
    private String username; // Tên người dùng (email)
    private String content; // Nội dung tin nhắn

    public Message() {
        // Empty constructor for Firebase
    }

    public Message(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

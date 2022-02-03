package com.example.petsonline;

public class MessageAttr {
    String id;
    String recieverId;
    String senderId;
    String message;
    String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MessageAttr() {
    }

    public MessageAttr(String id, String recieverId, String senderId, String message, String date) {
        this.id = id;
        this.recieverId = recieverId;
        this.senderId = senderId;
        this.message = message;
        this.date = date;
    }
}

package com.petsonline.models;

public class MessageAttr {
    String id;
    String ReceiverId;
    String SenderId;
    String message;
    String date;

    public MessageAttr() {
    }

    public MessageAttr(String id, String receiverId, String senderId, String message, String date) {
        this.id = id;
        ReceiverId = receiverId;
        SenderId = senderId;
        this.message = message;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
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
}

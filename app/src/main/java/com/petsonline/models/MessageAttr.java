package com.petsonline.models;

public class MessageAttr {
    String id;
    String ReceiverId;
    String SenderId;
    String message;
    String date;
    String imgURL;

    public MessageAttr() {
    }

    public MessageAttr(String id, String receiverId, String senderId, String message, String date, String imgURL) {
        this.id = id;
        ReceiverId = receiverId;
        SenderId = senderId;
        this.message = message;
        this.date = date;
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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

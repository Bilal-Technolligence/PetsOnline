package com.petsonline.models;

public class UserAttr {
    String Id;
    String Name;
    String Email;
    String Contact;
    String Imageurl;
    String City;
    String Language;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getImageUrl() {
        return Imageurl;
    }

    public void setImageUrl(String imageUrl) {
        Imageurl = imageUrl;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public UserAttr(String id, String name, String email, String contact, String imageUrl, String city, String language) {
        Id = id;
        Name = name;
        Email = email;
        Contact = contact;
        Imageurl = imageUrl;
        City = city;
        Language = language;
    }

    public UserAttr() {
    }
}

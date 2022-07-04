package com.petsonline.models;

import java.io.Serializable;

public class MyPet implements Serializable {
    String MyPet_ID;
    String MyPet_Img;
    String MyPet_Quantity;
    String MyPet_Age;
    String MyPet_Desc;
    String MyPet_Title;
    String Address;
    String SellerID;
    Boolean Selection;
    String HandoverTo;

    public MyPet() {
    }

    public MyPet(String myPet_ID, String myPet_Img, String myPet_Quantity, String myPet_Age, String myPet_Desc, String myPet_Title, String address, String sellerID) {
        MyPet_ID = myPet_ID;
        MyPet_Img = myPet_Img;
        MyPet_Quantity = myPet_Quantity;
        MyPet_Age = myPet_Age;
        MyPet_Desc = myPet_Desc;
        MyPet_Title = myPet_Title;
        Address = address;
        SellerID = sellerID;
    }

    public String getMyPet_ID() {
        return MyPet_ID;
    }

    public void setMyPet_ID(String myPet_ID) {
        MyPet_ID = myPet_ID;
    }

    public String getMyPet_Img() {
        return MyPet_Img;
    }

    public void setMyPet_Img(String myPet_Img) {
        MyPet_Img = myPet_Img;
    }

    public String getMyPet_Quantity() {
        return MyPet_Quantity;
    }

    public void setMyPet_Quantity(String myPet_Quantity) {
        MyPet_Quantity = myPet_Quantity;
    }

    public String getMyPet_Age() {
        return MyPet_Age;
    }

    public void setMyPet_Age(String myPet_Age) {
        MyPet_Age = myPet_Age;
    }

    public String getMyPet_Desc() {
        return MyPet_Desc;
    }

    public void setMyPet_Desc(String myPet_Desc) {
        MyPet_Desc = myPet_Desc;
    }

    public String getMyPet_Title() {
        return MyPet_Title;
    }

    public void setMyPet_Title(String myPet_Title) {
        MyPet_Title = myPet_Title;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public Boolean getSelection() {
        if (Selection!=null)
            return Selection;
        else return false;
    }

    public void setSelection(Boolean selection) {
        Selection = selection;
    }

    public String getHandoverTo() {
        return HandoverTo;
    }

    public void setHandoverTo(String handoverTo) {
        HandoverTo = handoverTo;
    }
}
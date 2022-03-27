package com.petsonline.models;

public class AdDetail {
    String Ad_ID;
    String Ad_Category_FID;
    String Ad_Img;
    String Ad_Price;
    String Ad_Quantity;
    String Ad_Sold;
    String Ad_Desc;
    String Ad_Title;
    String Ad_Address;
    String SellerID;
    String Date;

    public AdDetail(){}

    public AdDetail(String ad_ID, String ad_Category_FID, String ad_Img, String ad_Price, String ad_Quantity, String ad_Sold, String ad_Desc, String ad_Title, String ad_Address, String sellerID, String date) {
        Ad_ID = ad_ID;
        Ad_Category_FID = ad_Category_FID;
        Ad_Img = ad_Img;
        Ad_Price = ad_Price;
        Ad_Quantity = ad_Quantity;
        Ad_Sold = ad_Sold;
        Ad_Desc = ad_Desc;
        Ad_Title = ad_Title;
        Ad_Address = ad_Address;
        SellerID = sellerID;
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getAd_ID() {
        return Ad_ID;
    }

    public void setAd_ID(String ad_ID) {
        Ad_ID = ad_ID;
    }

    public String getAd_Category_FID() {
        return Ad_Category_FID;
    }

    public void setAd_Category_FID(String ad_Category_FID) {
        Ad_Category_FID = ad_Category_FID;
    }

    public String getAd_Img() {
        return Ad_Img;
    }

    public void setAd_Img(String ad_Img) {
        Ad_Img = ad_Img;
    }

    public String getAd_Price() {
        return Ad_Price;
    }

    public void setAd_Price(String ad_Price) {
        Ad_Price = ad_Price;
    }

    public String getAd_Quantity() {
        return Ad_Quantity;
    }

    public void setAd_Quantity(String ad_Quantity) {
        Ad_Quantity = ad_Quantity;
    }

    public String getAd_Sold() {
        return Ad_Sold;
    }

    public void setAd_Sold(String ad_Sold) {
        Ad_Sold = ad_Sold;
    }

    public String getAd_Desc() {
        return Ad_Desc;
    }

    public void setAd_Desc(String ad_Desc) {
        Ad_Desc = ad_Desc;
    }

    public String getAd_Title() {
        return Ad_Title;
    }

    public void setAd_Title(String ad_Title) {
        Ad_Title = ad_Title;
    }

    public String getAd_Address() {
        return Ad_Address;
    }

    public void setAd_Address(String ad_Address) {
        Ad_Address = ad_Address;
    }
}

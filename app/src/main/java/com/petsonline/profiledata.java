package com.petsonline;

class profiledata {

    String ID;
    private String NAME;
    private String MOBILE;
    private String ADDRESS;
    private String DOB;
    private String Age;
    private String VECHILENO;
    private String IMAGEURL;

    public profiledata() {
    }

    public profiledata(String ID, String NAME, String MOBILE, String ADDRESS, String DOB, String age, String VECHILENO, String IMAGEURL) {
        this.ID = ID;
        this.NAME = NAME;
        this.MOBILE = MOBILE;
        this.ADDRESS = ADDRESS;
        this.DOB = DOB;
        Age = age;
        this.VECHILENO = VECHILENO;
        this.IMAGEURL = IMAGEURL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getVECHILENO() {
        return VECHILENO;
    }

    public void setVECHILENO(String VECHILENO) {
        this.VECHILENO = VECHILENO;
    }

    public String getIMAGEURL() {
        return IMAGEURL;
    }

    public void setIMAGEURL(String IMAGEURL) {
        this.IMAGEURL = IMAGEURL;
    }
}


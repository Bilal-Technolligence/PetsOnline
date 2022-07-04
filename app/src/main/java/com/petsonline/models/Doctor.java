package com.petsonline.models;

import java.io.Serializable;

public class Doctor implements Serializable {
    public String ID;
    public String NAME;
    public String MOBILE;
    public String ADDRESS;
    public String DOB;
    public String Age;
    public String IMAGEURL;
    public String PROFILECOMPLETED;
    public String ROLE;
    public String EMAIL;
    private String EDUCATION;
    private String SPECIALIZATION;

    public Doctor(){}

    public Doctor(String ID, String NAME, String MOBILE, String ADDRESS, String DOB, String age, String IMAGEURL, String PROFILECOMPLETED, String ROLE, String EMAIL, String EDUCATION, String SPECIALIZATION) {
        this.ID = ID;
        this.NAME = NAME;
        this.MOBILE = MOBILE;
        this.ADDRESS = ADDRESS;
        this.DOB = DOB;
        Age = age;
        this.IMAGEURL = IMAGEURL;
        this.PROFILECOMPLETED = PROFILECOMPLETED;
        this.ROLE = ROLE;
        this.EMAIL = EMAIL;
        this.EDUCATION = EDUCATION;
        this.SPECIALIZATION = SPECIALIZATION;
    }

    public String getEDUCATION() {
        return EDUCATION;
    }

    public void setEDUCATION(String EDUCATION) {
        this.EDUCATION = EDUCATION;
    }

    public String getSPECIALIZATION() {
        return SPECIALIZATION;
    }

    public void setSPECIALIZATION(String SPECIALIZATION) {
        this.SPECIALIZATION = SPECIALIZATION;
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

    public String getIMAGEURL() {
        return IMAGEURL;
    }

    public void setIMAGEURL(String IMAGEURL) {
        this.IMAGEURL = IMAGEURL;
    }

    public String getPROFILECOMPLETED() {
        return PROFILECOMPLETED;
    }

    public void setPROFILECOMPLETED(String PROFILECOMPLETED) {
        this.PROFILECOMPLETED = PROFILECOMPLETED;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }
}

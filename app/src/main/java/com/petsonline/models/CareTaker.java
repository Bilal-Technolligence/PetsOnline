package com.petsonline.models;

import java.io.Serializable;

public class CareTaker implements Serializable {
    public String ID;
    public String NAME;
    public String MOBILE;
    public String ADDRESS;
    public String DOB;
    public String Age;
    public String IMAGEURL;
    public String PROFILECOMPLETED;
    public String ROLE;
    public String FEEPERDAY;
    public String AVAILABLESTATUS;
    public String STARTINGTIME;
    public String ENDINGTIME;
    public String FEEPERHOUR;
    public String EMAIL;

    public CareTaker(){}

    public CareTaker(String ID, String NAME, String MOBILE, String ADDRESS, String DOB, String age, String IMAGEURL, String PROFILECOMPLETED, String ROLE, String FEEPERDAY, String AVAILABLESTATUS, String STARTINGTIME, String ENDINGTIME, String FEEPERHOUR, String EMAIL) {
        this.ID = ID;
        this.NAME = NAME;
        this.MOBILE = MOBILE;
        this.ADDRESS = ADDRESS;
        this.DOB = DOB;
        Age = age;
        this.IMAGEURL = IMAGEURL;
        this.PROFILECOMPLETED = PROFILECOMPLETED;
        this.ROLE = ROLE;
        this.FEEPERDAY = FEEPERDAY;
        this.AVAILABLESTATUS = AVAILABLESTATUS;
        this.STARTINGTIME = STARTINGTIME;
        this.ENDINGTIME = ENDINGTIME;
        this.FEEPERHOUR = FEEPERHOUR;
        this.EMAIL = EMAIL;
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

    public String getFEEPERDAY() {
        return FEEPERDAY;
    }

    public void setFEEPERDAY(String FEEPERDAY) {
        this.FEEPERDAY = FEEPERDAY;
    }

    public String getAVAILABLESTATUS() {
        return AVAILABLESTATUS;
    }

    public void setAVAILABLESTATUS(String AVAILABLESTATUS) {
        this.AVAILABLESTATUS = AVAILABLESTATUS;
    }

    public String getSTARTINGTIME() {
        return STARTINGTIME;
    }

    public void setSTARTINGTIME(String STARTINGTIME) {
        this.STARTINGTIME = STARTINGTIME;
    }

    public String getENDINGTIME() {
        return ENDINGTIME;
    }

    public void setENDINGTIME(String ENDINGTIME) {
        this.ENDINGTIME = ENDINGTIME;
    }

    public String getFEEPERHOUR() {
        return FEEPERHOUR;
    }

    public void setFEEPERHOUR(String FEEPERHOUR) {
        this.FEEPERHOUR = FEEPERHOUR;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }
}

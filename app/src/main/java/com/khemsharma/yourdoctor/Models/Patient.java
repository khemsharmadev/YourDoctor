package com.khemsharma.yourdoctor.Models;

public class Patient {

    String patientId,name,email,profileURL,status,gender,age,mobNum,userType;

    public static String PATIENT="patient";


    public Patient(){}

    public Patient(String name, String email, String profileURL, String status, String age, String mobNum, String gender ) {
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
        this.status = status;
        this.age = age;
        this.mobNum = mobNum;
        this.gender = gender;
        this.userType = PATIENT;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobNum() {
        return mobNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}

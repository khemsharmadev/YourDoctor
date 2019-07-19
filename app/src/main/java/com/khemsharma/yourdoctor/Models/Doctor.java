package com.khemsharma.yourdoctor.Models;

public class Doctor  {

    String name,email,profileURL,status,gender,age,mobNum,userType,doctorID,accNum,bio,ifscCode,specialist,ap_status;
    public static String DOCTOR="doctor";
    public Doctor(){}


    public Doctor(String name, String email, String profileURL, String status, String age, String mobNum, String gender, String accNum, String bio, String ifscCode, String specialist, String ap_status) {
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
        this.status = status;
        this.gender = gender;
        this.age = age;
        this.mobNum = mobNum;
        this.userType = DOCTOR;
        this.accNum = accNum;
        this.bio = bio;
        this.ifscCode = ifscCode;
        this.specialist = specialist;
        this.ap_status = ap_status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getAp_status() {
        return ap_status;
    }

    public void setAp_status(String ap_status) {
        this.ap_status = ap_status;
    }
}

package com.roaaserver.placementadmin.Models;

public class StudentDetailsClass {
    private String email;
    private String studentID;
    private boolean isVerified;
    private String userName;
    private String image;

    public StudentDetailsClass(){}

    public StudentDetailsClass(String email,
                               String studentID, boolean isVerified, String userName,
                               String image) {
        this.email = email;

        this.studentID = studentID;
        this.isVerified = isVerified;
        this.userName = userName;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

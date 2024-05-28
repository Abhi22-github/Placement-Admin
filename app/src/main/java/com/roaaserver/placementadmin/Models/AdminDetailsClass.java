package com.roaaserver.placementadmin.Models;

public class AdminDetailsClass {
    private String email;
    private boolean admin;
    private String adminID;
    private boolean isVerified;
    private String userName;
    private String department;
    private String image;
    private String number;

    public AdminDetailsClass() {
    }

    public AdminDetailsClass(String email, boolean admin, String adminID,
                             boolean isVerified, String userName, String department, String image,
                             String number) {
        this.email = email;
        this.admin = admin;

        this.adminID = adminID;
        this.isVerified = isVerified;
        this.userName = userName;
        this.department = department;
        this.image = image;
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

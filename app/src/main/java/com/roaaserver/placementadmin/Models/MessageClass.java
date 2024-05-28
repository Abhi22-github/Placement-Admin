package com.roaaserver.placementadmin.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class MessageClass {
    String message,senderUid,name,image,department;
    public MessageClass(){}
    @ServerTimestamp
    Date time = null;

    public MessageClass(String message, Date time, String senderUid,String name,String image,String department) {
        this.message = message;
        this.time = time;
        this.senderUid = senderUid;
        this.name = name;
        this.image = image;
        this.department = department;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }
}

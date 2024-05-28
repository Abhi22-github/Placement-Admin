package com.roaaserver.placementadmin.Models;

import java.io.Serializable;

public class CompanyDetailsClass implements Serializable {
    private String documentID;
    private String companyImage;
    private String role;
    private String companyName;
    private String companyLocation;
    private String salary;
    private String courses;
    private String batch;
    private String branches;
    private String sscMarks;
    private String hscMarks;
    private String diplomaMarks;
    private String graduationMarks;
    private String backlog;
    private String experience;
    private String key1;
    private String key2;
    private String key3;
    private String key4;
    private String key5;
    private String campusType;
    private String totalRounds;
    private String startDate;
    private String applicationDeadline;
    private String website;
    private String industryType;
    private String applyLink;

    public CompanyDetailsClass() {

    }

    public CompanyDetailsClass(String documentID, String companyImage, String role, String companyName,
                               String companyLocation, String salary, String courses, String batch,
                               String branches, String sscMarks, String hscMarks, String diplomaMarks,
                               String graduationMarks, String backlog, String experience, String key1,
                               String key2, String key3, String key4, String key5, String campusType,
                               String totalRounds, String startDate, String applicationDeadline,
                               String website, String industryType,String applyLink) {
        this.documentID = documentID;
        this.companyImage = companyImage;
        this.role = role;
        this.companyName = companyName;
        this.companyLocation = companyLocation;
        this.salary = salary;
        this.courses = courses;
        this.batch = batch;
        this.branches = branches;
        this.sscMarks = sscMarks;
        this.hscMarks = hscMarks;
        this.diplomaMarks = diplomaMarks;
        this.graduationMarks = graduationMarks;
        this.backlog = backlog;
        this.experience = experience;
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
        this.key5 = key5;
        this.campusType = campusType;
        this.totalRounds = totalRounds;
        this.startDate = startDate;
        this.applicationDeadline = applicationDeadline;
        this.website = website;
        this.industryType = industryType;
        this.applyLink = applyLink;
    }

    public String getApplyLink() {
        return applyLink;
    }

    public void setApplyLink(String applyLink) {
        this.applyLink = applyLink;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBranches() {
        return branches;
    }

    public void setBranches(String branches) {
        this.branches = branches;
    }

    public String getSscMarks() {
        return sscMarks;
    }

    public void setSscMarks(String sscMarks) {
        this.sscMarks = sscMarks;
    }

    public String getHscMarks() {
        return hscMarks;
    }

    public void setHscMarks(String hscMarks) {
        this.hscMarks = hscMarks;
    }

    public String getDiplomaMarks() {
        return diplomaMarks;
    }

    public void setDiplomaMarks(String diplomaMarks) {
        this.diplomaMarks = diplomaMarks;
    }

    public String getGraduationMarks() {
        return graduationMarks;
    }

    public void setGraduationMarks(String graduationMarks) {
        this.graduationMarks = graduationMarks;
    }

    public String getBacklog() {
        return backlog;
    }

    public void setBacklog(String backlog) {
        this.backlog = backlog;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getKey4() {
        return key4;
    }

    public void setKey4(String key4) {
        this.key4 = key4;
    }

    public String getKey5() {
        return key5;
    }

    public void setKey5(String key5) {
        this.key5 = key5;
    }

    public String getCampusType() {
        return campusType;
    }

    public void setCampusType(String campusType) {
        this.campusType = campusType;
    }

    public String getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(String totalRounds) {
        this.totalRounds = totalRounds;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(String applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }
}

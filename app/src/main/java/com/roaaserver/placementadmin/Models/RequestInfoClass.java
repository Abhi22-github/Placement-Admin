package com.roaaserver.placementadmin.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class RequestInfoClass implements Serializable {
    private String image, name, erpNo, id, department;
    private float sem1, sem2, sem3, sem4, sem5, sem6, sem7, sem8, aggregate, aggregatePercentage;
    private int activeBacklog, previousBacklog, status, modifyCount;
    private String internshipCompanyName, internshipPosition, internshipDuration, certification;
    private boolean internshipPresent, certificatePresent;
    @ServerTimestamp
    Date time = null;


    public RequestInfoClass() {
    }

    public RequestInfoClass(String id, String image, String name, String erpNo, float sem1, float sem2, String department,
                            float sem3, float sem4, float sem5, float sem6, float sem7, float sem8,
                            float aggregate, float aggregatePercentage, int activeBacklog, int modifyCount,
                            int previousBacklog, int status, String internshipCompanyName,
                            String internshipPosition, String internshipDuration, String certification,
                            boolean internshipPresent, boolean certificatePresent, Date time) {
        this.id = id;
        this.image = image;
        this.modifyCount = modifyCount;
        this.name = name;
        this.department = department;
        this.erpNo = erpNo;
        this.sem1 = sem1;
        this.sem2 = sem2;
        this.sem3 = sem3;
        this.sem4 = sem4;
        this.sem5 = sem5;
        this.sem6 = sem6;
        this.sem7 = sem7;
        this.sem8 = sem8;
        this.aggregate = aggregate;
        this.aggregatePercentage = aggregatePercentage;
        this.activeBacklog = activeBacklog;
        this.previousBacklog = previousBacklog;
        this.status = status;
        this.internshipCompanyName = internshipCompanyName;
        this.internshipPosition = internshipPosition;
        this.internshipDuration = internshipDuration;
        this.certification = certification;
        this.internshipPresent = internshipPresent;
        this.certificatePresent = certificatePresent;
        this.time = time;
    }

    public String getDepartment() {
        return department;
    }

    public int getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(int modifyCount) {
        this.modifyCount = modifyCount;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getErpNo() {
        return erpNo;
    }

    public void setErpNo(String erpNo) {
        this.erpNo = erpNo;
    }

    public float getSem1() {
        return sem1;
    }

    public void setSem1(float sem1) {
        this.sem1 = sem1;
    }

    public float getSem2() {
        return sem2;
    }

    public void setSem2(float sem2) {
        this.sem2 = sem2;
    }

    public float getSem3() {
        return sem3;
    }

    public void setSem3(float sem3) {
        this.sem3 = sem3;
    }

    public float getSem4() {
        return sem4;
    }

    public void setSem4(float sem4) {
        this.sem4 = sem4;
    }

    public float getSem5() {
        return sem5;
    }

    public void setSem5(float sem5) {
        this.sem5 = sem5;
    }

    public float getSem6() {
        return sem6;
    }

    public void setSem6(float sem6) {
        this.sem6 = sem6;
    }

    public float getSem7() {
        return sem7;
    }

    public void setSem7(float sem7) {
        this.sem7 = sem7;
    }

    public float getSem8() {
        return sem8;
    }

    public void setSem8(float sem8) {
        this.sem8 = sem8;
    }

    public float getAggregate() {
        return aggregate;
    }

    public void setAggregate(float aggregate) {
        this.aggregate = aggregate;
    }

    public float getAggregatePercentage() {
        return aggregatePercentage;
    }

    public void setAggregatePercentage(float aggregatePercentage) {
        this.aggregatePercentage = aggregatePercentage;
    }

    public int getActiveBacklog() {
        return activeBacklog;
    }

    public void setActiveBacklog(int activeBacklog) {
        this.activeBacklog = activeBacklog;
    }

    public int getPreviousBacklog() {
        return previousBacklog;
    }

    public void setPreviousBacklog(int previousBacklog) {
        this.previousBacklog = previousBacklog;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInternshipCompanyName() {
        return internshipCompanyName;
    }

    public void setInternshipCompanyName(String internshipCompanyName) {
        this.internshipCompanyName = internshipCompanyName;
    }

    public String getInternshipPosition() {
        return internshipPosition;
    }

    public void setInternshipPosition(String internshipPosition) {
        this.internshipPosition = internshipPosition;
    }

    public String getInternshipDuration() {
        return internshipDuration;
    }

    public void setInternshipDuration(String internshipDuration) {
        this.internshipDuration = internshipDuration;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public boolean isInternshipPresent() {
        return internshipPresent;
    }

    public void setInternshipPresent(boolean internshipPresent) {
        this.internshipPresent = internshipPresent;
    }

    public boolean isCertificatePresent() {
        return certificatePresent;
    }

    public void setCertificatePresent(boolean certificatePresent) {
        this.certificatePresent = certificatePresent;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

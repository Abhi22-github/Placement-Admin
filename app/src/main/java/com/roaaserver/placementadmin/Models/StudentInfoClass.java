package com.roaaserver.placementadmin.Models;


import java.io.Serializable;

public class StudentInfoClass implements Serializable {
    private String image, name, contactNo, address,erpNo, prnNo, birthDate, gender, email,id;
    private float sscPercentage, hscPercentage, diplomaPercentage, sem1, sem2, sem3, sem4, sem5, sem6, sem7, sem8, aggregate,
            aggregatePercentage,offeredPackage;
    private int sscPassoutYear,hscPassoutYear,diplomaPassoutYear,graduationYear, activeBacklog, previousBacklog,status;
    private String sscCollege,hscCollege,diplomaCollege,branch,gapYears,division;
    private String internshipCompanyName, internshipPosition, internshipDuration, certification,placedCompanyName,placedCompanyLocation;
    private boolean profileCompleted, internshipPresent, certificatePresent,gapPresent,engineeringGapPresent,isPlaced,japaneseCertificationPresent
            ,isActive,isVerified;
    private String interviewDate,joiningDate,interestedFor,jlpt;
    private String resumeName, resumeLink,offerLetterName,offerLetterLink;

    public StudentInfoClass() {
    }


    public StudentInfoClass(String image, String name, String contactNo, String address, String erpNo,
                            String prnNo, String birthDate, String gender, String email, String id, float sscPercentage,
                            float hscPercentage, float diplomaPercentage, float sem1, float sem2, float sem3, float sem4,
                            float sem5, float sem6, float sem7, float sem8, float aggregate, float aggregatePercentage,
                            float offeredPackage, int sscPassoutYear, int hscPassoutYear, int diplomaPassoutYear, int graduationYear,
                            int activeBacklog, int previousBacklog, int status, String sscCollege, String hscCollege,
                            String diplomaCollege, String branch, String gapYears, String internshipCompanyName,
                            String internshipPosition, String internshipDuration, String certification, String placedCompanyName,
                            String placedCompanyLocation, boolean profileCompleted, boolean internshipPresent,
                            boolean certificatePresent, boolean gapPresent, boolean engineeringGapPresent, boolean isPlaced,
                            boolean japaneseCertificationPresent, String interviewDate, String joiningDate, String interestedFor,
                            String jlpt, String resumeName, String resumeLink, String offerLetterName, String offerLetterLink) {
        this.image = image;
        this.name = name;
        this.contactNo = contactNo;
        this.address = address;
        this.erpNo = erpNo;
        this.prnNo = prnNo;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.id = id;
        this.sscPercentage = sscPercentage;
        this.hscPercentage = hscPercentage;
        this.diplomaPercentage = diplomaPercentage;
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
        this.offeredPackage = offeredPackage;
        this.sscPassoutYear = sscPassoutYear;
        this.hscPassoutYear = hscPassoutYear;
        this.diplomaPassoutYear = diplomaPassoutYear;
        this.graduationYear = graduationYear;
        this.activeBacklog = activeBacklog;
        this.previousBacklog = previousBacklog;
        this.status = status;
        this.sscCollege = sscCollege;
        this.hscCollege = hscCollege;
        this.diplomaCollege = diplomaCollege;
        this.branch = branch;
        this.gapYears = gapYears;
        this.internshipCompanyName = internshipCompanyName;
        this.internshipPosition = internshipPosition;
        this.internshipDuration = internshipDuration;
        this.certification = certification;
        this.placedCompanyName = placedCompanyName;
        this.placedCompanyLocation = placedCompanyLocation;
        this.profileCompleted = profileCompleted;
        this.internshipPresent = internshipPresent;
        this.certificatePresent = certificatePresent;
        this.gapPresent = gapPresent;
        this.engineeringGapPresent = engineeringGapPresent;
        this.isPlaced = isPlaced;
        this.japaneseCertificationPresent = japaneseCertificationPresent;
        this.interviewDate = interviewDate;
        this.joiningDate = joiningDate;
        this.interestedFor = interestedFor;
        this.jlpt = jlpt;
        this.resumeName = resumeName;
        this.resumeLink = resumeLink;
        this.offerLetterName = offerLetterName;
        this.offerLetterLink = offerLetterLink;
    }

    public String getImage() {
        return image;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getErpNo() {
        return erpNo;
    }

    public void setErpNo(String erpNo) {
        this.erpNo = erpNo;
    }

    public String getPrnNo() {
        return prnNo;
    }

    public void setPrnNo(String prnNo) {
        this.prnNo = prnNo;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getSscPercentage() {
        return sscPercentage;
    }

    public void setSscPercentage(float sscPercentage) {
        this.sscPercentage = sscPercentage;
    }

    public float getHscPercentage() {
        return hscPercentage;
    }

    public void setHscPercentage(float hscPercentage) {
        this.hscPercentage = hscPercentage;
    }

    public float getDiplomaPercentage() {
        return diplomaPercentage;
    }

    public void setDiplomaPercentage(float diplomaPercentage) {
        this.diplomaPercentage = diplomaPercentage;
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

    public float getOfferedPackage() {
        return offeredPackage;
    }

    public void setOfferedPackage(float offeredPackage) {
        this.offeredPackage = offeredPackage;
    }

    public int getSscPassoutYear() {
        return sscPassoutYear;
    }

    public void setSscPassoutYear(int sscPassoutYear) {
        this.sscPassoutYear = sscPassoutYear;
    }

    public int getHscPassoutYear() {
        return hscPassoutYear;
    }

    public void setHscPassoutYear(int hscPassoutYear) {
        this.hscPassoutYear = hscPassoutYear;
    }

    public int getDiplomaPassoutYear() {
        return diplomaPassoutYear;
    }

    public void setDiplomaPassoutYear(int diplomaPassoutYear) {
        this.diplomaPassoutYear = diplomaPassoutYear;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
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

    public String getSscCollege() {
        return sscCollege;
    }

    public void setSscCollege(String sscCollege) {
        this.sscCollege = sscCollege;
    }

    public String getHscCollege() {
        return hscCollege;
    }

    public void setHscCollege(String hscCollege) {
        this.hscCollege = hscCollege;
    }

    public String getDiplomaCollege() {
        return diplomaCollege;
    }

    public void setDiplomaCollege(String diplomaCollege) {
        this.diplomaCollege = diplomaCollege;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGapYears() {
        return gapYears;
    }

    public void setGapYears(String gapYears) {
        this.gapYears = gapYears;
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

    public String getPlacedCompanyName() {
        return placedCompanyName;
    }

    public void setPlacedCompanyName(String placedCompanyName) {
        this.placedCompanyName = placedCompanyName;
    }

    public String getPlacedCompanyLocation() {
        return placedCompanyLocation;
    }

    public void setPlacedCompanyLocation(String placedCompanyLocation) {
        this.placedCompanyLocation = placedCompanyLocation;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
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

    public boolean isGapPresent() {
        return gapPresent;
    }

    public void setGapPresent(boolean gapPresent) {
        this.gapPresent = gapPresent;
    }

    public boolean isEngineeringGapPresent() {
        return engineeringGapPresent;
    }

    public void setEngineeringGapPresent(boolean engineeringGapPresent) {
        this.engineeringGapPresent = engineeringGapPresent;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public boolean isJapaneseCertificationPresent() {
        return japaneseCertificationPresent;
    }

    public void setJapaneseCertificationPresent(boolean japaneseCertificationPresent) {
        this.japaneseCertificationPresent = japaneseCertificationPresent;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getInterestedFor() {
        return interestedFor;
    }

    public void setInterestedFor(String interestedFor) {
        this.interestedFor = interestedFor;
    }

    public String getJlpt() {
        return jlpt;
    }

    public void setJlpt(String jlpt) {
        this.jlpt = jlpt;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getResumeLink() {
        return resumeLink;
    }

    public void setResumeLink(String resumeLink) {
        this.resumeLink = resumeLink;
    }

    public String getOfferLetterName() {
        return offerLetterName;
    }

    public void setOfferLetterName(String offerLetterName) {
        this.offerLetterName = offerLetterName;
    }

    public String getOfferLetterLink() {
        return offerLetterLink;
    }

    public void setOfferLetterLink(String offerLetterLink) {
        this.offerLetterLink = offerLetterLink;
    }
}

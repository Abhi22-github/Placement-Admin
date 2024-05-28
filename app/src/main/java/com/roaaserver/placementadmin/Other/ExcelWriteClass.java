package com.roaaserver.placementadmin.Other;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.roaaserver.placementadmin.Models.StudentInfoClass;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelWriteClass {
    private static final String TAG = "ExcelWriteClass";
    List<StudentInfoClass> studentList = new ArrayList<>();
    Context mContext;
    File filePath ;
    boolean resume, name,number, email, address, erp, prn, bod, gender, sscPercentage, sscCollege, sscYear, hscPercentage, hscCollege,
            hscYear, diplomaPercentage, diplomaCollege, diplomaYear, graduationYear, branch,division, be, bePercentage, activeBacklog,
            previousBacklog, sem1, sem2, sem3, sem4, sem5, sem6, sem7, sem8, hscGap, engineeringGap, engineeringGapYears, placed,
            placedCompanyName, placedCompanyLocation, placedCompanyPackage, interviewDate, joiningDate, offerLetter, japanese, jlpt,
            internship, certification;


    public ExcelWriteClass(List<StudentInfoClass> studentList, Context mContext,String fileName) {
        this.studentList = studentList;
        this.mContext = mContext;
        filePath= new File(Environment.getExternalStorageDirectory() + "/"+fileName);
    }

    public void writeData() {
        // Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Creating a blank Excel sheet
        XSSFSheet sheet
                = workbook.createSheet("student Details");

        // Creating an empty TreeMap of string and Object][]
        // type
        // Map<String, Object[]> data=new TreeMap<String, Object[]>();

        // Writing data to Object[]
        // using put() method
//      data.put("1",
//              new Object[] { "ID", "NAME", "LASTNAME" });
//      data.put("2",
//              new Object[] { 1, "Pankaj", "Kumar" });
//      data.put("3",
//              new Object[] { 2, "Prakashni", "Yadav" });
//      data.put("4", new Object[] { 3, "Ayan", "Mondal" });
//      data.put("5", new Object[] { 4, "Virat", "kohli" });

        // Iterating over data and writing it to sheet
        // Set<String> keyset = data.keySet();

        int rownum = 0;
        int cellnum = 0;
        {
            Row row0 = sheet.createRow(rownum++);
            row0.createCell(cellnum++).setCellValue("No.");

            if (resume)
                row0.createCell(cellnum++).setCellValue("Resume");

            if (name)
                row0.createCell(cellnum++).setCellValue("Name");
            if (number)
                row0.createCell(cellnum++).setCellValue("Contact Number");

            if (email)
                row0.createCell(cellnum++).setCellValue("Email");


            if (address)
                row0.createCell(cellnum++).setCellValue("Address");


            if (erp)
                row0.createCell(cellnum++).setCellValue("Erp No.");


            if (prn)
                row0.createCell(cellnum++).setCellValue("Prn No.");


            if (bod)
                row0.createCell(cellnum++).setCellValue("Date of Birth");


            if (gender)
                row0.createCell(cellnum++).setCellValue("Gender");


            if (sscPercentage)
                row0.createCell(cellnum++).setCellValue("SSC Percentage");


            if (sscCollege)
                row0.createCell(cellnum++).setCellValue("SSC College");


            if (sscYear)
                row0.createCell(cellnum++).setCellValue("SSC Passout Year");


            if (hscPercentage)
                row0.createCell(cellnum++).setCellValue("HSC Percentage");

            if (hscCollege)
                row0.createCell(cellnum++).setCellValue("HSC College");


            if (hscYear)
                row0.createCell(cellnum++).setCellValue("HSC Passout Year");


            if (diplomaPercentage)
                row0.createCell(cellnum++).setCellValue("Diploma Percentage");


            if (diplomaCollege)
                row0.createCell(cellnum++).setCellValue("Diploma College");


            if (diplomaYear)
                row0.createCell(cellnum++).setCellValue("Diploma Year");


            if (graduationYear)
                row0.createCell(cellnum++).setCellValue("Graduation Year");


            if (branch)
                row0.createCell(cellnum++).setCellValue("Branch");

            if (division)
                row0.createCell(cellnum++).setCellValue("Division");

            if (be)
                row0.createCell(cellnum++).setCellValue("BE Aggregate");


            if (bePercentage)
                row0.createCell(cellnum++).setCellValue("BE Aggregate Percentage");

            if (activeBacklog)
                row0.createCell(cellnum++).setCellValue("Active Backlog");

            if (previousBacklog)
                row0.createCell(cellnum++).setCellValue("Previous Backlog");

            if (sem1)
                row0.createCell(cellnum++).setCellValue("Sem 1");

            if (sem2)
                row0.createCell(cellnum++).setCellValue("Sem 2");

            if (sem3)
                row0.createCell(cellnum++).setCellValue("Sem 3");

            if (sem4)
                row0.createCell(cellnum++).setCellValue("Sem 4");

            if (sem5)
                row0.createCell(cellnum++).setCellValue("Sem 5");

            if (sem6)
                row0.createCell(cellnum++).setCellValue("Sem 6");

            if (sem7)
                row0.createCell(cellnum++).setCellValue("Sem 7");

            if (sem8)
                row0.createCell(cellnum++).setCellValue("Sem 8");

            if (hscGap)
                row0.createCell(cellnum++).setCellValue("HSC to Degree Gap");

            if (engineeringGap)
                row0.createCell(cellnum++).setCellValue("Engineering Gap");

            if (engineeringGapYears)
                row0.createCell(cellnum++).setCellValue("Engineering Gap in Year");

            if (placed)
                row0.createCell(cellnum++).setCellValue("Placed");

            if (placedCompanyName)
                row0.createCell(cellnum++).setCellValue("Placed Company name");

            if (placedCompanyLocation)
                row0.createCell(cellnum++).setCellValue("Placed Company Location");

            if (placedCompanyPackage)
                row0.createCell(cellnum++).setCellValue("Placed Company Package");

            if (interviewDate)
                row0.createCell(cellnum++).setCellValue("Interview Date");

            if (joiningDate)
                row0.createCell(cellnum++).setCellValue("Joining Date");

            if (offerLetter)
                row0.createCell(cellnum++).setCellValue("Offer Letter");

            if (japanese)
                row0.createCell(cellnum++).setCellValue("Japanese Certification");

            if (jlpt)
                row0.createCell(cellnum++).setCellValue("JLPT Level");

            if (internship)
                row0.createCell(cellnum++).setCellValue("Internship");

            if (certification)
                row0.createCell(cellnum++).setCellValue("Certifications");
        }

        for (int i = 0; i < studentList.size(); i++) {

            // Creating a new row in the sheet
            Row row = sheet.createRow(rownum++);
            cellnum = 0;
            //Object[] objArr = data.get(key);

            {

                row.createCell(cellnum++).setCellValue(i + 1);
                {

                if (resume)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getResumeLink());

                if (name)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getName());

                if (number)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getContactNo());


                if (email)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getEmail());


                if (address)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getAddress());


                if (erp)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getErpNo());


                if (prn)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getPrnNo());


                if (bod)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getBirthDate());


                if (gender)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getGender());


                if (sscPercentage)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSscPercentage());


                if (sscCollege)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSscCollege());


                if (sscYear)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSscPassoutYear());


                if (hscPercentage) {
                    if (studentList.get(i).getHscPercentage() == -1f) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getHscPercentage());
                    }

                }
                if (hscCollege) {
                    if (studentList.get(i).getHscCollege().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getHscCollege());
                    }
                }


                if (hscYear) {
                    if (studentList.get(i).getHscPassoutYear() == -1) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getHscPassoutYear());
                    }
                }


                if (diplomaPercentage) {
                    if (studentList.get(i).getDiplomaPercentage() == -1f) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getDiplomaPercentage());
                    }

                }
                if (diplomaCollege) {
                    if (studentList.get(i).getDiplomaCollege().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getDiplomaCollege());
                    }
                }


                if (diplomaYear) {
                    if (studentList.get(i).getDiplomaPassoutYear() == -1) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getDiplomaPassoutYear());
                    }
                }

                if (graduationYear)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getGraduationYear());


                if (branch)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getBranch());
                if (division)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getDivision());


                if (be)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getAggregate());


                if (bePercentage)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getAggregatePercentage());

                if (activeBacklog)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getActiveBacklog());


                if (previousBacklog)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getActiveBacklog());

                if (sem1)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSem1());

                if (sem2)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSem2());

                if (sem3)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSem3());

                if (sem4)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSem4());

                if (sem5)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSem5());

                if (sem6)
                    row.createCell(cellnum++).setCellValue(studentList.get(i).getSem6());

                if (sem7) {
                    if (studentList.get(i).getSem7() == -1f) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getSem7());
                    }
                }


                if (sem8) {
                    if (studentList.get(i).getSem8() == -1f) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getSem8());
                    }
                }
                if (hscGap) {
                    if (studentList.get(i).isGapPresent()) {
                        row.createCell(cellnum++).setCellValue("Yes");
                    } else {
                        row.createCell(cellnum++).setCellValue("No");
                    }
                }


                if (engineeringGap) {
                    if (studentList.get(i).isEngineeringGapPresent()) {
                        row.createCell(cellnum++).setCellValue("Yes");
                    } else {
                        row.createCell(cellnum++).setCellValue("No");
                    }
                }

                if (engineeringGapYears) {
                    if (studentList.get(i).getGapYears().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getGapYears());
                    }
                }

                if (placed) {
                    if (studentList.get(i).isPlaced()) {
                        row.createCell(cellnum++).setCellValue("Yes");
                    } else {
                        row.createCell(cellnum++).setCellValue("No");
                    }
                }

                if (placedCompanyName) {
                    if (studentList.get(i).getPlacedCompanyName().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getPlacedCompanyName());
                    }
                }

                if (placedCompanyLocation) {
                    if (studentList.get(i).getPlacedCompanyLocation().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getPlacedCompanyLocation());
                    }
                }

                if (placedCompanyPackage) {
                    if (studentList.get(i).getOfferedPackage() == -1f) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getOfferedPackage());
                    }
                }

                if (interviewDate) {
                    if (studentList.get(i).getInterviewDate().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getInterviewDate());
                    }
                }

                if (joiningDate) {
                    if (studentList.get(i).getJoiningDate().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getJoiningDate());
                    }
                }

                if (offerLetter) {
                    if (studentList.get(i).getOfferLetterLink().isEmpty()) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getOfferLetterLink());
                    }
                }

                if (japanese) {
                    if (studentList.get(i).isJapaneseCertificationPresent()) {
                        row.createCell(cellnum++).setCellValue("Yes");
                    } else {
                        row.createCell(cellnum++).setCellValue("No");
                    }
                }

                if (jlpt) {
                    if (studentList.get(i).getJlpt().equals("-1")) {
                        row.createCell(cellnum++).setCellValue("NA");
                    } else {
                        row.createCell(cellnum++).setCellValue(studentList.get(i).getJlpt());
                    }
                }


                if (internship) {
                    if (studentList.get(i).isInternshipPresent()) {
                        row.createCell(cellnum++).setCellValue("Yes");
                    } else {
                        row.createCell(cellnum++).setCellValue("No");
                    }
                }

                if (certification) {
                    if (studentList.get(i).isCertificatePresent()) {
                        row.createCell(cellnum++).setCellValue("Yes");
                    } else {
                        row.createCell(cellnum++).setCellValue("No");
                    }
                }
            }
            }


//         for(int j=0;j<10;j++){
//
//            // This line creates a cell in the next
//            // column of that row
//            Cell cell = row.createCell(cellnum++);
//
//            if (studentList.get(i) instanceof String)
//               cell.setCellValue((String)obj);
//
//            else if (obj instanceof Integer)
//               cell.setCellValue((Integer)obj);
//         }
        }

        // Try block to check for exceptions
        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            if (out != null) {
                out.flush();
                out.close();
            }
            Toast.makeText(mContext, "File Created Successfully.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "writeData: " + e);
        }
//        try {
//
//            // Writing the workbook
//            FileOutputStream out = new FileOutputStream(
//                    new File("testing.xlsx"));
//            workbook.write(out);
//
//            // Closing file output connections
//            out.close();
//            Toast.makeText(mContext, "File Created Successfully.", Toast.LENGTH_SHORT).show();
//        }
//
//        // Catch block to handle exceptions
//        catch (Exception e) {
//
//            // Display exceptions along with line number
//            // using printStackTrace() method
//            e.printStackTrace();
//            Log.d(TAG, "writeData: " + e);
//        }
    }


    public boolean isResume() {
        return resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isEmail() {
        return email;
    }

    public boolean isDivision() {
        return division;
    }

    public void setDivision(boolean division) {
        this.division = division;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isAddress() {
        return address;
    }

    public void setAddress(boolean address) {
        this.address = address;
    }

    public boolean isErp() {
        return erp;
    }

    public void setErp(boolean erp) {
        this.erp = erp;
    }

    public boolean isPrn() {
        return prn;
    }

    public void setPrn(boolean prn) {
        this.prn = prn;
    }

    public boolean isBod() {
        return bod;
    }

    public void setBod(boolean bod) {
        this.bod = bod;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isSscPercentage() {
        return sscPercentage;
    }

    public void setSscPercentage(boolean sscPercentage) {
        this.sscPercentage = sscPercentage;
    }

    public boolean isSscCollege() {
        return sscCollege;
    }

    public void setSscCollege(boolean sscCollege) {
        this.sscCollege = sscCollege;
    }

    public boolean isSscYear() {
        return sscYear;
    }

    public void setSscYear(boolean sscYear) {
        this.sscYear = sscYear;
    }

    public boolean isHscPercentage() {
        return hscPercentage;
    }

    public void setHscPercentage(boolean hscPercentage) {
        this.hscPercentage = hscPercentage;
    }

    public boolean isHscCollege() {
        return hscCollege;
    }

    public void setHscCollege(boolean hscCollege) {
        this.hscCollege = hscCollege;
    }

    public boolean isHscYear() {
        return hscYear;
    }

    public void setHscYear(boolean hscYear) {
        this.hscYear = hscYear;
    }

    public boolean isDiplomaPercentage() {
        return diplomaPercentage;
    }

    public void setDiplomaPercentage(boolean diplomaPercentage) {
        this.diplomaPercentage = diplomaPercentage;
    }

    public boolean isDiplomaCollege() {
        return diplomaCollege;
    }

    public void setDiplomaCollege(boolean diplomaCollege) {
        this.diplomaCollege = diplomaCollege;
    }

    public boolean isDiplomaYear() {
        return diplomaYear;
    }

    public void setDiplomaYear(boolean diplomaYear) {
        this.diplomaYear = diplomaYear;
    }

    public boolean isGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(boolean graduationYear) {
        this.graduationYear = graduationYear;
    }

    public boolean isBranch() {
        return branch;
    }

    public void setBranch(boolean branch) {
        this.branch = branch;
    }

    public boolean isBe() {
        return be;
    }

    public void setBe(boolean be) {
        this.be = be;
    }

    public boolean isBePercentage() {
        return bePercentage;
    }

    public void setBePercentage(boolean bePercentage) {
        this.bePercentage = bePercentage;
    }

    public boolean isActiveBacklog() {
        return activeBacklog;
    }

    public void setActiveBacklog(boolean activeBacklog) {
        this.activeBacklog = activeBacklog;
    }

    public boolean isPreviousBacklog() {
        return previousBacklog;
    }

    public void setPreviousBacklog(boolean previousBacklog) {
        this.previousBacklog = previousBacklog;
    }

    public boolean isSem1() {
        return sem1;
    }

    public void setSem1(boolean sem1) {
        this.sem1 = sem1;
    }

    public boolean isSem2() {
        return sem2;
    }

    public void setSem2(boolean sem2) {
        this.sem2 = sem2;
    }

    public boolean isSem3() {
        return sem3;
    }

    public void setSem3(boolean sem3) {
        this.sem3 = sem3;
    }

    public boolean isSem4() {
        return sem4;
    }

    public boolean isNumber() {
        return number;
    }

    public void setNumber(boolean number) {
        this.number = number;
    }

    public void setSem4(boolean sem4) {
        this.sem4 = sem4;
    }

    public boolean isSem5() {
        return sem5;
    }

    public void setSem5(boolean sem5) {
        this.sem5 = sem5;
    }

    public boolean isSem6() {
        return sem6;
    }

    public void setSem6(boolean sem6) {
        this.sem6 = sem6;
    }

    public boolean isSem7() {
        return sem7;
    }

    public void setSem7(boolean sem7) {
        this.sem7 = sem7;
    }

    public boolean isSem8() {
        return sem8;
    }

    public void setSem8(boolean sem8) {
        this.sem8 = sem8;
    }

    public boolean isHscGap() {
        return hscGap;
    }

    public void setHscGap(boolean hscGap) {
        this.hscGap = hscGap;
    }

    public boolean isEngineeringGap() {
        return engineeringGap;
    }

    public void setEngineeringGap(boolean engineeringGap) {
        engineeringGap = engineeringGap;
    }

    public boolean isEngineeringGapYears() {
        return engineeringGapYears;
    }

    public void setEngineeringGapYears(boolean engineeringGapYears) {
        this.engineeringGapYears = engineeringGapYears;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public boolean isPlacedCompanyName() {
        return placedCompanyName;
    }

    public void setPlacedCompanyName(boolean placedCompanyName) {
        this.placedCompanyName = placedCompanyName;
    }

    public boolean isPlacedCompanyLocation() {
        return placedCompanyLocation;
    }

    public void setPlacedCompanyLocation(boolean placedCompanyLocation) {
        this.placedCompanyLocation = placedCompanyLocation;
    }

    public boolean isPlacedCompanyPackage() {
        return placedCompanyPackage;
    }

    public void setPlacedCompanyPackage(boolean placedCompanyPackage) {
        placedCompanyPackage = placedCompanyPackage;
    }

    public boolean isInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(boolean interviewDate) {
        this.interviewDate = interviewDate;
    }

    public boolean isJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(boolean joiningDate) {
        this.joiningDate = joiningDate;
    }

    public boolean isOfferLetter() {
        return offerLetter;
    }

    public void setOfferLetter(boolean offerLetter) {
        this.offerLetter = offerLetter;
    }

    public boolean isJapanese() {
        return japanese;
    }

    public void setJapanese(boolean japanese) {
        this.japanese = japanese;
    }

    public boolean isJlpt() {
        return jlpt;
    }

    public void setJlpt(boolean jlpt) {
        this.jlpt = jlpt;
    }

    public boolean isInternship() {
        return internship;
    }

    public void setInternship(boolean internship) {
        this.internship = internship;
    }

    public boolean isCertification() {
        return certification;
    }

    public void setCertification(boolean certification) {
        this.certification = certification;
    }
}

package com.roaaserver.placementadmin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.roaaserver.placementadmin.Models.StudentInfoClass;
import com.roaaserver.placementadmin.Models.TokenClass;
import com.roaaserver.placementadmin.Nofifications.FcmNotificationsSender;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileActivity extends AppCompatActivity {
    private static final String TAG = "StudentProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private static boolean PLACED, ACTIVE;

    private CircleImageView profileImage;
    private ImageButton backButton;
    private ProgressBar imageProgress;
    private TextView name, email, contact, address, erp, prn, dob, gender, ssc, sscCollege, sscYear, hsc, hscCollege, hscYear, diploma,
            diplomaCollege, diplomaYear, year, branch, classField, division,
            aggregate, aggregatePercentage, activeBacklog, prevBacklog, sem1, sem2, sem3, sem4, sem5, sem6, sem7, sem8, gap, engineeringGap, gapYears,
            isPlaced, placedCompanyName, companyLocation, packageOffered, interviewDate, joiningDate,
            internshipAns, certificateAns, company, position, duration, certificate, japanese, jlpt;
    private LinearLayout internshipLayout, certificateLayout, hscLayout, diplomaLayout, gapLayout, placedLayout, japaneseLayout;
    private ProgressBar progressBar;
    private MaterialButton suspend, freeze, active, correctionButton, verifiedButton;
    private TextView resumeName, offerLetterName;
    private ExtendedFloatingActionButton actionButton;
    private ImageButton resumeViewButton, offerLetterViewButton;
    private int from, objectPosition;
    private LinearLayout verificationLayout;

    private StudentInfoClass studentInfoClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        initFields();
        initFirebase();
        getDataFromIntent();
        backButton.setOnClickListener(view -> onBackPressed());
        suspend.setOnClickListener(view -> askAdminForSuspendConfirmation());
        freeze.setOnClickListener(view -> askAdminForFreezeConfirmation());
        active.setOnClickListener(view -> askAdminForActiveConfirmation());
        resumeViewButton.setOnClickListener(v -> sendUserToPDFViewerActivity(studentInfoClass.getResumeLink(), studentInfoClass.getResumeName()));
        offerLetterViewButton.setOnClickListener(v -> sendUserToPDFViewerActivity(studentInfoClass.getOfferLetterLink(), studentInfoClass.getOfferLetterName()));

        if (from == 1) {
            actionButton.setVisibility(View.VISIBLE);
        } else {
            actionButton.setVisibility(View.GONE);
        }

        if (from == 2) {
            verificationLayout.setVisibility(View.VISIBLE);
        } else {
            verificationLayout.setVisibility(View.GONE);
        }

        verifiedButton.setOnClickListener(v -> MakeStudentVerified());
        correctionButton.setOnClickListener(v -> sendForCorrections());

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Mark as Placed and Active", "Mark as Unplaced and Active", "Mark as placed and freeze",
                        "Mark as Unplaced and freeze"};

                int status = 0;
                if (PLACED && ACTIVE)
                    status = 0;
                if (!PLACED && ACTIVE)
                    status = 1;
                if (PLACED && !ACTIVE)
                    status = 2;
                if (!PLACED && !ACTIVE)
                    status = 3;

                new MaterialAlertDialogBuilder(StudentProfileActivity.this)
                        .setTitle("Confirm Action")
                        .setSingleChoiceItems(items, status, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                //   Toast.makeText(StudentProfileActivity.this, "i"+which, Toast.LENGTH_SHORT).show();
                                new MaterialAlertDialogBuilder(StudentProfileActivity.this)
                                        .setTitle("Are you sure?")
                                        .setMessage("Do you want to perform this action")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int w) {
                                                switch (which) {
                                                    case 0:
                                                        setPlacedAndActiveStatus(true, true);
                                                        break;
                                                    case 1:
                                                        setPlacedAndActiveStatus(false, true);
                                                        break;
                                                    case 2:
                                                        setPlacedAndActiveStatus(true, false);
                                                        break;
                                                    case 3:
                                                        setPlacedAndActiveStatus(false, false);
                                                        break;
                                                }
                                            }
                                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //  suspendStudent();
                                //Toast.makeText(StudentProfileActivity.this, "i"+i, Toast.LENGTH_SHORT).show();


                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });
    }

    private void setPlacedAndActiveStatus(boolean b, boolean b1) {
        WriteBatch writeBatch = firestore.batch();
        DocumentReference documentReference = firestore.collection("Students Information").document(studentInfoClass.getId());
        writeBatch.update(documentReference, "placed", b);
        writeBatch.update(documentReference, "active", b1);
        studentInfoClass.setPlaced(b);
        studentInfoClass.setActive(b1);
        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    onBackPressed();
                    String p ="";
                    String a ="";
                    if(b){
                        p = "placed";
                    }else{
                        p ="unplaced";
                    }
                    if(b1){
                        a = "active";
                    }else{
                        a ="Freeze";
                    }
                    sendNotificationToStudents("Status change","Dear student you profile has been marked as "+p+" and "+a+" by Administrator.");
                } else {
                    Log.d(TAG, "onComplete: " + task.getException());
                    Toast.makeText(StudentProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initFields() {
        resumeName = findViewById(R.id.resume_name);
        resumeViewButton = findViewById(R.id.resume_view_button);
        backButton = findViewById(R.id.back_button);
        profileImage = findViewById(R.id.profile_image_view);
        imageProgress = findViewById(R.id.profile_image_progress_bar);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        erp = findViewById(R.id.erp);
        prn = findViewById(R.id.prn);
        dob = findViewById(R.id.dob);
        gender = findViewById(R.id.gender);
        ssc = findViewById(R.id.ssc);
        sscCollege = findViewById(R.id.ssc_college);
        sscYear = findViewById(R.id.ssc_year);
        hsc = findViewById(R.id.hsc);
        hscCollege = findViewById(R.id.hsc_college);
        hscYear = findViewById(R.id.hsc_year);
        hscLayout = findViewById(R.id.hsc_layout);
        diploma = findViewById(R.id.diploma);
        diplomaCollege = findViewById(R.id.diploma_college);
        diplomaYear = findViewById(R.id.diploma_year);
        diplomaLayout = findViewById(R.id.diploma_layout);
        year = findViewById(R.id.year);
        branch = findViewById(R.id.branch);
        classField = findViewById(R.id.class_field);
        division = findViewById(R.id.division);
        gap = findViewById(R.id.gap);
        engineeringGap = findViewById(R.id.engineering_gap);
        gapYears = findViewById(R.id.gap_years);
        gapLayout = findViewById(R.id.engineering_gap_years_layout);
        aggregate = findViewById(R.id.aggregate);
        aggregatePercentage = findViewById(R.id.aggregate_percentage);
        activeBacklog = findViewById(R.id.active_backlogs);
        prevBacklog = findViewById(R.id.prev_backlogs);
        sem1 = findViewById(R.id.sem1);
        sem2 = findViewById(R.id.sem2);
        sem3 = findViewById(R.id.sem3);
        sem4 = findViewById(R.id.sem4);
        sem5 = findViewById(R.id.sem5);
        sem6 = findViewById(R.id.sem6);
        sem7 = findViewById(R.id.sem7);
        sem8 = findViewById(R.id.sem8);
        isPlaced = findViewById(R.id.is_placed);
        placedLayout = findViewById(R.id.placed_company_layout);
        placedCompanyName = findViewById(R.id.placed_company_name);
        companyLocation = findViewById(R.id.company_location);
        packageOffered = findViewById(R.id.offered_package);
        interviewDate = findViewById(R.id.interview_date);
        joiningDate = findViewById(R.id.joining_date);
        internshipAns = findViewById(R.id.internship_ans);
        offerLetterName = findViewById(R.id.offer_letter_name);
        offerLetterViewButton = findViewById(R.id.offer_letter_view_button);
        internshipLayout = findViewById(R.id.internship_details_layout);
        company = findViewById(R.id.company_name);
        position = findViewById(R.id.position);
        duration = findViewById(R.id.duration);
        certificateAns = findViewById(R.id.certificated_ans);
        certificateLayout = findViewById(R.id.certificate_layout);
        certificate = findViewById(R.id.certificate);
        japanese = findViewById(R.id.japanese_ans);
        japaneseLayout = findViewById(R.id.japanese_layout);
        jlpt = findViewById(R.id.jlpt);
        progressBar = findViewById(R.id.progress_bar);
        suspend = findViewById(R.id.suspend);
        freeze = findViewById(R.id.freeze);
        active = findViewById(R.id.active);
        actionButton = findViewById(R.id.action_button);
        verificationLayout = findViewById(R.id.verification_module);
        correctionButton = findViewById(R.id.corrections);
        verifiedButton = findViewById(R.id.verified);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void getDataFromIntent() {
        try {
            studentInfoClass = (StudentInfoClass) getIntent().getSerializableExtra("studentDetails");
            from = getIntent().getIntExtra("from", 0);
            objectPosition = getIntent().getIntExtra("position", 0);
            PLACED = studentInfoClass.isPlaced();
            ACTIVE = studentInfoClass.isActive();
            setUpFields();
        } catch (Exception e) {
            Log.d(TAG, "getDataFromIntent: some problem while getting data from intent");
            Toast.makeText(StudentProfileActivity.this, "Something went wrong" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendUserToPDFViewerActivity(String Link, String name) {
        Intent intent = new Intent(this, pdfViewerActivity.class);
        intent.putExtra("pdfUrl", Link);
        intent.putExtra("pdfName", name);
        startActivity(intent);
    }

    private void setUpFields() {
        imageProgress.setVisibility(View.VISIBLE);
        if (!studentInfoClass.getImage().isEmpty()) {
            Picasso.get().load(studentInfoClass.getImage()).into(profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    imageProgress.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    imageProgress.setVisibility(View.GONE);
                }
            });
        }
        resumeName.setText(studentInfoClass.getResumeName());
        name.setText(studentInfoClass.getName());
        email.setText(studentInfoClass.getEmail());
        contact.setText(studentInfoClass.getContactNo());
        address.setText(studentInfoClass.getAddress());
        erp.setText(studentInfoClass.getErpNo());
        prn.setText(studentInfoClass.getPrnNo());
        dob.setText(studentInfoClass.getBirthDate());
        gender.setText(studentInfoClass.getGender());

        ssc.setText(String.valueOf(studentInfoClass.getSscPercentage()) + "%");
        sscCollege.setText(studentInfoClass.getSscCollege());
        sscYear.setText(String.valueOf(studentInfoClass.getSscPassoutYear()));

        if (studentInfoClass.getHscPercentage() == -1) {
            hsc.setText("Not Applicable");
            hscLayout.setVisibility(View.GONE);
        } else {
            hsc.setText(String.valueOf(studentInfoClass.getHscPercentage()) + "%");
            hscLayout.setVisibility(View.VISIBLE);
        }
        hscCollege.setText(studentInfoClass.getHscCollege());
        hscYear.setText(String.valueOf(studentInfoClass.getHscPassoutYear()));
        if (studentInfoClass.getDiplomaPercentage() == -1) {
            diploma.setText("Not Applicable");
            diplomaLayout.setVisibility(View.GONE);
        } else {
            diploma.setText(String.valueOf(studentInfoClass.getDiplomaPercentage()) + "%");
            diplomaLayout.setVisibility(View.VISIBLE);
        }
        diplomaCollege.setText(studentInfoClass.getDiplomaCollege());
        diplomaYear.setText(String.valueOf(studentInfoClass.getDiplomaPassoutYear()));
        year.setText(String.valueOf(studentInfoClass.getGraduationYear()));
        branch.setText(studentInfoClass.getBranch());
        //classField.setText(studentInfoClass.getClassDetail());
         division.setText(studentInfoClass.getDivision());

        aggregate.setText(String.valueOf(studentInfoClass.getAggregate()));
        aggregatePercentage.setText(String.valueOf(studentInfoClass.getAggregatePercentage()));
        if (studentInfoClass.getActiveBacklog() == 0) {
            activeBacklog.setText("No Backlog");
        } else {
            activeBacklog.setText(String.valueOf(studentInfoClass.getActiveBacklog()));
        }
        if (studentInfoClass.getPreviousBacklog() == 0) {
            prevBacklog.setText("No Backlog");
        } else {
            prevBacklog.setText(String.valueOf(studentInfoClass.getPreviousBacklog()));
        }

        sem1.setText(String.valueOf(studentInfoClass.getSem1()));
        sem2.setText(String.valueOf(studentInfoClass.getSem2()));
        sem3.setText(String.valueOf(studentInfoClass.getSem3()));
        sem4.setText(String.valueOf(studentInfoClass.getSem4()));
        sem5.setText(String.valueOf(studentInfoClass.getSem5()));
        sem6.setText(String.valueOf(studentInfoClass.getSem6()));
        if (studentInfoClass.getSem7() == -1) {
            sem7.setText("Not applicable");
        } else {
            sem7.setText(String.valueOf(studentInfoClass.getSem7()));
        }
        if (studentInfoClass.getSem8() == -1) {
            sem8.setText("Not applicable");
        } else {
            sem8.setText(String.valueOf(studentInfoClass.getSem8()));
        }

        if (studentInfoClass.isGapPresent()) {
            gap.setText("Yes");
        } else {
            gap.setText("No");
        }
        if (studentInfoClass.isEngineeringGapPresent()) {
            engineeringGap.setText("Yes");
            gapLayout.setVisibility(View.VISIBLE);
        } else {
            engineeringGap.setText("No");
            gapLayout.setVisibility(View.GONE);
        }

        gapYears.setText(studentInfoClass.getGapYears());

        if (studentInfoClass.isPlaced()) {
            isPlaced.setText("Yes");
            placedLayout.setVisibility(View.VISIBLE);
        } else {
            isPlaced.setText("No");
            placedLayout.setVisibility(View.GONE);
        }

        placedCompanyName.setText(studentInfoClass.getPlacedCompanyName());
        companyLocation.setText(studentInfoClass.getPlacedCompanyLocation());
        packageOffered.setText(studentInfoClass.getOfferedPackage() + " LPA");
        interviewDate.setText(studentInfoClass.getInterviewDate());
        joiningDate.setText(studentInfoClass.getJoiningDate());
        offerLetterName.setText(studentInfoClass.getOfferLetterName());


        if (studentInfoClass.isInternshipPresent()) {
            internshipAns.setText("Yes");
            internshipLayout.setVisibility(View.VISIBLE);

        } else {
            internshipAns.setText("No");
            internshipLayout.setVisibility(View.GONE);
        }
        company.setText(studentInfoClass.getInternshipCompanyName());
        position.setText(studentInfoClass.getInternshipPosition());
        duration.setText(studentInfoClass.getInternshipDuration());
        if (studentInfoClass.isCertificatePresent()) {
            certificateAns.setText("Yes");
            certificateLayout.setVisibility(View.VISIBLE);

        } else {
            certificateAns.setText("No");
            certificateLayout.setVisibility(View.GONE);
        }
        certificate.setText(studentInfoClass.getCertification());
        if (studentInfoClass.isJapaneseCertificationPresent()) {
            japanese.setText("Yes");
            japaneseLayout.setVisibility(View.VISIBLE);
        } else {
            japanese.setText("No");
            japaneseLayout.setVisibility(View.GONE);
        }
        jlpt.setText(studentInfoClass.getJlpt());

    }

    private void askAdminForSuspendConfirmation() {
//        new MaterialAlertDialogBuilder(StudentProfileActivity.this)
//                .setTitle("Confirm Suspend")
//                .setMessage("Are you sure that you want to suspend Mr. " + studentInfoClass.getName())
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        suspendStudent();
//
//                    }
//                })
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .show();


    }

    private void MakeStudentVerified() {
        WriteBatch writeBatch = firestore.batch();
        DocumentReference userInfoRef = firestore.collection("Students Information").document(studentInfoClass.getId());
        DocumentReference userRef = firestore.collection("Students").document(studentInfoClass.getId());
        writeBatch.update(userInfoRef, "verified", true);
        writeBatch.update(userInfoRef, "active", true);
        writeBatch.update(userRef, "verified", true);
        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    sendNotificationToStudents("Account Verified & Activated", "Dear student your account has been verified and activated.");
                } else {
                    Log.d(TAG, "onComplete: " + task.getException());
                    Toast.makeText(StudentProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendForCorrections() {
        firestore.collection("Students Information").document(studentInfoClass.getId())
                .update("profileCompleted", false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            //    sendNotificationToStudents("Correction needed", "Dear student your profile needs some Corrections.");
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                            Toast.makeText(StudentProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void askAdminForFreezeConfirmation() {
        new MaterialAlertDialogBuilder(StudentProfileActivity.this)
                .setTitle("Confirm Freeze")
                .setMessage("Are you sure that you want to Freeze Mr. " + studentInfoClass.getName())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        freezeStudent();

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    private void askAdminForActiveConfirmation() {
        new MaterialAlertDialogBuilder(StudentProfileActivity.this)
                .setTitle("Confirm Activation")
                .setMessage("Are you sure that you want to Activate Mr. " + studentInfoClass.getName())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        activateStudent();

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    private void suspendStudent() {
        firestore.collection("Students Information")
                .document(studentInfoClass.getId())
                .update("status", -1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(StudentProfileActivity.this, "Account suspended", Toast.LENGTH_SHORT).show();
                            suspend.setEnabled(false);
                            active.setEnabled(true);
                            freeze.setEnabled(true);
                            sendNotificationToStudents("Account Suspended", "Dear student your account has been Suspended.");
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });

    }

    private void freezeStudent() {
        firestore.collection("Students Information")
                .document(studentInfoClass.getId())
                .update("status", 1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(StudentProfileActivity.this, "Account has been freeze", Toast.LENGTH_SHORT).show();
                            suspend.setEnabled(true);
                            active.setEnabled(true);
                            freeze.setEnabled(false);
                            sendNotificationToStudents("Account Freezed", "Dear student your account has been Freeze.");
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });

    }

    private void activateStudent() {
        firestore.collection("Students Information")
                .document(studentInfoClass.getId())
                .update("status", 0)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(StudentProfileActivity.this, "Account Activated", Toast.LENGTH_SHORT).show();
                            suspend.setEnabled(true);
                            active.setEnabled(false);
                            freeze.setEnabled(true);
                            sendNotificationToStudents("Account Activated", "Dear student your account has been activated.");
                            onBackPressed();
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });

    }

    //not overrided method
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", objectPosition);
        resultIntent.putExtra("studentDetails", studentInfoClass);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void sendNotificationToStudents(String title, String body) {

        firestore.collection("Tokens").document(studentInfoClass.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: " + studentInfoClass.getId());
                    DocumentSnapshot documentSnapshot = task.getResult();
                    TokenClass tokenClass = documentSnapshot.toObject(TokenClass.class);
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(tokenClass.getToken(),
                            title, body, getApplicationContext(), StudentProfileActivity.this);
                    notificationsSender.SendNotifications();
                    Log.d(TAG, "onComplete: " + tokenClass.getToken());
                } else {

                    Log.d(TAG, "onComplete: " + task.getException());
                }
            }
        });

    }

}
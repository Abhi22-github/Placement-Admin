package com.roaaserver.placementadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CompanyDetails extends AppCompatActivity {
    private static final String TAG = "CompanyDetails";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private CompanyDetailsClass companyDetailsClass;

    private ImageButton backButton;
    private ImageView companyLogo;
    private TextView role, companyName, salary, location, courses, batch, branches, sscMarks, hscMarks, diplomaMarks,
            graduationMarks, backlog, experience, campusType, totalRounds, startDate, applicationDeadline,
            website, industryType, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5,otherMsg;
    private LinearLayout eligibleLayout, editLayout, deleteLayout;
    private ProgressBar progressBar;
    private LinearLayout field1, field2, field3, field4, field5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        initFields();

        initFirebase();

        eligibleLayout.setOnClickListener(view -> sendUserToEligibleStudentsActivity());
        editLayout.setOnClickListener(view -> sendUserToEditDetailsLayout());
        deleteLayout.setOnClickListener(view -> askUserForConfirmation());

        getDataFromIntent();


        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void askUserForConfirmation() {

        new MaterialAlertDialogBuilder(CompanyDetails.this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure about this.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteCompanyEntry();

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    private void deleteCompanyEntry() {
        firestore.collection("Company Details").document(companyDetailsClass.getDocumentID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onBackPressed();
                } else {
                    Log.d(TAG, "onComplete: error" + task.getException());
                }
            }
        });
    }

    private void sendUserToEditDetailsLayout() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CompanyDetails.this, EditCompanyDetails.class);
                intent.putExtra("companyDetails", companyDetailsClass);
                startActivity(intent);
            }
        });
        thread.start();
    }

    private void sendUserToEligibleStudentsActivity() {
        Intent intent = new Intent(CompanyDetails.this, EligibleStudentsList.class);
        //intent.putExtra("companyDetails", companyList.get(position));
        startActivity(intent);
    }

    private void getDataFromIntent() {
        try {
            companyDetailsClass = (CompanyDetailsClass) getIntent().getSerializableExtra("companyDetails");
            setUpFields();
        } catch (Exception e) {
            Log.d(TAG, "getDataFromIntent: some problem while getting data from intent");
            Toast.makeText(CompanyDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpFields() {
        progressBar.setVisibility(View.VISIBLE);
        if (companyDetailsClass.getCompanyImage() == null) {
            progressBar.setVisibility(View.GONE);
        }
        Picasso.get().load(companyDetailsClass.getCompanyImage()).into(companyLogo, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
        role.setText(companyDetailsClass.getRole());
        companyName.setText(companyDetailsClass.getCompanyName());
        salary.setText("₹" + companyDetailsClass.getSalary() + " LPA");
        location.setText(companyDetailsClass.getCompanyLocation());
        courses.setText(companyDetailsClass.getCourses());
        batch.setText(companyDetailsClass.getBatch());
        branches.setText(companyDetailsClass.getBranches());
        sscMarks.setText(companyDetailsClass.getSscMarks());
        hscMarks.setText(companyDetailsClass.getHscMarks());
        diplomaMarks.setText(companyDetailsClass.getDiplomaMarks());
        graduationMarks.setText(companyDetailsClass.getGraduationMarks());
        backlog.setText(companyDetailsClass.getBacklog());
        experience.setText(companyDetailsClass.getExperience());

        boolean available = false;
        if (companyDetailsClass.getKey1() != null) {
            field1.setVisibility(View.VISIBLE);
            String[] str = companyDetailsClass.getKey1().split("@", 2);
            key1.setText(str[0]);
            value1.setText(str[1]);
            available = true;
        }
        if (companyDetailsClass.getKey2() != null) {
            field2.setVisibility(View.VISIBLE);
            String[] str = companyDetailsClass.getKey2().split("@", 2);
            key2.setText(str[0]);
            value2.setText(str[1]);
            available = true;
        }
        if (companyDetailsClass.getKey3() != null) {
            field3.setVisibility(View.VISIBLE);
            String[] str = companyDetailsClass.getKey3().split("@", 2);
            key3.setText(str[0]);
            value3.setText(str[1]);
            available = true;
        }
        if (companyDetailsClass.getKey4() != null) {
            field4.setVisibility(View.VISIBLE);
            String[] str = companyDetailsClass.getKey4().split("@", 2);
            key4.setText(str[0]);
            value4.setText(str[1]);
            available = true;
        }
        if (companyDetailsClass.getKey5() != null) {
            field5.setVisibility(View.VISIBLE);
            String[] str = companyDetailsClass.getKey5().split("@", 2);
            key5.setText(str[0]);
            value5.setText(str[1]);
            available = true;
        }
        if(!available){
           otherMsg.setVisibility(View.VISIBLE);
        }else {
            otherMsg.setVisibility(View.GONE);
        }
        campusType.setText(companyDetailsClass.getCampusType());
        totalRounds.setText(companyDetailsClass.getTotalRounds());
        startDate.setText(companyDetailsClass.getStartDate());
        applicationDeadline.setText(companyDetailsClass.getApplicationDeadline());
        website.setText(companyDetailsClass.getWebsite());
        industryType.setText(companyDetailsClass.getIndustryType());
    }

    private void initFields() {
        backButton = findViewById(R.id.back_button);

        eligibleLayout = findViewById(R.id.eligible);
        editLayout = findViewById(R.id.edit);
        deleteLayout = findViewById(R.id.delete);

        companyLogo = findViewById(R.id.company_imageview);
        progressBar = findViewById(R.id.company_logo_progress_bar);

        role = findViewById(R.id.role);
        companyName = findViewById(R.id.company_name);
        salary = findViewById(R.id.salary);
        location = findViewById(R.id.location);
        courses = findViewById(R.id.courses);
        batch = findViewById(R.id.batch);
        branches = findViewById(R.id.branch);
        sscMarks = findViewById(R.id.ssc_marks);
        hscMarks = findViewById(R.id.hsc_marks);
        diplomaMarks = findViewById(R.id.diploma_marks);
        graduationMarks = findViewById(R.id.graduation_marks);
        backlog = findViewById(R.id.backlog);
        experience = findViewById(R.id.experence);
        campusType = findViewById(R.id.campus_type);
        totalRounds = findViewById(R.id.total_rounds);
        startDate = findViewById(R.id.start_date);
        applicationDeadline = findViewById(R.id.application_deadline);
        website = findViewById(R.id.website);
        industryType = findViewById(R.id.industry_type);

        otherMsg = findViewById(R.id.other_msg);

        field1 = findViewById(R.id.field1);
        field2 = findViewById(R.id.field2);
        field3 = findViewById(R.id.field3);
        field4 = findViewById(R.id.field4);
        field5 = findViewById(R.id.field5);
        key1 = findViewById(R.id.key1);
        value1 = findViewById(R.id.value1);

        key2 = findViewById(R.id.key2);
        value2 = findViewById(R.id.value2);

        key3 = findViewById(R.id.key3);
        value3 = findViewById(R.id.value3);

        key4 = findViewById(R.id.key4);
        value4 = findViewById(R.id.value4);

        key5 = findViewById(R.id.key5);
        value5 = findViewById(R.id.value5);
    }
}
package com.roaaserver.placementadmin;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.roaaserver.placementadmin.Models.RequestInfoClass;
import com.roaaserver.placementadmin.Models.StudentInfoClass;
import com.roaaserver.placementadmin.Models.TokenClass;
import com.roaaserver.placementadmin.Nofifications.FcmNotificationsSender;

public class RequestDetailActivity extends AppCompatActivity {
    private static final String TAG = "RequestDetailActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private ImageButton backButton;
    private RequestInfoClass requestInfoClass;
    private TextView requesterName, requestTime, requestID, requestStatus;

    private LinearLayout sem1Layout, sem2Layout, sem3Layout, sem4Layout, sem5Layout, sem6Layout, sem7Layout, sem8Layout, cgpaLayout, percentageLayout,
            activeBacklogLayout, previousBacklogLayout;
    private TextView sem1Old, sem2Old, sem3Old, sem4Old, sem5Old, sem6Old, sem7Old, sem8Old, cgpaOld, percentageOld, activeBacklogOld, previousBacklogOld;
    private TextView sem1New, sem2New, sem3New, sem4New, sem5New, sem6New, sem7New, sem8New, cgpaNew, percentageNew, activeBacklogNew, previousBacklogNew;
    private boolean sem1Flag, sem2Flag, sem3Flag, sem4Flag, sem5Flag, sem6Flag, sem7Flag, sem8Flag, cgpaFlag, percentageFlag, activeBacklogFlag, previousBacklogFlag;

    private LinearLayout buttonLayout;
    private MaterialButton rejectButton, approvedButton;
    //private ProgressBar buttonProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        initFields();
        initFirebase();
        getDataFromIntent();
        backButton.setOnClickListener(v -> onBackPressed());
        rejectButton.setOnClickListener(v -> rejectRequestFromFirebase());
        approvedButton.setOnClickListener(v -> approveRequestFromFirebase());
    }

    private void rejectRequestFromFirebase() {
        //showProgressDialog();
        firestore.collection("Requests").document(requestInfoClass.getId())
                .update("status", 0)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            sendNotificationToStudents("Request is rejected", "Dear student your request for data modification" +
                                    " has been rejected.");
                            onBackPressed();
                            //  hideProgressDialog();
                        } else {
                            Log.e(TAG, "onComplete: ", task.getException());
                        }
                    }
                });
    }

    private void approveRequestFromFirebase() {
        WriteBatch writeBatch = firestore.batch();
        DocumentReference ref = firestore.collection("Students Information").document(requestInfoClass.getId());
        DocumentReference reqRef = firestore.collection("Requests").document(requestInfoClass.getId());
        if(sem1Flag){
            writeBatch.update(ref,"sem1",requestInfoClass.getSem1());
        }
        if(sem2Flag){
            writeBatch.update(ref,"sem2",requestInfoClass.getSem2());
        }
        if(sem3Flag){
            writeBatch.update(ref,"sem3",requestInfoClass.getSem3());
        }
        if(sem4Flag){
            writeBatch.update(ref,"sem4",requestInfoClass.getSem4());
        }
        if(sem5Flag){
            writeBatch.update(ref,"sem5",requestInfoClass.getSem5());
        }
        if(sem6Flag){
            writeBatch.update(ref,"sem6",requestInfoClass.getSem6());
        }
        if(sem7Flag){
            writeBatch.update(ref,"sem7",requestInfoClass.getSem7());
        }
        if(sem8Flag){
            writeBatch.update(ref,"sem8",requestInfoClass.getSem8());
        }
        if(cgpaFlag){
            writeBatch.update(ref,"aggregate",requestInfoClass.getAggregate());
        }
        if(percentageFlag){
            writeBatch.update(ref,"aggregatePercentage",requestInfoClass.getAggregatePercentage());
        }
        if(activeBacklogFlag){
            writeBatch.update(ref,"activeBacklog",requestInfoClass.getActiveBacklog());
        }
        if(previousBacklogFlag){
            writeBatch.update(ref,"previousBacklog",requestInfoClass.getPreviousBacklog());
        }
        writeBatch.update(reqRef,"status",2);


        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    onBackPressed();
                    sendNotificationToStudents("Request is approved","Your request for data modification has been approved. " +
                            "Your data will be updated within 10 min.");
                }else{
                    Toast.makeText(RequestDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onComplete: ",task.getException() );
                }
            }
        });
    }

    private void sendNotificationToStudents(String title, String body) {

        firestore.collection("Tokens").document(requestInfoClass.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //Log.d(TAG, "onComplete: "+studentInfoClass.getId());
                    DocumentSnapshot documentSnapshot = task.getResult();
                    TokenClass tokenClass = documentSnapshot.toObject(TokenClass.class);
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(tokenClass.getToken(),
                            title, body, getApplicationContext(), RequestDetailActivity.this);
                    notificationsSender.SendNotifications();
                    Log.d(TAG, "onComplete: " + tokenClass.getToken());
                } else {

                    Log.d(TAG, "onComplete: " + task.getException());
                }
            }
        });

    }

    private void getDataFromIntent() {
        try {
            requestInfoClass = (RequestInfoClass) getIntent().getSerializableExtra("requestDetails");
            setUpFields();
        } catch (Exception e) {
            Log.e(TAG, "getDataFromIntent: some problem while getting data from intent" + e.toString());
            Toast.makeText(RequestDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void setUpFields() {
        requesterName.setText(requestInfoClass.getName());
        requestTime.setText(requestInfoClass.getTime().toString());

        requestID.setText(requestInfoClass.getId());
        if (requestInfoClass.getStatus() == 2) {
            requestStatus.setText("Completed");
            buttonLayout.setVisibility(View.GONE);
        } else if (requestInfoClass.getStatus() == 1) {
            requestStatus.setText("Pending");
            buttonLayout.setVisibility(View.VISIBLE);
        } else if (requestInfoClass.getStatus() == 0) {
            requestStatus.setText("Rejected");
            buttonLayout.setVisibility(View.GONE);
        } else {
            requestStatus.setText("Cancelled");
            buttonLayout.setVisibility(View.GONE);
        }

        firestore.collection("Students Information").document(requestInfoClass.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    StudentInfoClass studentInfoClass = task.getResult().toObject(StudentInfoClass.class);
                    setupContentFields(requestInfoClass, studentInfoClass);

                } else {
                    Log.e(TAG, "onComplete: " + task.getException());
                }
            }
        });

    }

    private void setupContentFields(RequestInfoClass a, StudentInfoClass b) {
        if (a.getSem1() != 0) {
            sem1Layout.setVisibility(View.VISIBLE);
            sem1Old.setText(String.valueOf(b.getSem1()));
            sem1Old.setPaintFlags(sem1Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem1New.setText(String.valueOf(a.getSem1()) + " SGPA");
            sem1Flag = true;
        }
        if (a.getSem2() != 0) {
            sem2Layout.setVisibility(View.VISIBLE);
            sem2Old.setText(String.valueOf(b.getSem2()));
            sem2Old.setPaintFlags(sem2Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem2New.setText(String.valueOf(a.getSem2()) + " SGPA");
            sem2Flag = true;
        }
        if (a.getSem3() != 0) {
            sem3Layout.setVisibility(View.VISIBLE);
            sem3Old.setText(String.valueOf(b.getSem3()));
            sem3Old.setPaintFlags(sem3Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem3New.setText(String.valueOf(a.getSem3()) + " SGPA");
            sem3Flag = true;
        }
        if (a.getSem4() != 0) {
            sem4Layout.setVisibility(View.VISIBLE);
            sem4Old.setText(String.valueOf(b.getSem4()));
            sem4Old.setPaintFlags(sem4Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem4New.setText(String.valueOf(a.getSem4()) + " SGPA");
            sem4Flag = true;
        }
        if (a.getSem5() != 0) {
            sem5Layout.setVisibility(View.VISIBLE);
            sem5Old.setText(String.valueOf(b.getSem5()));
            sem5Old.setPaintFlags(sem5Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem5New.setText(String.valueOf(a.getSem5()) + " SGPA");
            sem5Flag = true;
        }
        if (a.getSem6() != 0) {
            sem6Layout.setVisibility(View.VISIBLE);
            sem6Old.setText(String.valueOf(b.getSem6()));
            sem6Old.setPaintFlags(sem6Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem6New.setText(String.valueOf(a.getSem6()) + " SGPA");
            sem6Flag = true;
        }
        if (a.getSem7() != 0) {
            sem7Layout.setVisibility(View.VISIBLE);
            sem7Old.setText(String.valueOf(b.getSem7()));
            sem7Old.setPaintFlags(sem7Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem7New.setText(String.valueOf(a.getSem7()) + " SGPA");
            sem7Flag = true;
        }
        if (a.getSem8() != 0) {
            sem8Layout.setVisibility(View.VISIBLE);
            sem8Old.setText(String.valueOf(b.getSem8()));
            sem8Old.setPaintFlags(sem8Old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            sem8New.setText(String.valueOf(a.getSem8()) + " SGPA");
            sem8Flag = true;
        }
        if (a.getAggregate() != 0) {
            cgpaLayout.setVisibility(View.VISIBLE);
            cgpaOld.setText(String.valueOf(b.getAggregate()));
            cgpaOld.setPaintFlags(cgpaOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            cgpaNew.setText(String.valueOf(a.getAggregate()) + " CGPA");
            cgpaFlag = true;
        }
        if (a.getAggregatePercentage() != 0) {
            percentageLayout.setVisibility(View.VISIBLE);
            percentageOld.setText(String.valueOf(b.getAggregatePercentage()));
            percentageOld.setPaintFlags(percentageOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            percentageNew.setText(String.valueOf(a.getAggregatePercentage()) + "%");
            percentageFlag = true;
        }
        if (a.getActiveBacklog() != 0) {
            activeBacklogLayout.setVisibility(View.VISIBLE);
            activeBacklogOld.setText(String.valueOf(b.getActiveBacklog()));
            activeBacklogOld.setPaintFlags(activeBacklogOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            activeBacklogNew.setText(String.valueOf(a.getActiveBacklog()));
            activeBacklogFlag = true;
        }
        if (a.getPreviousBacklog() != 0) {
            previousBacklogLayout.setVisibility(View.VISIBLE);
            previousBacklogOld.setText(String.valueOf(b.getPreviousBacklog()));
            previousBacklogOld.setPaintFlags(previousBacklogOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            previousBacklogNew.setText(String.valueOf(a.getPreviousBacklog()));
            previousBacklogFlag = true;
        }


    }

    private void initFields() {
        backButton = findViewById(R.id.back_button);
        requesterName = findViewById(R.id.requesters_name);
        requestTime = findViewById(R.id.request_time);
        requestID = findViewById(R.id.request_id);
        requestStatus = findViewById(R.id.request_status);

        sem1Layout = findViewById(R.id.sem1_layout);
        sem2Layout = findViewById(R.id.sem2_layout);
        sem3Layout = findViewById(R.id.sem3_layout);
        sem4Layout = findViewById(R.id.sem4_layout);
        sem5Layout = findViewById(R.id.sem5_layout);
        sem6Layout = findViewById(R.id.sem6_layout);
        sem7Layout = findViewById(R.id.sem7_layout);
        sem8Layout = findViewById(R.id.sem8_layout);
        cgpaLayout = findViewById(R.id.aggregate_layout);
        percentageLayout = findViewById(R.id.aggregate_percentage_layout);
        activeBacklogLayout = findViewById(R.id.active_backlogs_layout);
        previousBacklogLayout = findViewById(R.id.previous_backlogs_layout);

        sem1Old = findViewById(R.id.sem1_old);
        sem2Old = findViewById(R.id.sem2_old);
        sem3Old = findViewById(R.id.sem3_old);
        sem4Old = findViewById(R.id.sem4_old);
        sem5Old = findViewById(R.id.sem5_old);
        sem6Old = findViewById(R.id.sem6_old);
        sem7Old = findViewById(R.id.sem7_old);
        sem8Old = findViewById(R.id.sem8_old);
        cgpaOld = findViewById(R.id.aggregate_old);
        percentageOld = findViewById(R.id.aggregate_percentage_old);
        activeBacklogOld = findViewById(R.id.active_backlogs_old);
        previousBacklogOld = findViewById(R.id.previous_backlogs_old);

        sem1New = findViewById(R.id.sem1_new);
        sem2New = findViewById(R.id.sem2_new);
        sem3New = findViewById(R.id.sem3_new);
        sem4New = findViewById(R.id.sem4_new);
        sem5New = findViewById(R.id.sem5_new);
        sem6New = findViewById(R.id.sem6_new);
        sem7New = findViewById(R.id.sem7_new);
        sem8New = findViewById(R.id.sem8_new);
        cgpaNew = findViewById(R.id.aggregate_new);
        percentageNew = findViewById(R.id.aggregate_percentage_new);
        activeBacklogNew = findViewById(R.id.active_backlogs_new);
        previousBacklogNew = findViewById(R.id.previous_backlogs_new);

        buttonLayout = findViewById(R.id.button_layout);
        rejectButton = findViewById(R.id.reject_button);
        approvedButton = findViewById(R.id.approve_button);
        // buttonProgressBar = findViewById(R.id.progress_bar);
    }

//    private void showProgressDialog() {
//        buttonProgressBar.setVisibility(View.VISIBLE);
//        cancelButton.setText("");
//        cancelButton.setEnabled(false);
//    }
//
//    private void hideProgressDialog() {
//        buttonProgressBar.setVisibility(View.GONE);
//        cancelButton.setText("Add Request");
//        cancelButton.setEnabled(true);
//    }
}
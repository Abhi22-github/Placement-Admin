package com.roaaserver.placementadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;

public class PendingVerificationPage extends AppCompatActivity {
    private static final String TAG = "PendingVerificationPage";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private ImageButton backButton;
    private MaterialButton checkAgainButton;
    private ProgressBar progressBar;

    private Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_verification_page);

        initFields();
        initFirebase();
        backButton.setOnClickListener(view -> onBackPressed());
        checkAgainButton.setOnClickListener(view -> checkIfUserIsVerified());
    }

    private void initFields() {
        backButton = findViewById(R.id.back_button);
        checkAgainButton = findViewById(R.id.check_again_button);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            PendingVerificationPage.this.finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void checkIfUserIsVerified() {
        showProgressDialog();
        Log.d(TAG, "checkIfUserIsVerified: " + mAuth.getCurrentUser().getUid());
        firestore.collection("Admins").document(mAuth.getCurrentUser().getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        AdminDetailsClass adminDetailsClass = documentSnapshot.toObject(AdminDetailsClass.class);
                        if (adminDetailsClass.isVerified()) {
                            sendUserToMainActivity();
                        } else {

                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();
                            }
                        }, 2000);


                    }
                });

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void sendUserToMainActivity() {

        FirebaseMessaging.getInstance().subscribeToTopic("Admins")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent Intent = new Intent(PendingVerificationPage.this, MainActivity.class);
                            Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Intent);
                            finish();
                        }else{
                            Toast.makeText(PendingVerificationPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        checkAgainButton.setText("");
        checkAgainButton.setEnabled(false);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
        checkAgainButton.setText("Check again");
        checkAgainButton.setEnabled(true);
    }
}
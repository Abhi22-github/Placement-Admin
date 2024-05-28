package com.roaaserver.placementadmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        initFirebase();

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isOnline()) {
            //databaseClass.savingUserTokenInDatabase();
            checkUser();
        } else {
            // NoInternetConnectionSnackbar();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    //    private void NoInternetConnectionSnackbar() {
//        Snackbar snackbar = Snackbar.make(constraintLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
//                .setActionTextColor(getResources().getColor(R.color.green))
//                .setAction("Refresh", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (isOnline()) {
//                            checkUser();
//                        } else {
//                            NoInternetConnectionSnackbar();
//                        }
//                    }
//                });
//        snackbar.show();
//    }
//
    public void checkUser() {
        if (isOnline()) {
            Log.d(TAG, "onStart: ");

            //showLoadingProgressbar();
            if (mAuth.getCurrentUser() == null) {
                Log.d(TAG, "onStart: user is null");
                //if not present then create account
                sendUserToAdminLogin();

            } else {
                Log.d(TAG, "onStart: checking if profile is completed");
                //else check if profile progress is completed or not
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        verifyUserExistence();
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();
            }
        } else {

        }
    }

    private void verifyUserExistence() {
        String currentUserID = mAuth.getCurrentUser().getUid();
        DocumentReference userStoreRef = firestore.collection("Admins").document(currentUserID);
        userStoreRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "verifyUserExistence: --------------");
                        checkIfUserIsVerified();
                    } else {
                        // progressBar.setVisibility(View.GONE);
                        //mainContentLayout.setVisibility(View.VISIBLE);
                        sendUserToAdminLogin();
                    }
                } else {
                    // progressBar.setVisibility(View.GONE);
                    // mainContentLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(SplashScreen.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });


    }

    private void checkIfUserIsVerified() {

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
                            sendUserToPendingVerificationPage();
                        }


                    }
                });

    }

//    private void sendUserToUserNameActivity() {
//        Intent intent = new Intent(SplashScreen.this, UsernameActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }

    private void sendUserToMainActivity() {
        Intent Intent = new Intent(SplashScreen.this, MainActivity.class);
        Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent);
        finish();
    }

    private void sendUserToAdminLogin() {
        Intent Intent = new Intent(SplashScreen.this, AdminLogin.class);
        Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent);
        finish();
    }

    private void sendUserToPendingVerificationPage() {
        Intent intent = new Intent(SplashScreen.this, PendingVerificationPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
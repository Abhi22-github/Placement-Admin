package com.roaaserver.placementadmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminLogin extends AppCompatActivity {
    private static final String TAG = "AdminLogin";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private ImageButton backButton;
    private MaterialButton loginButton;
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText email, password;
    private ProgressBar progressBar;
    private TextView register, forgetPassword;

    public static Activity adminLoginActivity;
    private Boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        adminLoginActivity = this;
        initFields();

        initFirebase();
        backButton.setOnClickListener(view -> onBackPressed());
        loginButton.setOnClickListener(view -> checkCredentials());
        register.setOnClickListener(view -> sendUserToRegisterActivity());
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        forgetPassword.setOnClickListener(v -> sendUserToForgetPasswordActivity());
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            AdminLogin.this.finish();
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

    private void checkCredentials() {

        String emailString, passwordString;
        emailString = email.getText().toString();
        passwordString = password.getText().toString();
        String regex = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailString);
        boolean allCorrect = true;
        if (emailString.isEmpty() && passwordString.isEmpty()) {
            emailLayout.setError("Please enter email.");
            passwordLayout.setError("Please enter Password.");
            emailLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            passwordLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            allCorrect = false;
        }
        if (emailString.isEmpty()) {
            emailLayout.setError("Please enter email.");
            emailLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            allCorrect = false;
        } else {
            if (!matcher.matches()) {
                emailLayout.setError("Please enter valid email.");
                emailLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
                allCorrect = false;
            }
        }
        if (passwordString.isEmpty()) {
            passwordLayout.setError("Please enter Password.");
            passwordLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            allCorrect = false;
        } else {
            if (passwordString.length() < 6) {
                passwordLayout.setError("Password should have minimum 6 characters.");
                passwordLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
                allCorrect = false;
            }
        }


        if (allCorrect) {
            chekAdminLogin(emailString, passwordString);
        }


    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setUpFieldsToNormalAfterError(s);
        }
    };

    private void setUpFieldsToNormalAfterError(Editable s) {
        if (s == email.getEditableText()) {
            emailLayout.setError("");
            emailLayout.setErrorEnabled(false);
            emailLayout.setBoxBackgroundColor(getColor(R.color.white));
        }
        if (s == password.getEditableText()) {
            passwordLayout.setError("");
            passwordLayout.setErrorEnabled(false);
            passwordLayout.setBoxBackgroundColor(getColor(R.color.white));
        }
    }

    private void chekAdminLogin(String emailString, String passwordString) {
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String currentUserID = mAuth.getCurrentUser().getUid();
                    DocumentReference userStoreRef = firestore.collection("Admins").document(currentUserID);
                    userStoreRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                try {
                                    if (documentSnapshot.getBoolean("admin")) {
                                        checkIfUserIsVerified();

                                    }
                                } catch (Exception e) {

                                }
                            } else {
                                // progressBar.setVisibility(View.GONE);
                                // mainContentLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(AdminLogin.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Failed with: ", task.getException());
                            }
                        }
                    });


                } else {
                    Log.d(TAG, "onComplete: " + task.getException());
                    Toast.makeText(AdminLogin.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
                }

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        }, 2000);


    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(AdminLogin.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToForgetPasswordActivity() {
        Intent intent = new Intent(AdminLogin.this, ForgetPasswordActivity.class);
        startActivity(intent);
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

    private void sendUserToPendingVerificationPage() {
        Intent intent = new Intent(AdminLogin.this, PendingVerificationPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent intent = new Intent(AdminLogin.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void initFields() {
        backButton = findViewById(R.id.back_button);
        loginButton = findViewById(R.id.login_button);
        emailLayout = findViewById(R.id.email_layout);
        email = findViewById(R.id.email);

        passwordLayout = findViewById(R.id.password_layout);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register_button);
        forgetPassword = findViewById(R.id.forgot_password_button);
        progressBar = findViewById(R.id.progress_bar);

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setText("");
        loginButton.setEnabled(false);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
        loginButton.setText("Log In");
        loginButton.setEnabled(true);
    }
}
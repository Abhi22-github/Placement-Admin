package com.roaaserver.placementadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgetPasswordActivity";

    private TextInputLayout emailLayout;
    private TextInputEditText email;
    private MaterialButton sendButton;
    private ProgressBar progressBar;
    private TextView successMessage;
    private ImageButton backButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initFields();
        initFirebase();
        sendButton.setOnClickListener(v -> checkEmail());
        email.addTextChangedListener(textWatcher);
        backButton.setOnClickListener(v->onBackPressed());
    }

    private void checkEmail() {
        String emailString = email.getText().toString();

        String regex = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailString);
        boolean allCorrect = true;

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
        if (allCorrect) {
            sendResetInstruction(emailString);
        }
        hideProgressDialog();
    }

    private void sendResetInstruction(String emailString) {
        mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    successMessage.setVisibility(View.VISIBLE);
                    successMessage.setText("The instruction to reset password has been sent to your email.");
                    successMessage.setTextColor(getColor(R.color.green));
                    //sendButton.setEnabled(false);
                } else {
                    successMessage.setVisibility(View.VISIBLE);
                    successMessage.setText(task.getException().getMessage());
                    successMessage.setTextColor(getColor(R.color.red));
                }
            }
        });
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
            emailLayout.setError("");
            emailLayout.setErrorEnabled(false);
            emailLayout.setBoxBackgroundColor(getColor(R.color.white));
        }
    };

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initFields() {
        emailLayout = findViewById(R.id.email_layout);
        email = findViewById(R.id.email);
        sendButton = findViewById(R.id.send_button);
        progressBar = findViewById(R.id.progress_bar);
        successMessage = findViewById(R.id.success_message);
        backButton = findViewById(R.id.back_button);
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        sendButton.setText("");
        sendButton.setEnabled(false);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
        sendButton.setText("Send Instructions");
        sendButton.setEnabled(true);
    }
}
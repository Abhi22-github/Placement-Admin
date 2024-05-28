package com.roaaserver.placementadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.roaaserver.placementadmin.Nofifications.FcmNotificationsSender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.shahabazimi.instagrampicker.InstagramPicker;

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private StorageReference userImageRef;

    private ImageButton backButton;
    private TextInputLayout nameLayout, emailLayout, passwordLayout, departmentLayout;
    private TextInputEditText email, password, name;
    private AutoCompleteTextView department;
    private MaterialButton registerButton;
    private ProgressBar progressBar;
    private RelativeLayout addImageLayout;
    private ImageView addImageView;
    private CircleImageView addCircleImageView;
    private TextView imageErrorMessage, loginButton;

    private Uri imageUri;
    private String downloadUri = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initFields();
        initFirebase();
        branchAdapter();
        backButton.setOnClickListener(view -> onBackPressed());
        registerButton.setOnClickListener(view -> checkDetails());
        name.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        addImageLayout.setOnClickListener(view -> selectImageFromDevice());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, AdminLogin.class);
                startActivity(intent);
            }
        });

        department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                departmentLayout.setError("");
                departmentLayout.setErrorEnabled(false);
                departmentLayout.setBoxBackgroundColor(getColor(R.color.white));
                //Toast.makeText(CreateAccountActivity.this, "item selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userImageRef = FirebaseStorage.getInstance().getReference().child("User Images");

    }

    private void branchAdapter() {
        String[] branchArray = getResources().getStringArray(R.array.department);
        ArrayAdapter branchArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, branchArray);
        department.setAdapter(branchArrayAdapter);
    }

    private void selectImageFromDevice() {
        InstagramPicker instagramPicker = new InstagramPicker(CreateAccountActivity.this);
        instagramPicker.show(1, 1, address -> {
            imageUri = Uri.parse(address);
            Log.d(TAG, "onActivityResult: " + imageUri);
            addCircleImageView.setImageURI(imageUri);
            addImageView.setVisibility(View.GONE);
            imageErrorMessage.setVisibility(View.GONE);
        });
    }

    private void checkDetails() {

        String emailString, passwordString, nameString;
        emailString = email.getText().toString();
        passwordString = password.getText().toString();
        nameString = name.getText().toString();

        String regex = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailString);
        boolean checkAll = true;
        if (emailString.isEmpty() && passwordString.isEmpty() && nameString.isEmpty() && imageUri == null) {
            emailLayout.setError("Please enter email.");
            passwordLayout.setError("Please enter Password.");
            nameLayout.setError("Please enter name");
            imageErrorMessage.setVisibility(View.VISIBLE);
            emailLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            passwordLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            nameLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            checkAll = false;
        }
        if (nameString.isEmpty()) {
            nameLayout.setError("Please enter name.");
            nameLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            checkAll = false;
        }

        if (department.getText().toString().isEmpty()) {
            departmentLayout.setError("Please select department.");
            departmentLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            checkAll = false;
        }

        if (emailString.isEmpty()) {
            emailLayout.setError("Please enter email.");
            emailLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            checkAll = false;
        } else {
            if (!matcher.matches()) {
                emailLayout.setError("Please enter valid email.");
                emailLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
                checkAll = false;
            }
        }
        if (passwordString.isEmpty()) {
            passwordLayout.setError("Please enter Password.");
            passwordLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
            checkAll = false;
        } else {
            if (passwordString.length() < 6) {
                passwordLayout.setError("Password should have minimum 6 characters.");
                passwordLayout.setBoxBackgroundColor(getColor(R.color.red_faint));
                checkAll = false;
            }
        }
        if (imageUri == null) {
            checkAll = false;
            imageErrorMessage.setVisibility(View.VISIBLE);
        }
        if (checkAll) {
            createUserAccount();
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
        if (s == name.getEditableText()) {
            nameLayout.setError("");
            nameLayout.setErrorEnabled(false);
            nameLayout.setBoxBackgroundColor(getColor(R.color.white));
        }
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

    private void createUserAccount() {
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            uploadProfileImageToFirebaseStorage(task);
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException());
                            Toast.makeText(CreateAccountActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }
                    }
                });
    }

    private void uploadProfileImageToFirebaseStorage(Task<AuthResult> task) {
        showProgressDialog();
        Log.d(TAG, "UploadProfileImageToFirebaseStorage: before bitmap");
        String docID = task.getResult().getUser().getUid();
        if (imageUri != null) {
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //here you can choose quality factor in third parameter(ex. i choosen 25)
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] fileInBytes = baos.toByteArray();
            Log.d(TAG, "UploadProfileImageToFirebaseStorage: after bitmap");
            StorageReference fileRef = userImageRef.child(docID + ".jpg");
            fileRef.putBytes(fileInBytes)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: profile image saved in storage");

                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri img = uri;
                                        downloadUri = img.toString();
                                        Log.d(TAG, "onSuccess: " + downloadUri);
                                        saveUserDetailsInFirestore(downloadUri, docID);
                                    }
                                });
                            } else {
                                Log.d(TAG, "onComplete: failed to store profile image in storage " + task.getException());
                                hideProgressDialog();
                            }
                        }
                    });
        } else {
            saveUserDetailsInFirestore(null, docID);
        }
    }

    private void saveUserDetailsInFirestore(String downloadUri, String docID) {
        AdminDetailsClass adminDetailsClass = new AdminDetailsClass();
        adminDetailsClass.setEmail(email.getText().toString());
        adminDetailsClass.setAdmin(false);
        adminDetailsClass.setAdminID(docID);
        adminDetailsClass.setVerified(false);
        adminDetailsClass.setDepartment(department.getText().toString());
        adminDetailsClass.setImage(downloadUri);
        adminDetailsClass.setUserName(name.getText().toString());
        firestore.collection("Admins").document(docID).
                set(adminDetailsClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                if (task1.isSuccessful()) {
                    // sendUserToUserNameActivity();
                    if (adminDetailsClass.isVerified()) {
                        sendUserToMainActivity();
                    } else {
                        sendUserToPendingVerificationPage();
                    }
                } else {
                    Log.d(TAG, "onComplete: " + task1.getException());
                    hideProgressDialog();
                }

            }
        });
    }

//    private void sendUserToUserNameActivity() {
//        Intent intent = new Intent(CreateAccountActivity.this, UsernameActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        AdminLogin.adminLoginActivity.finish();
//        finish();
//    }

//    private void checkIfUserIsVerified() {
//
//        Log.d(TAG, "checkIfUserIsVerified: " + mAuth.getCurrentUser().getUid());
//        firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().
//                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        DocumentSnapshot documentSnapshot = task.getResult();
//                        AdminDetailsClass adminDetailsClass = documentSnapshot.toObject(AdminDetailsClass.class);
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                              hideProgressDialog();
//                            }
//                        }, 2000);
//                    }
//                });
//    }

    private void sendUserToPendingVerificationPage() {
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/Admins",
                "Verification is pending", "New Admin account has registered and waiting for verification", getApplicationContext(), CreateAccountActivity.this);
        notificationsSender.SendNotifications();
        Intent intent = new Intent(CreateAccountActivity.this, PendingVerificationPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        AdminLogin.adminLoginActivity.finish();
        finish();
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        AdminLogin.adminLoginActivity.finish();
        finish();
    }

    private void initFields() {
        backButton = findViewById(R.id.back_button);

        addImageLayout = findViewById(R.id.add_image_button);
        addImageView = findViewById(R.id.add_image_view);
        addCircleImageView = findViewById(R.id.add_image_circle_image_view);

        name = findViewById(R.id.username);
        nameLayout = findViewById(R.id.username_layout);
        email = findViewById(R.id.email);
        emailLayout = findViewById(R.id.email_layout);
        department = findViewById(R.id.department);
        departmentLayout = findViewById(R.id.department_layout);

        passwordLayout = findViewById(R.id.password_layout);
        password = findViewById(R.id.password);

        imageErrorMessage = findViewById(R.id.image_error_message);
        loginButton = findViewById(R.id.login_button);

        // errorMessage = findViewById(R.id.error_message);

//        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);
//        confirmPassword = findViewById(R.id.confirm_password);

        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setText("");
        registerButton.setEnabled(false);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
        registerButton.setText("Create an account");
        registerButton.setEnabled(true);
    }
}
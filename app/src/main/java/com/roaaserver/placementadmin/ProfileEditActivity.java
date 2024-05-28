package com.roaaserver.placementadmin;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.shahabazimi.instagrampicker.InstagramPicker;

public class ProfileEditActivity extends AppCompatActivity {
    private static final String TAG = "ProfileEditActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private StorageReference userImageRef;

    private String[] branchArray;
    private TextInputLayout departmentLayout, usernameLayout;
    private AutoCompleteTextView departmentAutoCompleteTextView;
    private TextInputEditText username;
    private MaterialButton saveButton;
    private ProgressBar progressBar, imageProgressBar;
    private RelativeLayout imageLayout;
    private CircleImageView image;
    private ImageView addImageView;
    private ImageButton backButton;

    private Uri imageUri;
    private String downloadUri = "";
    private String tempDownloadUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        initFields();
        initFirebase();

        getCurrentUserDetails();
        branchAdapter();

        imageLayout.setOnClickListener(view -> selectImageFromDevice());

        saveButton.setOnClickListener(view -> checkDetails());
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void getCurrentUserDetails() {
        firestore.collection("Admins").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                AdminDetailsClass adminDetailsClass = documentSnapshot.toObject(AdminDetailsClass.class);
                username.setText(adminDetailsClass.getUserName());
                Picasso.get().load(adminDetailsClass.getImage()).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        addImageView.setVisibility(View.GONE);
                        imageProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        imageProgressBar.setVisibility(View.GONE);
                    }
                });

                departmentAutoCompleteTextView.setText(adminDetailsClass.getDepartment());
                tempDownloadUri = adminDetailsClass.getImage();
            }
        });

    }

    private void selectImageFromDevice() {
        InstagramPicker instagramPicker = new InstagramPicker(ProfileEditActivity.this);
        instagramPicker.show(1, 1, address -> {
            imageUri = Uri.parse(address);
            Log.d(TAG, "onActivityResult: " + imageUri);
            image.setImageURI(imageUri);
            addImageView.setVisibility(View.GONE);
        });
    }

    private void checkDetails() {
        boolean allClear = true;
        if (username.getText().toString().isEmpty()) {
            //usernameLayout.setError("Username can't be empty");
            Toast.makeText(ProfileEditActivity.this, "Name field can't be empty", Toast.LENGTH_SHORT).show();
            allClear = false;
        }
        if (username.getText().toString().length() < 4 && !username.getText().toString().isEmpty()) {
            // usernameLayout.setError("provide usename with sufficient length");
            allClear = false;
        }
        if (departmentAutoCompleteTextView.getText().toString().isEmpty()) {
            allClear = false;
        }
        if (allClear) {
            uploadProfileImageToFirebaseStorage();
        }
    }

    private void uploadProfileImageToFirebaseStorage() {
        showProgressDialog();
        Log.d(TAG, "UploadProfileImageToFirebaseStorage: before bitmap");
        String docID = mAuth.getCurrentUser().getUid();
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
                                        uploadRemainingDetailsInDatabase(downloadUri, docID);
                                    }
                                });
                            } else {
                                Log.d(TAG, "onComplete: failed to store profile image in storage " + task.getException());
                                hideProgressDialog();
                            }
                        }
                    });
        } else {
            if (tempDownloadUri.isEmpty()) {
                uploadRemainingDetailsInDatabase(downloadUri, docID);
            } else {
                uploadRemainingDetailsInDatabase(tempDownloadUri, docID);
            }
        }
    }

    private void uploadRemainingDetailsInDatabase(String downloadUri, String docID) {
        DocumentReference userReference = firestore.collection("Admins").document(docID);
        Map<String, Object> map = new HashMap<>();
        map.put("image", downloadUri);
        map.put("department", departmentAutoCompleteTextView.getText().toString());
        map.put("userName", username.getText().toString());

        userReference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onBackPressed();
                    finish();
                } else {
                    Toast.makeText(ProfileEditActivity.this, "Error while saving data", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });
        hideProgressDialog();
    }


    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userImageRef = FirebaseStorage.getInstance().getReference().child("User Images");
    }

    private void initFields() {
        //usernameLayout = findViewById(R.id.username_text_layout);
        username = findViewById(R.id.username_text);
        departmentLayout = findViewById(R.id.department_layout);
        departmentAutoCompleteTextView = findViewById(R.id.department_autocomplete_textview);
        saveButton = findViewById(R.id.save_button);
        progressBar = findViewById(R.id.progress_bar);
        imageLayout = findViewById(R.id.image_relative_layout);
        image = findViewById(R.id.image_circle_image_view);
        addImageView = findViewById(R.id.add_image_view);
        backButton = findViewById(R.id.back_button);
        imageProgressBar = findViewById(R.id.progress_bar_image);
    }

    private void branchAdapter() {
        branchArray = getResources().getStringArray(R.array.department);
        ArrayAdapter branchArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, branchArray);
        departmentAutoCompleteTextView.setAdapter(branchArrayAdapter);
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        saveButton.setText("");
        saveButton.setEnabled(false);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
        saveButton.setText("Save");
        saveButton.setEnabled(true);
    }

}
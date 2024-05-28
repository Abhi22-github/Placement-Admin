package com.roaaserver.placementadmin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.shahabazimi.instagrampicker.InstagramPicker;

public class CreateCompanyDetailsActivity extends AppCompatActivity {
    private static final String TAG = "CreateCampusDetailsActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private StorageReference companyImageRef;

    private ImageButton backButton;
    private MaterialButton createButton, addButton;
    private ProgressBar progressBar;
    private String[] courseArray, campusArray, genderArray, roundsArray, industryTypeArray, sscArray, hscArray, diplomaArray, engineeringArray;
    private AutoCompleteTextView coursesAutoCompleteTextView, campusAutoCompleteTextView, totalRoundAutoCompleteTextView,
            industryTypeAutoCompleteTextView, sscAutoCompleteTextView, hscAutoCompleteTextView, diplomaAutoCompleteTextView,
            engineeringAutoCompleteTextView, genderAutoCompleteTextView;
    private TextInputEditText roleText, companyText, locationText, salaryText, batchText, branchText, backlogText,registerLink,
            experienceText, startDate, applicationDeadline, websiteText, key1, value1, key2, value2, key3, value3, key4, value4, key5, value5;
    private TextInputLayout roleLayout, companyLayout, locationLayout, salaryLayout, coursesLayout, batchLayout, branchesLayout,
            sscMarksLayout, hscMarksLayout, diplomaMarksLayout, engineeringMarksLayout, backlogLayout, experienceLayout,registerLinkLayout,
            campusTypeLayout, totalRoundLayout, startDateLayout, applicationDeadlineLayout, websiteLayout, industryTypeLayout, genderLayout;
    private RelativeLayout imageRelativeLayout;
    private ImageView addPhotoImage;
    private CircleImageView companyLogo;
    private LinearLayout field1, field2, field3, field4, field5;

    private Uri imageUri;
    private String downloadUri = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company_details);

        initFields();

        initFirebase();

        backButton.setOnClickListener(view -> onBackPressed());


        courseAdapter();
        campusAdapter();
        totalRoundsAdapter();
        industryTypeAdapter();
        sscAdapter();
        hscAdapter();
        diplomaAdapter();
        engineeringAdapter();
        //  genderAdapter();

        startDate.setOnClickListener(view -> setStartDate());
        applicationDeadline.setOnClickListener(view -> setApplicationDeadline());


        salaryText.setOnEditorActionListener(this::disableNextFieldFocus);
        branchText.setOnEditorActionListener(this::disableNextFieldFocus);
        experienceText.setOnEditorActionListener(this::disableNextFieldFocus);
        websiteText.setOnEditorActionListener(this::disableNextFieldFocus);

        textWatcherMethod();

        imageRelativeLayout.setOnClickListener(view -> selectCompanyLogoFromDevice());

        createButton.setOnClickListener(view -> checkDetails());

        addButton.setOnClickListener(view -> setupDynamicFieldsInsert());

    }

    private void setupDynamicFieldsInsert() {
        if (field1.getVisibility() == View.GONE && field2.getVisibility() == View.GONE && field3.getVisibility() == View.GONE &&
                field4.getVisibility() == View.GONE && field5.getVisibility() == View.GONE) {
            field1.setVisibility(View.VISIBLE);
        } else if (field2.getVisibility() == View.GONE && field3.getVisibility() == View.GONE &&
                field4.getVisibility() == View.GONE && field5.getVisibility() == View.GONE) {
            field2.setVisibility(View.VISIBLE);

        } else if (field3.getVisibility() == View.GONE &&
                field4.getVisibility() == View.GONE && field5.getVisibility() == View.GONE) {
            field3.setVisibility(View.VISIBLE);

        } else if (field4.getVisibility() == View.GONE && field5.getVisibility() == View.GONE) {
            field4.setVisibility(View.VISIBLE);

        } else {
            field5.setVisibility(View.VISIBLE);
        }


    }

    private void setStartDate() {

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        MaterialDatePicker materialDatePicker = builder.setTitleText("Drive Start Date")
                .build();
        materialDatePicker.show(getSupportFragmentManager(), "start_date");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                startDate.setText(materialDatePicker.getHeaderText());
                startDateLayout.setError("");
                startDateLayout.setErrorEnabled(false);
            }
        });

    }

    private void setApplicationDeadline() {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        MaterialDatePicker materialDatePicker = builder.setTitleText("Drive Start Date")
                .build();
        materialDatePicker.show(getSupportFragmentManager(), "application_deadline");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                applicationDeadline.setText(materialDatePicker.getHeaderText());
                applicationDeadlineLayout.setError("");
                applicationDeadlineLayout.setErrorEnabled(false);
            }
        });
    }


    private void selectCompanyLogoFromDevice() {
        InstagramPicker instagramPicker = new InstagramPicker(CreateCompanyDetailsActivity.this);
        instagramPicker.show(1, 1, address -> {
            imageUri = Uri.parse(address);
            Log.d(TAG, "onActivityResult: " + imageUri);
            companyLogo.setImageURI(imageUri);
            addPhotoImage.setVisibility(View.GONE);
        });
    }

    private void textWatcherMethod() {
        roleText.addTextChangedListener(watcher);
        companyText.addTextChangedListener(watcher);
        locationText.addTextChangedListener(watcher);
        salaryText.addTextChangedListener(watcher);
        coursesAutoCompleteTextView.addTextChangedListener(watcher);
        // genderAutoCompleteTextView.addTextChangedListener(watcher);
        batchText.addTextChangedListener(watcher);
        branchText.addTextChangedListener(watcher);
        sscAutoCompleteTextView.addTextChangedListener(watcher);
        hscAutoCompleteTextView.addTextChangedListener(watcher);
        diplomaAutoCompleteTextView.addTextChangedListener(watcher);
        engineeringAutoCompleteTextView.addTextChangedListener(watcher);
        backlogText.addTextChangedListener(watcher);
        experienceText.addTextChangedListener(watcher);
        campusAutoCompleteTextView.addTextChangedListener(watcher);
        totalRoundAutoCompleteTextView.addTextChangedListener(watcher);
        websiteText.addTextChangedListener(watcher);
        industryTypeAutoCompleteTextView.addTextChangedListener(watcher);


    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            setInputFieldsToNormal(editable);
        }

    };

    private void setInputFieldsToNormal(Editable editable) {
        if (editable == roleText.getEditableText()) {
            roleLayout.setError("");
            roleLayout.setErrorEnabled(false);
        }

        if (editable == companyText.getEditableText()) {
            companyLayout.setError(null);
            companyLayout.setErrorEnabled(false);
        }

        if (editable == locationText.getEditableText()) {
            locationLayout.setError("");
            locationLayout.setErrorEnabled(false);
        }

        if (editable == salaryText.getEditableText()) {
            salaryLayout.setError("");
            salaryLayout.setErrorEnabled(false);
        }

        if (editable == coursesAutoCompleteTextView.getEditableText()) {
            coursesLayout.setError("");
            coursesLayout.setErrorEnabled(false);
        }

        if (editable == batchText.getEditableText()) {
            batchLayout.setError("");
            batchLayout.setErrorEnabled(false);
        }

        if (editable == branchText.getEditableText()) {
            branchesLayout.setError("");
            branchesLayout.setErrorEnabled(false);
        }

        if (editable == sscAutoCompleteTextView.getEditableText()) {
            sscMarksLayout.setError("");
            sscMarksLayout.setErrorEnabled(false);
        }

        if (editable == hscAutoCompleteTextView.getEditableText()) {
            hscMarksLayout.setError("");
            hscMarksLayout.setErrorEnabled(false);
        }

        if (editable == diplomaAutoCompleteTextView.getEditableText()) {
            diplomaMarksLayout.setError("");
            diplomaMarksLayout.setErrorEnabled(false);
        }

        if (editable == engineeringAutoCompleteTextView.getEditableText()) {
            engineeringMarksLayout.setError("");
            engineeringMarksLayout.setErrorEnabled(false);
        }

        if (editable == backlogText.getEditableText()) {
            backlogLayout.setError("");
            backlogLayout.setErrorEnabled(false);
        }

        if (editable == experienceText.getEditableText()) {
            experienceLayout.setError("");
            experienceLayout.setErrorEnabled(false);
        }

        if (editable == campusAutoCompleteTextView.getEditableText()) {
            campusTypeLayout.setError("");
            campusTypeLayout.setErrorEnabled(false);
        }

        if (editable == totalRoundAutoCompleteTextView.getEditableText()) {
            totalRoundLayout.setError("");
            totalRoundLayout.setErrorEnabled(false);
        }

        if (editable == startDate.getEditableText()) {
            startDateLayout.setError("");
            startDateLayout.setErrorEnabled(false);
        }

        if (editable == applicationDeadline.getEditableText()) {
            applicationDeadlineLayout.setError("");
            applicationDeadlineLayout.setErrorEnabled(false);
        }

        if (editable == websiteText.getEditableText()) {
            websiteLayout.setError("");
            websiteLayout.setErrorEnabled(false);
        }

        if (editable == industryTypeAutoCompleteTextView.getEditableText()) {
            industryTypeLayout.setError("");
            industryTypeLayout.setErrorEnabled(false);
        }

    }

    private void checkDetails() {
        showProgressDialog();
        boolean allclear = true;

        if (roleText.getText().toString().isEmpty()) {
            roleLayout.setError("Please fill above details");
            allclear = false;
        }

        if (companyText.getText().toString().isEmpty()) {
            companyLayout.setError("Please fill above details");
            allclear = false;
        }

        if (locationText.getText().toString().isEmpty()) {
            locationLayout.setError("Please fill above details");
            allclear = false;
        }

        if (salaryText.getText().toString().isEmpty()) {
            salaryLayout.setError("Please fill above details");
            allclear = false;
        }

        if (coursesAutoCompleteTextView.getText().toString().isEmpty()) {
            coursesLayout.setError("Please fill above details");
            allclear = false;
        }

        if (batchText.getText().toString().isEmpty()) {
            batchLayout.setError("Please fill above details");
            allclear = false;
        }

        if (branchText.getText().toString().isEmpty()) {
            branchesLayout.setError("Please fill above details");
            allclear = false;
        }

        if (roleText.getText().toString().isEmpty()) {
            roleLayout.setError("Please fill above details");
            allclear = false;
        }

        if (sscAutoCompleteTextView.getText().toString().isEmpty()) {
            sscMarksLayout.setError("Please fill above details");
            allclear = false;
        }

        if (hscAutoCompleteTextView.getText().toString().isEmpty()) {
            hscMarksLayout.setError("Please fill above details");
            allclear = false;
        }

        if (diplomaAutoCompleteTextView.getText().toString().isEmpty()) {
            diplomaMarksLayout.setError("Please fill above details");
            allclear = false;
        }

        if (engineeringAutoCompleteTextView.getText().toString().isEmpty()) {
            engineeringMarksLayout.setError("Please fill above details");
            allclear = false;
        }

        if (backlogText.getText().toString().isEmpty()) {
            backlogLayout.setError("Please fill above details");
            allclear = false;
        }

        if (experienceText.getText().toString().isEmpty()) {
            experienceLayout.setError("Please fill above details");
            allclear = false;
        }

        if (campusAutoCompleteTextView.getText().toString().isEmpty()) {
            campusTypeLayout.setError("Please fill above details");
            allclear = false;
        }

        if (totalRoundAutoCompleteTextView.getText().toString().isEmpty()) {
            totalRoundLayout.setError("Please fill above details");
            allclear = false;
        }

        if (startDate.getText().toString().isEmpty()) {
            startDateLayout.setError("Please fill above details");
            allclear = false;
        }

        if (applicationDeadline.getText().toString().isEmpty()) {
            applicationDeadlineLayout.setError("Please fill above details");
            allclear = false;
        }

        if (websiteText.getText().toString().isEmpty()) {
            websiteLayout.setError("Please fill above details");
            allclear = false;
        }

        if (industryTypeAutoCompleteTextView.getText().toString().isEmpty()) {
            industryTypeLayout.setError("Please fill above details");
            allclear = false;
        }
        if (registerLink.getText().toString().isEmpty()) {
            registerLinkLayout.setError("Please fill above details");
            allclear = false;
        }
        if (allclear) {
            uploadProfileImageToFirebaseStorage();
        } else {
            hideProgressDialog();
        }

    }

    private void uploadProfileImageToFirebaseStorage() {
        Log.d(TAG, "UploadProfileImageToFirebaseStorage: before bitmap");
        String docID = firestore.collection("Company Details").document().getId();
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
            StorageReference fileRef = companyImageRef.child(docID + ".jpg");
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
                                        linkDetailsToObject(downloadUri, docID);
                                    }
                                });
                            } else {
                                Log.d(TAG, "onComplete: failed to store profile image in storage " + task.getException());
                                hideProgressDialog();
                            }
                        }
                    });
        } else {
            linkDetailsToObject(null, docID);
        }
    }

    private void linkDetailsToObject(String downloadUri, String docID) {
        showProgressDialog();
        CompanyDetailsClass companyDetailsClass = new CompanyDetailsClass();
        companyDetailsClass.setCompanyImage(downloadUri);
        companyDetailsClass.setRole(roleText.getText().toString());
        companyDetailsClass.setCompanyName(companyText.getText().toString());
        companyDetailsClass.setCompanyLocation(locationText.getText().toString());
        companyDetailsClass.setSalary(salaryText.getText().toString());
        companyDetailsClass.setCourses(coursesAutoCompleteTextView.getText().toString());
        companyDetailsClass.setBatch(batchText.getText().toString());
        companyDetailsClass.setBranches(branchText.getText().toString());
        companyDetailsClass.setSscMarks(sscAutoCompleteTextView.getText().toString());
        companyDetailsClass.setHscMarks(hscAutoCompleteTextView.getText().toString());
        companyDetailsClass.setDiplomaMarks(diplomaAutoCompleteTextView.getText().toString());
        companyDetailsClass.setGraduationMarks(engineeringAutoCompleteTextView.getText().toString());
        companyDetailsClass.setBacklog(backlogText.getText().toString());
        companyDetailsClass.setExperience(experienceText.getText().toString());

        if(!key1.getText().toString().isEmpty() && !value1.getText().toString().isEmpty())
        companyDetailsClass.setKey1(key1.getText().toString() + "@" + value1.getText().toString());

        if(!key2.getText().toString().isEmpty() && !value2.getText().toString().isEmpty())
        companyDetailsClass.setKey2(key2.getText().toString() + "@" + value2.getText().toString());

        if(!key3.getText().toString().isEmpty() && !value3.getText().toString().isEmpty())
        companyDetailsClass.setKey3(key3.getText().toString() + "@" + value3.getText().toString());

        if(!key4.getText().toString().isEmpty() && !value4.getText().toString().isEmpty())
        companyDetailsClass.setKey4(key4.getText().toString() + "@" + value4.getText().toString());

        if(!key5.getText().toString().isEmpty() && !value5.getText().toString().isEmpty())
        companyDetailsClass.setKey5(key5.getText().toString() + "@" + value5.getText().toString());

        companyDetailsClass.setCampusType(campusAutoCompleteTextView.getText().toString());
        companyDetailsClass.setTotalRounds(totalRoundAutoCompleteTextView.getText().toString());
        companyDetailsClass.setStartDate(startDate.getText().toString());
        companyDetailsClass.setApplicationDeadline(applicationDeadline.getText().toString());
        companyDetailsClass.setWebsite(websiteText.getText().toString());
        companyDetailsClass.setIndustryType(industryTypeAutoCompleteTextView.getText().toString());
        companyDetailsClass.setApplyLink(registerLink.getText().toString());

        addDetailsToFirebase(companyDetailsClass, docID);


    }

    private void addDetailsToFirebase(CompanyDetailsClass companyDetailsClass, String docID) {

        DocumentReference companyReference = firestore.collection("Company Details").document(docID);
        companyDetailsClass.setDocumentID(docID);

        companyReference.set(companyDetailsClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendUserToMainActivity();
                } else {
                    Toast.makeText(CreateCompanyDetailsActivity.this, "Error while saving data", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });
        hideProgressDialog();
    }

    private boolean disableNextFieldFocus(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            hideKeyboard(CreateCompanyDetailsActivity.this);
            return true;
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void courseAdapter() {
        courseArray = getResources().getStringArray(R.array.courses);
        ArrayAdapter courseArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, courseArray);
        coursesAutoCompleteTextView.setAdapter(courseArrayAdapter);
    }

    private void campusAdapter() {
        campusArray = getResources().getStringArray(R.array.campusType);
        ArrayAdapter campusArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, campusArray);
        campusAutoCompleteTextView.setAdapter(campusArrayAdapter);


    }

    private void totalRoundsAdapter() {
        roundsArray = getResources().getStringArray(R.array.noOfRounds);
        ArrayAdapter roundsArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, roundsArray);
        totalRoundAutoCompleteTextView.setAdapter(roundsArrayAdapter);
    }

    private void industryTypeAdapter() {
        industryTypeArray = getResources().getStringArray(R.array.industryType);
        ArrayAdapter industryTypeArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, industryTypeArray);
        industryTypeAutoCompleteTextView.setAdapter(industryTypeArrayAdapter);
    }

    private void sscAdapter() {
        sscArray = getResources().getStringArray(R.array.sscMarks);
        ArrayAdapter sscArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, sscArray);
        sscAutoCompleteTextView.setAdapter(sscArrayAdapter);
    }

    private void hscAdapter() {
        hscArray = getResources().getStringArray(R.array.hscMarks);
        ArrayAdapter hscArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, hscArray);
        hscAutoCompleteTextView.setAdapter(hscArrayAdapter);
    }

    private void diplomaAdapter() {
        diplomaArray = getResources().getStringArray(R.array.diplomaMarks);
        ArrayAdapter diplomaArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, diplomaArray);
        diplomaAutoCompleteTextView.setAdapter(diplomaArrayAdapter);
    }

    private void engineeringAdapter() {
        engineeringArray = getResources().getStringArray(R.array.engineeringMarks);
        ArrayAdapter engineeringArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, engineeringArray);
        engineeringAutoCompleteTextView.setAdapter(engineeringArrayAdapter);
    }

//    private void genderAdapter() {
//        genderArray = getResources().getStringArray(R.array.gender);
//        ArrayAdapter engineeringArrayAdapter = new ArrayAdapter<String>(this, R.layout.exposed_dropdown_design, genderArray);
//        genderAutoCompleteTextView.setAdapter(engineeringArrayAdapter);
//    }

    private void initFields() {
        backButton = findViewById(R.id.back_button);

        imageRelativeLayout = findViewById(R.id.image_relative_layout);
        addPhotoImage = findViewById(R.id.add_image_view);
        companyLogo = findViewById(R.id.company_logo_circle_image_view);

        roleText = findViewById(R.id.role_textview);
        roleLayout = findViewById(R.id.role_layout);

        companyText = findViewById(R.id.company_textview);
        companyLayout = findViewById(R.id.company_layout);

        locationText = findViewById(R.id.location_textview);
        locationLayout = findViewById(R.id.location_layout);

        salaryText = findViewById(R.id.salary_textview);
        salaryLayout = findViewById(R.id.salary_layout);

        // genderAutoCompleteTextView = findViewById(R.id.gender_autocomplete_textview);
        //  genderLayout = findViewById(R.id.gender_layout);

        coursesAutoCompleteTextView = findViewById(R.id.courses_autocomplete_textview);
        coursesLayout = findViewById(R.id.courses_layout);

        batchText = findViewById(R.id.batch_textview);
        batchLayout = findViewById(R.id.batch_layout);

        branchText = findViewById(R.id.branch_textview);
        branchesLayout = findViewById(R.id.branches_layout);

        sscAutoCompleteTextView = findViewById(R.id.ssc_autocomplete_textview);
        sscMarksLayout = findViewById(R.id.ssc_marks_layout);

        hscAutoCompleteTextView = findViewById(R.id.hsc_autocomplete_textview);
        hscMarksLayout = findViewById(R.id.hsc_layout);

        diplomaAutoCompleteTextView = findViewById(R.id.diploma_autocomplete_textview);
        diplomaMarksLayout = findViewById(R.id.diploma_layout);

        engineeringAutoCompleteTextView = findViewById(R.id.engineering_autocomplete_textview);
        engineeringMarksLayout = findViewById(R.id.engineering_marks_layout);

        backlogText = findViewById(R.id.backlog_textview);
        backlogLayout = findViewById(R.id.backlog_layout);

        experienceText = findViewById(R.id.experience_textview);
        experienceLayout = findViewById(R.id.experience_layout);

        campusAutoCompleteTextView = findViewById(R.id.campus_autocomplete_textview);
        campusTypeLayout = findViewById(R.id.campus_type_layout);

        totalRoundAutoCompleteTextView = findViewById(R.id.total_rounds_autocomplete_textview);
        totalRoundLayout = findViewById(R.id.total_rounds_layout);

        startDate = findViewById(R.id.start_date_textview);
        startDateLayout = findViewById(R.id.start_data_layout);

        applicationDeadline = findViewById(R.id.application_date_textview);
        applicationDeadlineLayout = findViewById(R.id.application_date_layout);

        websiteText = findViewById(R.id.website_textview);
        websiteLayout = findViewById(R.id.website_layout);

        industryTypeAutoCompleteTextView = findViewById(R.id.industry_type_autocomplete_textview);
        industryTypeLayout = findViewById(R.id.industry_type_layout);

        registerLink = findViewById(R.id.registeration_link_textview);
        registerLinkLayout = findViewById(R.id.registeration_link_layout);

        createButton = findViewById(R.id.create_button);
        progressBar = findViewById(R.id.progress_bar);


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

        addButton = findViewById(R.id.add_button);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        companyImageRef = FirebaseStorage.getInstance().getReference().child("Company Images");
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        createButton.setText("");
        createButton.setEnabled(false);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
        createButton.setText("Create");
        createButton.setEnabled(true);
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(CreateCompanyDetailsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
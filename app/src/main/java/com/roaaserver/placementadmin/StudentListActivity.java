package com.roaaserver.placementadmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.roaaserver.placementadmin.Adpters.SearchAdapterClass;
import com.roaaserver.placementadmin.Adpters.StudentFilterAdapter;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;
import com.roaaserver.placementadmin.Models.StudentInfoClass;
import com.roaaserver.placementadmin.Other.ExcelWriteClass;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {
    private static final String TAG = "StudentListActivity";

    private RecyclerView recyclerView;
    private SearchAdapterClass searchAdapter;
    //private AutoCompleteTextView branch;
    private ExtendedFloatingActionButton filterButton;
    private FloatingActionButton downloadButton;
    private StudentFilterAdapter studentFilterAdapter;
    ArrayList<StudentInfoClass> studentList = new ArrayList<StudentInfoClass>();
    BottomSheetDialog bottomSheetDialog;
    private ImageButton backButton, searchButton;
    private SimpleSearchView searchView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerAdapter searchRecyclerAdapter;
    private LinearLayout noDataLayout;
    private ProgressBar progressBar;
    public MaterialAlertDialogBuilder m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        initFields();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        initFirebase();
        setupSearchRecyclerView();

        backButton.setOnClickListener(v -> onBackPressed());
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.isSearchOpen()) {
                    searchView.closeSearch();
                } else {
                    searchView.showSearch();
                }
            }
        });

        searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SimpleSearchView", "Submit:" + query);
                processSearch(query);
                hideSoftKeyboard((Activity) StudentListActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SimpleSearchView", "Text changed:" + newText);
                processSearch(newText);
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");

                return false;
            }
        });

        searchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Log.d("SimpleSearchView", "onSearchViewShown");
            }

            @Override
            public void onSearchViewClosed() {
                Log.d("SimpleSearchView", "onSearchViewClosed");
                fillAllRecords();
            }

            @Override
            public void onSearchViewShownAnimation() {
                Log.d("SimpleSearchView", "onSearchViewShownAnimation");
            }

            @Override
            public void onSearchViewClosedAnimation() {
                Log.d("SimpleSearchView", "onSearchViewClosedAnimation");
            }
        });

        filterButton.setOnClickListener(v -> showBottomSheetDialog());

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    private void showDialog() {
//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.file_option_name_dialog);
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//        dialog.getWindow().setLayout((6 * width)/7, (2 * height)/8);
//        dialog.show();
        final View customDialogView = getLayoutInflater().inflate(
                R.layout.file_option_name_dialog, null);
        new MaterialAlertDialogBuilder(StudentListActivity.this)
                .setTitle("Create file")
                .setMessage("Do you want to create an .xls file with " + studentList.size() + " rows")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        MaterialAlertDialogBuilder m = new MaterialAlertDialogBuilder(StudentListActivity.this)
                                .setTitle("Please provide name and columns for .xls file")
                                .setView(customDialogView)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        AlertDialog dialog = m.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        ChipGroup columnChipGroup = customDialogView.findViewById(R.id.columns_chip_group);
                                        TextInputLayout fileLayout = customDialogView.findViewById(R.id.file_name_layout);
                                        TextInputEditText fileName = customDialogView.findViewById(R.id.file_name);
                                        Chip resume = customDialogView.findViewById(R.id.resume_chip);
                                        Chip name = customDialogView.findViewById(R.id.name_chip);
                                        Chip number = customDialogView.findViewById(R.id.number_chip);
                                        Chip email = customDialogView.findViewById(R.id.email_chip);
                                        Chip address = customDialogView.findViewById(R.id.address_chip);
                                        Chip erp = customDialogView.findViewById(R.id.erp_chip);
                                        Chip prn = customDialogView.findViewById(R.id.prn_chip);
                                        Chip bod = customDialogView.findViewById(R.id.bod_chip);
                                        Chip gender = customDialogView.findViewById(R.id.gender_chip);
                                        Chip sscPercentage = customDialogView.findViewById(R.id.ssc_percentage_chip);
                                        Chip sscCollege = customDialogView.findViewById(R.id.ssc_college_chip);
                                        Chip sscYear = customDialogView.findViewById(R.id.ssc_year_chip);
                                        Chip hscPercentage = customDialogView.findViewById(R.id.hsc_percentage_chip);
                                        Chip hscCollege = customDialogView.findViewById(R.id.hsc_college_chip);
                                        Chip hscYear = customDialogView.findViewById(R.id.hsc_year_chip);
                                        Chip diplomaPercentage = customDialogView.findViewById(R.id.diploma_percentage_chip);
                                        Chip diplomaCollege = customDialogView.findViewById(R.id.diploma_college_chip);
                                        Chip diplomaYear = customDialogView.findViewById(R.id.diploma_year_chip);
                                        Chip graduationYear = customDialogView.findViewById(R.id.graducation_year_chip);
                                        Chip branch = customDialogView.findViewById(R.id.branch_chip);
                                        Chip division = customDialogView.findViewById(R.id.division_chip);
                                        Chip be = customDialogView.findViewById(R.id.be_aggregate_chip);
                                        Chip bePercentage = customDialogView.findViewById(R.id.be_aggregate_percentage_chip);
                                        Chip activeBacklog = customDialogView.findViewById(R.id.active_backlog_chip);
                                        Chip previousBacklog = customDialogView.findViewById(R.id.previous_backlogs_chip);
                                        Chip sem1 = customDialogView.findViewById(R.id.sem1_chip);
                                        Chip sem2 = customDialogView.findViewById(R.id.sem2_chip);
                                        Chip sem3 = customDialogView.findViewById(R.id.sem3_chip);
                                        Chip sem4 = customDialogView.findViewById(R.id.sem4_chip);
                                        Chip sem5 = customDialogView.findViewById(R.id.sem5_chip);
                                        Chip sem6 = customDialogView.findViewById(R.id.sem6_chip);
                                        Chip sem7 = customDialogView.findViewById(R.id.sem7_chip);
                                        Chip sem8 = customDialogView.findViewById(R.id.sem8_chip);
                                        Chip hscGap = customDialogView.findViewById(R.id.hsc_gap_chip);
                                        Chip engineeringGap = customDialogView.findViewById(R.id.engineering_gap_chip);
                                        Chip engineeringGapYears = customDialogView.findViewById(R.id.engineering_gap_years_chip);
                                        Chip placed = customDialogView.findViewById(R.id.placed_chip);
                                        Chip placedCompanyName = customDialogView.findViewById(R.id.placed_company_name_chip);
                                        Chip placedCompanyLocation = customDialogView.findViewById(R.id.placed_company_location_chip);
                                        Chip placedCompanyPackage = customDialogView.findViewById(R.id.placed_company_package_chip);
                                        Chip interviewDate = customDialogView.findViewById(R.id.interview_date_chip);
                                        Chip joiningDate = customDialogView.findViewById(R.id.joining_date_chip);
                                        Chip offerLetter = customDialogView.findViewById(R.id.offer_letter_chip);
                                        Chip japnese = customDialogView.findViewById(R.id.japanese_chip);
                                        Chip jlpt = customDialogView.findViewById(R.id.jlpt_chip);
                                        Chip internship = customDialogView.findViewById(R.id.internship_chip);
                                        Chip certification = customDialogView.findViewById(R.id.certifications_chip);


                                        String fileNameString = "demo.xls";
                                        if (fileName.getText().toString().isEmpty()) {
                                            fileLayout.setError("Please enter file name.");
                                            fileLayout.setBoxBackgroundColor(getColor(R.color.red_faint));

                                        } else {

                                            fileNameString = fileName.getText().toString() + ".xls";
                                            dialogInterface.dismiss();
                                            if (!studentList.isEmpty()) {
                                                ExcelWriteClass excelWriteClass = new ExcelWriteClass(studentList, StudentListActivity.this, fileNameString);
                                                excelWriteClass.setResume(resume.isChecked());
                                                excelWriteClass.setName(name.isChecked());
                                                excelWriteClass.setEmail(email.isChecked());
                                                excelWriteClass.setNumber(number.isChecked());
                                                excelWriteClass.setAddress(address.isChecked());
                                                excelWriteClass.setErp(erp.isChecked());
                                                excelWriteClass.setPrn(prn.isChecked());
                                                excelWriteClass.setBod(bod.isChecked());
                                                excelWriteClass.setGender(gender.isChecked());
                                                excelWriteClass.setSscPercentage(sscPercentage.isChecked());
                                                excelWriteClass.setSscCollege(sscCollege.isChecked());
                                                excelWriteClass.setSscYear(sscYear.isChecked());
                                                excelWriteClass.setHscPercentage(hscPercentage.isChecked());
                                                excelWriteClass.setHscCollege(hscCollege.isChecked());
                                                excelWriteClass.setHscYear(hscYear.isChecked());
                                                excelWriteClass.setDiplomaPercentage(diplomaPercentage.isChecked());
                                                excelWriteClass.setDiplomaCollege(diplomaCollege.isChecked());
                                                excelWriteClass.setDiplomaYear(diplomaYear.isChecked());
                                                excelWriteClass.setGraduationYear(graduationYear.isChecked());
                                                excelWriteClass.setBranch(branch.isChecked());
                                                excelWriteClass.setDivision(division.isChecked());
                                                excelWriteClass.setBe(be.isChecked());
                                                excelWriteClass.setBePercentage(bePercentage.isChecked());
                                                excelWriteClass.setActiveBacklog(activeBacklog.isChecked());
                                                excelWriteClass.setPreviousBacklog(previousBacklog.isChecked());
                                                excelWriteClass.setSem1(sem1.isChecked());
                                                excelWriteClass.setSem2(sem2.isChecked());
                                                excelWriteClass.setSem3(sem3.isChecked());
                                                excelWriteClass.setSem4(sem4.isChecked());
                                                excelWriteClass.setSem5(sem5.isChecked());
                                                excelWriteClass.setSem6(sem6.isChecked());
                                                excelWriteClass.setSem7(sem7.isChecked());
                                                excelWriteClass.setSem8(sem8.isChecked());
                                                excelWriteClass.setHscGap(hscGap.isChecked());
                                                excelWriteClass.setEngineeringGap(engineeringGap.isChecked());
                                                excelWriteClass.setEngineeringGapYears(engineeringGapYears.isChecked());
                                                excelWriteClass.setPlaced(placed.isChecked());
                                                excelWriteClass.setPlacedCompanyName(placedCompanyName.isChecked());
                                                excelWriteClass.setPlacedCompanyLocation(placedCompanyLocation.isChecked());
                                                excelWriteClass.setPlacedCompanyPackage(placedCompanyPackage.isChecked());
                                                excelWriteClass.setInterviewDate(interviewDate.isChecked());
                                                excelWriteClass.setJoiningDate(joiningDate.isChecked());
                                                excelWriteClass.setOfferLetter(offerLetter.isChecked());
                                                excelWriteClass.setJapanese(japnese.isChecked());
                                                excelWriteClass.setJlpt(jlpt.isChecked());
                                                excelWriteClass.setInternship(internship.isChecked());
                                                excelWriteClass.setCertification(certification.isChecked());

                                                excelWriteClass.writeData();
                                            }
                                        }


                                        dialog.dismiss();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();


    }


    private void showBottomSheetDialog() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        ChipGroup branch, placementStatus, profileStatus, diploma, hsc, degree, hscGap, engineeringGap;
        Chip csChip, itChip, civilChip, mechChip, e_tc_chip, prod_chip, a_r_chip, electricalChip, e_c_chip, placedChip, unplacedChip,
                activeChip, freezeChip, diplomaAbove65, diplomaAbove60, diplomaBelow60, hscAbove65, hscAbove60, hscBelow60,
                degreeAbove80, degreeBelow60, degreeAbove60, hscNoGap, hscGapPresent, hsc1YearGap, hsc2YearGap,
                hscMorethan2YearGap, engNoGap, eng1YearGap, eng2YearGap, engMorethan2YearGap;
        MaterialButton resetButton, applyButton;
        {
            branch = bottomSheetDialog.findViewById(R.id.branch_chip_group);
            placementStatus = bottomSheetDialog.findViewById(R.id.placement_status_chip_group);
            profileStatus = bottomSheetDialog.findViewById(R.id.profile_status_chip_group);
            diploma = bottomSheetDialog.findViewById(R.id.diploma_chip_group);
            hsc = bottomSheetDialog.findViewById(R.id.hsc_chip_group);
            degree = bottomSheetDialog.findViewById(R.id.degree_chip_group);
            hscGap = bottomSheetDialog.findViewById(R.id.hsc_gap_chip_group);
            engineeringGap = bottomSheetDialog.findViewById(R.id.engineering_gap_chip_group);
        }
        csChip = bottomSheetDialog.findViewById(R.id.cs_chip);
        itChip = bottomSheetDialog.findViewById(R.id.it_chip);
        civilChip = bottomSheetDialog.findViewById(R.id.civil_chip);
        mechChip = bottomSheetDialog.findViewById(R.id.mech_chip);
        e_tc_chip = bottomSheetDialog.findViewById(R.id.e_tc_chip);
        prod_chip = bottomSheetDialog.findViewById(R.id.prod_chip);
        a_r_chip = bottomSheetDialog.findViewById(R.id.a_r_chip);
        electricalChip = bottomSheetDialog.findViewById(R.id.electrical_chip);
        e_c_chip = bottomSheetDialog.findViewById(R.id.e_c_chip);
        placedChip = bottomSheetDialog.findViewById(R.id.placed_chip);
        unplacedChip = bottomSheetDialog.findViewById(R.id.unplaced_chip);
        activeChip = bottomSheetDialog.findViewById(R.id.active_chip);
        freezeChip = bottomSheetDialog.findViewById(R.id.freeze_chip);
        diplomaAbove65 = bottomSheetDialog.findViewById(R.id.diploma_above65_chip);
        diplomaAbove60 = bottomSheetDialog.findViewById(R.id.diploma_above60_chip);
        diplomaBelow60 = bottomSheetDialog.findViewById(R.id.diploma_below_60_chip);
        hscAbove65 = bottomSheetDialog.findViewById(R.id.hsc_above65_chip);
        hscAbove60 = bottomSheetDialog.findViewById(R.id.hsc_above60_chip);
        hscBelow60 = bottomSheetDialog.findViewById(R.id.hsc_below60_chip);
        degreeAbove80 = bottomSheetDialog.findViewById(R.id.degree_above80_chip);
        degreeBelow60 = bottomSheetDialog.findViewById(R.id.degree_below60_chip);
        degreeAbove60 = bottomSheetDialog.findViewById(R.id.degree_above60_chip);
        hscNoGap = bottomSheetDialog.findViewById(R.id.hsc_no_gap_chip);
        hscGapPresent = bottomSheetDialog.findViewById(R.id.hsc_gap_present_chip);
        hsc1YearGap = bottomSheetDialog.findViewById(R.id.hsc_1_year_gap_chip);
        hsc2YearGap = bottomSheetDialog.findViewById(R.id.hsc_2_year_gap_chip);
        hscMorethan2YearGap = bottomSheetDialog.findViewById(R.id.hsc_morethan2_year_gap_chip);
        engNoGap = bottomSheetDialog.findViewById(R.id.engineering_no_gap_chip);
        eng1YearGap = bottomSheetDialog.findViewById(R.id.engineering_1year_gap_chip);
        eng2YearGap = bottomSheetDialog.findViewById(R.id.engineering_2year_gap_chip);
        engMorethan2YearGap = bottomSheetDialog.findViewById(R.id.engineering_morethan2year_gap_chip);

        resetButton = bottomSheetDialog.findViewById(R.id.reset_button);
        applyButton = bottomSheetDialog.findViewById(R.id.apply_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset all fields

                branch.clearCheck();
                placementStatus.clearCheck();
                profileStatus.clearCheck();
                diploma.clearCheck();
                hsc.clearCheck();
                degree.clearCheck();
                hscGap.clearCheck();
                engineeringGap.clearCheck();
                studentList.clear();

            }
        });
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ArrayList<String> list = new ArrayList<>();
                list.clear();
                if (csChip.isChecked())
                    list.add("Computer Engineering");

                if (itChip.isChecked())
                    list.add("Information Technology");

                if (civilChip.isChecked())
                    list.add("Civil Engineering");

                if (mechChip.isChecked())
                    list.add("Mechanical Engineering");

                if (prod_chip.isChecked())
                    list.add("Production Engineering");

                if (electricalChip.isChecked())
                    list.add("Electrical Engineering");

                if (a_r_chip.isChecked())
                    list.add("Automation and Robotics");

                if (e_tc_chip.isChecked())
                    list.add("E and TC Engineering");

                if (e_c_chip.isChecked())
                    list.add("Electronics and Computer Engineering");


                Query query = firestore.collection("Students Information");

//                if (!list.isEmpty()) {
//                    Log.d(TAG, "onClick: 1--------------");
//                    query.whereIn("branch", list);
//                }
                //  query.whereIn("graduationYear", Arrays.asList("2022","0"));
                if (placedChip.isChecked() && !unplacedChip.isChecked()) {
                    query = query.whereEqualTo("placed", true);
                } else if (!placedChip.isChecked() && unplacedChip.isChecked()) {
                    query = query.whereEqualTo("placed", false);
                }

                if (activeChip.isChecked() && !freezeChip.isChecked()) {
                    query = query.whereEqualTo("isActive", true);
                } else if (!activeChip.isChecked() && freezeChip.isChecked()) {
                    query = query.whereEqualTo("isActive", false);
                }

//                if (diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
//                    query= query.whereGreaterThanOrEqualTo("diplomaPercentage", 60);
//                } else if (!diplomaAbove60.isChecked() && diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
//                    query= query.whereGreaterThanOrEqualTo("diplomaPercentage", 65);
//                } else if (!diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && diplomaBelow60.isChecked()) {
//                    query= query.whereLessThanOrEqualTo("diplomaPercentage", 60);
//                }
//
//                if (hscAbove60.isChecked() && !hscAbove65.isChecked() && !hscBelow60.isChecked()) {
//                    query= query.whereGreaterThanOrEqualTo("hscPercentage", 60);
//                } else if (!hscAbove60.isChecked() && hscAbove65.isChecked() && !hscBelow60.isChecked()) {
//                    query= query.whereGreaterThanOrEqualTo("hscPercentage", 65);
//                } else if (!hscAbove60.isChecked() && !hscAbove65.isChecked() && hscBelow60.isChecked()) {
//                    query= query.whereLessThanOrEqualTo("hscPercentage", 60);
//                }
//
                if (degreeAbove60.isChecked() && !degreeAbove80.isChecked() && !degreeBelow60.isChecked()) {
                    query = query.whereGreaterThanOrEqualTo("aggregatePercentage", 60);
                } else if (!degreeAbove60.isChecked() && degreeAbove80.isChecked() && !degreeBelow60.isChecked()) {
                    query = query.whereGreaterThanOrEqualTo("aggregatePercentage", 80);
                } else if (!degreeAbove60.isChecked() && !degreeAbove80.isChecked() && degreeBelow60.isChecked()) {
                    query = query.whereLessThan("aggregatePercentage", 60);
                }

                if (hscGapPresent.isChecked() && !hscNoGap.isChecked()) {
                    query = query.whereEqualTo("gapPresent", true);
                } else if (!hscGapPresent.isChecked() && hscNoGap.isChecked()) {
                    query = query.whereEqualTo("gapPresent", false);
                }

                if (engNoGap.isChecked() && !eng1YearGap.isChecked() && !eng2YearGap.isChecked() && !engMorethan2YearGap.isChecked()) {
                    query = query.whereEqualTo("engineeringGapPresent", false);
                } else if (!engNoGap.isChecked() && (eng1YearGap.isChecked() || eng2YearGap.isChecked() || engMorethan2YearGap.isChecked())) {
                    query = query.whereEqualTo("engineeringGapPresent", true);
                    if (eng1YearGap.isChecked()) {
                        query = query.whereEqualTo("gapYears", "1 year");
                    }
                    if (eng2YearGap.isChecked()) {
                        query = query.whereEqualTo("gapYears", "2 year");
                    }
                    if (engMorethan2YearGap.isChecked()) {
                        query = query.whereEqualTo("gapYears", "more than 2 years");
                    }
                }


                studentList.clear();
                if (list.isEmpty()) {
                    query.whereEqualTo("verified", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.d(TAG, "onEvent: " + error.getMessage());
                                return;
                            }

                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    StudentInfoClass studentInfoClass = dc.getDocument().toObject(StudentInfoClass.class);

                                    if (hscAbove60.isChecked() && !hscAbove65.isChecked() && !hscBelow60.isChecked()) {
                                        if (studentInfoClass.getHscPercentage() >= 60 || studentInfoClass.getHscPercentage() == -1) {

                                            {
                                                if (diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }
                                                } else if (!diplomaAbove60.isChecked() && diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 65 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else if (!diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() < 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else {
                                                    studentList.add(studentInfoClass);
                                                }
                                            }

                                        }
                                    } else if (!hscAbove60.isChecked() && hscAbove65.isChecked() && !hscBelow60.isChecked()) {
                                        if (studentInfoClass.getHscPercentage() >= 65 || studentInfoClass.getHscPercentage() == -1) {
                                            {
                                                if (diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }
                                                } else if (!diplomaAbove60.isChecked() && diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 65 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else if (!diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() < 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else {
                                                    studentList.add(studentInfoClass);
                                                }
                                            }
                                        }

                                    } else if (!hscAbove60.isChecked() && !hscAbove65.isChecked() && hscBelow60.isChecked()) {
                                        if (studentInfoClass.getHscPercentage() < 60 || studentInfoClass.getHscPercentage() == -1) {
                                            {
                                                if (diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }
                                                } else if (!diplomaAbove60.isChecked() && diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 65 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else if (!diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() < 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else {
                                                    studentList.add(studentInfoClass);
                                                }
                                            }
                                        }

                                    } else {
                                        studentList.add(studentInfoClass);
                                    }


                                    Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                                }

                                studentFilterAdapter.notifyDataSetChanged();
                            }


                            if (studentList.isEmpty()) {
                                studentList.clear();
                                studentFilterAdapter = new StudentFilterAdapter(studentList, StudentListActivity.this);
                                recyclerView.setAdapter(studentFilterAdapter);
                                // setVerifiedDropdownInitial();
                            }
                            setUpEmptyLayouts(studentList.size());

                        }
                    });
                } else {
                    query.whereEqualTo("verified", true).whereIn("branch", list).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.d(TAG, "onEvent: " + error.getMessage());
                                return;
                            }

                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    StudentInfoClass studentInfoClass = dc.getDocument().toObject(StudentInfoClass.class);

                                    if (hscAbove60.isChecked() && !hscAbove65.isChecked() && !hscBelow60.isChecked()) {
                                        if (studentInfoClass.getHscPercentage() >= 60 || studentInfoClass.getHscPercentage() == -1) {

                                            {
                                                if (diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }
                                                } else if (!diplomaAbove60.isChecked() && diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 65 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else if (!diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() < 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else {
                                                    studentList.add(studentInfoClass);
                                                }
                                            }

                                        }
                                    } else if (!hscAbove60.isChecked() && hscAbove65.isChecked() && !hscBelow60.isChecked()) {
                                        if (studentInfoClass.getHscPercentage() >= 65 || studentInfoClass.getHscPercentage() == -1) {
                                            {
                                                if (diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }
                                                } else if (!diplomaAbove60.isChecked() && diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 65 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else if (!diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() < 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else {
                                                    studentList.add(studentInfoClass);
                                                }
                                            }
                                        }

                                    } else if (!hscAbove60.isChecked() && !hscAbove65.isChecked() && hscBelow60.isChecked()) {
                                        if (studentInfoClass.getHscPercentage() < 60 || studentInfoClass.getHscPercentage() == -1) {
                                            {
                                                if (diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }
                                                } else if (!diplomaAbove60.isChecked() && diplomaAbove65.isChecked() && !diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() >= 65 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else if (!diplomaAbove60.isChecked() && !diplomaAbove65.isChecked() && diplomaBelow60.isChecked()) {
                                                    if (studentInfoClass.getDiplomaPercentage() < 60 || studentInfoClass.getDiplomaPercentage() == -1) {
                                                        studentList.add(studentInfoClass);
                                                    }

                                                } else {
                                                    studentList.add(studentInfoClass);
                                                }
                                            }
                                        }

                                    } else {
                                        studentList.add(studentInfoClass);
                                    }
                                }

                                studentFilterAdapter.notifyDataSetChanged();
                            }


                            Log.d(TAG, "onEvent: list-----" + studentList.size() + " " + studentList);
                            Log.d(TAG, "onEvent: list-----" + list.size() + " " + list);
                            if (studentList.isEmpty()) {
                                studentList.clear();
                                studentFilterAdapter = new StudentFilterAdapter(studentList, StudentListActivity.this);
                                recyclerView.setAdapter(studentFilterAdapter);
                                // setVerifiedDropdownInitial();
                            }
                            setUpEmptyLayouts(studentList.size());

                        }
                    });
                }

                bottomSheetDialog.hide();

                progressBar.setVisibility(View.GONE);
            }

        });


        bottomSheetDialog.show();
    }

    private void setupSearchRecyclerView() {
        progressBar.setVisibility(View.VISIBLE);
        studentList.clear();
        studentFilterAdapter = new StudentFilterAdapter(studentList, StudentListActivity.this);
        recyclerView.setAdapter(studentFilterAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
        //Query
//        Query query = firestore.collection("Students Information");
//        //Recycler options
//        FirestoreRecyclerOptions<StudentInfoClass> options =
//                new FirestoreRecyclerOptions.Builder<StudentInfoClass>()
//                        .setQuery(query, StudentInfoClass.class)
//                        .build();
//
//        searchAdapter = new SearchAdapterClass(options, SearchActivity.this);
//        recyclerView.setAdapter(searchAdapter);

        Query query = firestore.collection("Students Information").whereEqualTo("verified", true);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: " + error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        studentList.add(dc.getDocument().toObject(StudentInfoClass.class));
                        Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                    }
                    studentFilterAdapter.notifyDataSetChanged();

                }
                if (studentList.isEmpty()) {
                    // setVerifiedDropdownInitial();
                }
                setUpEmptyLayouts(studentList.size());

            }
        });
        progressBar.setVisibility(View.GONE);
    }


    private void fillAllRecords() {
        String mode = "name";
        //Query
        progressBar.setVisibility(View.VISIBLE);
        Query query = firestore.collection("Students Information").whereEqualTo("verified", true);

        studentList.clear();

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: " + error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        studentList.add(dc.getDocument().toObject(StudentInfoClass.class));
                        Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                    }
                    studentFilterAdapter.notifyDataSetChanged();

                }
                if (studentList.isEmpty()) {
                    // setVerifiedDropdownInitial();
                }
                setUpEmptyLayouts(studentList.size());

            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        studentFilterAdapter.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "results"+data.getIntExtra("position",0), Toast.LENGTH_SHORT).show();
        //for changing position of objects in adapter
        studentList.set(data.getIntExtra("position", 0), (StudentInfoClass) data.getSerializableExtra("studentDetails"));
        studentFilterAdapter.notifyItemChanged(data.getIntExtra("position", 0));
    }

    private void processSearch(String s) {
        String mode = "name";
        //Query
        Query query = firestore.collection("Students Information").whereEqualTo("verified", true).orderBy(mode).startAt(s).endAt(s + "\uf8ff");

        studentList.clear();

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: " + error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        studentList.add(dc.getDocument().toObject(StudentInfoClass.class));
                        Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                    }
                    studentFilterAdapter.notifyDataSetChanged();

                }
                if (studentList.isEmpty()) {
                    // setVerifiedDropdownInitial();
                }
                setUpEmptyLayouts(studentList.size());

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  searchAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // searchAdapter.stopListening();
    }

    private void initFields() {
        recyclerView = findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // branch = findViewById(R.id.branch_autocomplete_textview);
        filterButton = findViewById(R.id.filter_button);
        downloadButton = findViewById(R.id.download_button);
        searchButton = findViewById(R.id.search_button);
        searchView = findViewById(R.id.search_view);
        backButton = findViewById(R.id.back_button);
        noDataLayout = findViewById(R.id.no_data);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setUpEmptyLayouts(int i) {
        if (i == 0) {
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            noDataLayout.setVisibility(View.GONE);
        }
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }


}
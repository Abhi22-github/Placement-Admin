package com.roaaserver.placementadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.roaaserver.placementadmin.Adpters.CompanyAdapter;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;
import com.roaaserver.placementadmin.Models.TokenClass;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;


    private LinearLayout student, message, search, verification;
    private MaterialButton addCampus;
    private LinearLayout companyCardView;
    private ImageButton optionButton, profileButton, consoleButton;
    private RecyclerView companyRecyclerView;


    private CompanyAdapter companyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();

        initFirebase();

        setConsoleVisibility();

        saveToken();

        addCampus.setOnClickListener(view -> sendToCreateCompanyDetails());

        student.setOnClickListener(view -> sendToStudentList());
        message.setOnClickListener(view -> sendToMessageList());
        search.setOnClickListener(view -> sendToSearchActivity());
        verification.setOnClickListener(view -> sendToVerificationActivity());
        //  companyCardView.setOnClickListener(view -> sendToCompanyDetails());
        // optionButton.setOnClickListener(view -> displayPopupMenu());

        profileButton.setOnClickListener(view -> sendToProfileActivity());

        consoleButton.setOnClickListener(view -> sendToConsoleActivity());

    }

    private void setConsoleVisibility() {
        firestore.collection("Admins").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                AdminDetailsClass adminDetailsClass = documentSnapshot.toObject(AdminDetailsClass.class);
                if (adminDetailsClass.isAdmin()) {
                    consoleButton.setVisibility(View.VISIBLE);
                } else {
                    consoleButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void sendToProfileActivity() {
        Intent userNameIntent = new Intent(MainActivity.this, ProfileActivity.class);
        //userNameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userNameIntent);
        // finish();
    }


    private void sendToSearchActivity() {
        Intent userNameIntent = new Intent(MainActivity.this, RequestActivity.class);
        //userNameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userNameIntent);
        // finish();
    }


    private void CompanyRecyclerAdapter() {
        ArrayList<CompanyDetailsClass> companyList = new ArrayList<CompanyDetailsClass>();
        companyAdapter = new CompanyAdapter(companyList, this);

        companyRecyclerView.setAdapter(companyAdapter);

        companyRecyclerView.setHasFixedSize(true);
        companyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection("Company Details")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "onEvent: " + error.getMessage());
                            return;
                        }


                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                companyList.add(dc.getDocument().toObject(CompanyDetailsClass.class));
                                Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                            }

                            companyAdapter.notifyDataSetChanged();

                        }

                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CompanyRecyclerAdapter();
            }
        });

    }

//    private void displayPopupMenu() {
//        PopupMenu popupMenu = new PopupMenu(MainActivity.this, optionButton);
//        // Inflating popup menu from popup_menu.xml file
//        popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
//
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                int id = menuItem.getItemId();
//                switch (id) {
//                    case R.id.profile:
//                        sendUserToProfileEditActivity();
//                        return true;
//                    case R.id.console:
//                        sendToConsoleActivity();
//                        return true;
//                    case R.id.sign_out:
//                        mAuth.signOut();
//                        sendUserToAdminLogin();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
//        // Showing the popup menu
//        popupMenu.show();
//    }

//    private void sendUserToProfileEditActivity() {
//        Intent Intent = new Intent(MainActivity.this, ProfileEditActivity.class);
//        startActivity(Intent);
//    }
//
//    private void sendUserToAdminLogin() {
//        Intent Intent = new Intent(MainActivity.this, AdminLogin.class);
//        Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent);
//        finish();
//    }


    private void sendToVerificationActivity() {
        Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
        //userNameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // finish();
    }

    private void sendToStudentList() {
        Intent intent = new Intent(MainActivity.this, StudentListActivity.class);
        //userNameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // finish();
    }

    private void sendToMessageList() {
        Intent userNameIntent = new Intent(MainActivity.this, MessageActivity.class);
        //userNameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userNameIntent);
        // finish();
    }

    private void sendToConsoleActivity() {
        Intent userNameIntent = new Intent(MainActivity.this, ConsoleActivity.class);
        //userNameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userNameIntent);
        // finish();
    }

    private void sendToCreateCompanyDetails() {
        Intent userNameIntent = new Intent(MainActivity.this, CreateCompanyDetailsActivity.class);
        //userNameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userNameIntent);
        // finish();
    }


    private void initFields() {
        student = findViewById(R.id.student);
        message = findViewById(R.id.message);
        search = findViewById(R.id.search);
        verification = findViewById(R.id.verifications);
        addCampus = findViewById(R.id.add_campus);
        //  companyCardView = findViewById(R.id.company_cardview);
        optionButton = findViewById(R.id.option_menu);
        companyRecyclerView = findViewById(R.id.company_recyclerview);
        profileButton = findViewById(R.id.profile);
        consoleButton = findViewById(R.id.console);

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void saveToken() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("avcoe", 0);
        TokenClass tokenClass = new TokenClass(sharedPreferences.getString("token", null));

        firestore.collection("Tokens").document(mAuth.getCurrentUser().getUid()).
                set(tokenClass);
    }
}
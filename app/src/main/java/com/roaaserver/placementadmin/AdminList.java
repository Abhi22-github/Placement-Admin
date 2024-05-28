package com.roaaserver.placementadmin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.roaaserver.placementadmin.Adpters.AdminVerifiedAdapter;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;
import com.roaaserver.placementadmin.Other.FirestoreCallback;

import java.util.ArrayList;

public class AdminList extends AppCompatActivity {
    private static final String TAG = "AdminList";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;


    private ImageButton backButton;
    private RecyclerView adminVerifiedRecyclerView;
    private AdminVerifiedAdapter adminVerifiedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        initFields();

        initFirebase();

        backButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();
        adminVerifiedRecycleAdapter();
    }

    private void adminVerifiedRecycleAdapter() {
        ArrayList<AdminDetailsClass> adminList = new ArrayList<AdminDetailsClass>();
        adminVerifiedAdapter = new AdminVerifiedAdapter(adminList, this);
        adminList.clear();
        adminVerifiedRecyclerView.setAdapter(adminVerifiedAdapter);

        adminVerifiedRecyclerView.setHasFixedSize(true);
        adminVerifiedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection("Users").whereEqualTo("verified", true).whereEqualTo("mainAdmin",false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "onEvent: " + error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                adminList.add(dc.getDocument().toObject(AdminDetailsClass.class));
                                Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                            }

                            adminVerifiedAdapter.notifyDataSetChanged();

                        }

                    }
                });


    }

    private void initFields() {

        backButton = findViewById(R.id.back_button);
        adminVerifiedRecyclerView = findViewById(R.id.admin_verified_recycler_view);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

//    @Override
//    public void markVerifiedOrUnverified(AdminDetailsClass adminDetailsClass) {
//        firestore.collection("Users").document(adminDetailsClass.getAdminID())
//                .update("verified", false).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    onStart();
//                } else {
//                    Log.d(TAG, "onComplete: " + task.getException());
//                }
//            }
//        });
//    }
}
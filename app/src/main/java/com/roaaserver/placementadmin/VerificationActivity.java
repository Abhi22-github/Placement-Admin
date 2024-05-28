package com.roaaserver.placementadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.roaaserver.placementadmin.Adpters.StudentAdapter;
import com.roaaserver.placementadmin.Models.StudentDetailsClass;
import com.roaaserver.placementadmin.Models.StudentInfoClass;

import java.util.ArrayList;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "VerificationActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private ImageButton backButton;
    private RecyclerView recyclerView;
    private LinearLayout noDataLayout;

    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initFields();
        initFirebase();
        backButton.setOnClickListener(view -> onBackPressed());

        studentRecyclerView();
    }

    private void studentRecyclerView() {

        ArrayList<StudentInfoClass> studentDetailsList = new ArrayList<StudentInfoClass>();
        studentAdapter = new StudentAdapter(studentDetailsList, this);
        studentDetailsList.clear();
        recyclerView.setAdapter(studentAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection("Students Information").whereEqualTo("verified", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "onEvent: " + error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                studentDetailsList.add(dc.getDocument().toObject(StudentInfoClass.class));
                                Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(StudentDetailsClass.class));
                            }

                            studentAdapter.notifyDataSetChanged();

                        }
                        if(studentDetailsList.isEmpty()){
                            noDataLayout.setVisibility(View.VISIBLE);
                        }else{
                            noDataLayout.setVisibility(View.GONE);
                        }

                    }
                });

    }


    private void initFields() {
        backButton = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.student_recycler_view);
        noDataLayout = findViewById(R.id.no_data);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }
}
package com.roaaserver.placementadmin.StudentVerificationFragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.roaaserver.placementadmin.R;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;


    private RecyclerView recyclerView;

    private StudentAdapter studentAdapter;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);
        Log.d(TAG, "onCreateView: initialted");
        initFields();
        initFirebase();

        studentRecyclerView();
        return view;
    }

    private void studentRecyclerView() {

        ArrayList<StudentInfoClass> studentDetailsList = new ArrayList<StudentInfoClass>();
        studentAdapter = new StudentAdapter(studentDetailsList, getContext());
        studentDetailsList.clear();
        recyclerView.setAdapter(studentAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firestore.collection("Students Information").whereNotEqualTo("status", 0)
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

                    }
                });

    }


    private void initFields() {
        recyclerView = view.findViewById(R.id.student_recycler_view);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

}
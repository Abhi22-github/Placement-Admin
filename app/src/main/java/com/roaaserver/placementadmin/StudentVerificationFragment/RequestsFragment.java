package com.roaaserver.placementadmin.StudentVerificationFragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.roaaserver.placementadmin.Holders.RequestViewHolder;
import com.roaaserver.placementadmin.Models.RequestInfoClass;
import com.roaaserver.placementadmin.R;

public class RequestsFragment extends Fragment {
    private static final String TAG = "RequestsFragment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;


    private RecyclerView recyclerView;

    private FirestoreRecyclerAdapter firestoreRequestsRecyclerAdapter;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initFields();
        initFirebase();

        setUpRequestsRecyclerView();
        return view;
    }




    private void initFields() {
        recyclerView = view.findViewById(R.id.student_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        firestoreRequestsRecyclerAdapter.startListening();
    }

    private void setUpRequestsRecyclerView() {


        //Query
        Query query = firestore.collection("Requests").orderBy("time", Query.Direction.ASCENDING);
        //Recycler options
        FirestoreRecyclerOptions<RequestInfoClass> options = new FirestoreRecyclerOptions.Builder<RequestInfoClass>()
                .setQuery(query, RequestInfoClass.class)
                .build();

        firestoreRequestsRecyclerAdapter = new FirestoreRecyclerAdapter<RequestInfoClass, RequestViewHolder>(options) {
            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout_pending, parent, false);
                return new RequestViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull RequestInfoClass model) {
//                             holder.receiverId.setText(model.getId());
//                holder.receiverName.setText(model.getName());
//                holder.requestLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), RequestDetailActivity.class);
//                        intent.putExtra("requestDetails", model);
//                        startActivity(intent);
//                    }
//                });
            }


            @Override
            public void onDataChanged() {
                super.onDataChanged();
                //recyclerView.smoothScrollToPosition(messageRecyclerView.getBottom());
            }
        };

        recyclerView.setAdapter(firestoreRequestsRecyclerAdapter);

    }
}
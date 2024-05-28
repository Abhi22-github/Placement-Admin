package com.roaaserver.placementadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.roaaserver.placementadmin.Adpters.StudentAdapter;
import com.roaaserver.placementadmin.Holders.RequestViewHolder;
import com.roaaserver.placementadmin.Models.RequestInfoClass;

import java.util.Arrays;

public class RequestActivity extends AppCompatActivity {
    private static final String TAG = "RequestActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private ImageButton backButton;
    private RecyclerView pendingRecyclerView,completedRecyclerView;
    private LinearLayout noDataLayout;

    private StudentAdapter studentAdapter;
    private RelativeLayout pendingRequestLayout,completedRequestLayout;
    private FirestoreRecyclerAdapter pendingRequestsRecyclerAdapter,completedRequestRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        initFields();
        initFirebase();
        backButton.setOnClickListener(view -> onBackPressed());

        setUpPendingRequestsRecyclerView();
        setUpCompletedRequestsRecyclerView();
    }

    private void setUpPendingRequestsRecyclerView() {


        //Query
        Query query = firestore.collection("Requests").whereEqualTo("status",1).orderBy("time", Query.Direction.DESCENDING);
        //Recycler options
        FirestoreRecyclerOptions<RequestInfoClass> options = new FirestoreRecyclerOptions.Builder<RequestInfoClass>()
                .setQuery(query, RequestInfoClass.class)
                .build();

        pendingRequestsRecyclerAdapter = new FirestoreRecyclerAdapter<RequestInfoClass, RequestViewHolder>(options) {
            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout_pending, parent, false);
                return new RequestViewHolder(view);


            }


            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull RequestInfoClass model) {

                holder.name.setText(model.getName());
                holder.branch.setText(model.getDepartment());
                holder.modifyCount.setText(String.valueOf(model.getModifyCount()));
                Log.d(TAG, "onBindViewHolder: 000000000000000000"+ model.getName());
                holder.requestLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RequestActivity.this, RequestDetailActivity.class);
                        intent.putExtra("requestDetails", model);
                        startActivity(intent);
                    }
                });
            }


            @Override
            public void onDataChanged() {
                super.onDataChanged();
                //pendingRecyclerView.smoothScrollToPosition(messageRecyclerView.getBottom());
            }
        };

        pendingRecyclerView.setAdapter(pendingRequestsRecyclerAdapter);

    }

    private void setUpCompletedRequestsRecyclerView() {


        //Query
        Query query = firestore.collection("Requests").whereIn("status", Arrays.asList(0,2)).orderBy("time", Query.Direction.DESCENDING);
        //Recycler options
        FirestoreRecyclerOptions<RequestInfoClass> options = new FirestoreRecyclerOptions.Builder<RequestInfoClass>()
                .setQuery(query, RequestInfoClass.class)
                .build();

        completedRequestRecyclerAdapter = new FirestoreRecyclerAdapter<RequestInfoClass, RequestViewHolder>(options) {
            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout_completed, parent, false);
                return new RequestViewHolder(view);


            }


            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull RequestInfoClass model) {
                holder.name.setText(model.getName());
                holder.branch.setText(model.getDepartment());
                holder.modifyCount.setText(String.valueOf(model.getModifyCount()));
                Log.d(TAG, "onBindViewHolder: 000000000000000000"+ model.getName());
                holder.requestLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RequestActivity.this, RequestDetailActivity.class);
                        intent.putExtra("requestDetails", model);
                        startActivity(intent);
                    }
                });
            }


            @Override
            public void onDataChanged() {
                super.onDataChanged();
                //pendingRecyclerView.smoothScrollToPosition(messageRecyclerView.getBottom());
            }
        };

        completedRecyclerView.setAdapter(completedRequestRecyclerAdapter);

    }



    private void initFields() {
        backButton = findViewById(R.id.back_button);
        pendingRecyclerView = findViewById(R.id.pending_recycler_view);
        completedRecyclerView = findViewById(R.id.completed_recycler_view);
        noDataLayout = findViewById(R.id.no_data);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        completedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pendingRequestsRecyclerAdapter.startListening();
        completedRequestRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pendingRequestsRecyclerAdapter.stopListening();
        completedRequestRecyclerAdapter.stopListening();
    }
}
package com.roaaserver.placementadmin.Adpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.roaaserver.placementadmin.Models.StudentInfoClass;
import com.roaaserver.placementadmin.R;
import com.roaaserver.placementadmin.StudentProfileActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapterClass extends FirestoreRecyclerAdapter<StudentInfoClass, SearchAdapterClass.ViewHolder> {
    Context mContext;

    public SearchAdapterClass(@NonNull FirestoreRecyclerOptions<StudentInfoClass> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull StudentInfoClass model) {
        if (!model.getImage().isEmpty()) {
            Picasso.get().load(model.getImage()).into(holder.profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            });
        }
        holder.branch.setText(model.getBranch());
        holder.name.setText(model.getName());
        holder.studentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentProfileActivity.class);
                intent.putExtra("studentDetails", model);
                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView name,branch;
        private LinearLayout studentLayout;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.student_profile_image);
            name = itemView.findViewById(R.id.student_name);
            studentLayout = itemView.findViewById(R.id.student_layout);
            progressBar = itemView.findViewById(R.id.progress_bar);
            branch = itemView.findViewById(R.id.branch);
        }
    }
}

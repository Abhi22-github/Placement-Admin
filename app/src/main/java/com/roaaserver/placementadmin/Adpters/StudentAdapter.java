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

import com.roaaserver.placementadmin.Models.StudentInfoClass;
import com.roaaserver.placementadmin.R;
import com.roaaserver.placementadmin.StudentProfileActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<StudentInfoClass> studentDetailsList = new ArrayList<>();
    private Context mContext;
    // private FirestoreCallback firestoreCallback;


    public StudentAdapter(List<StudentInfoClass> studentDetailsList, Context mContext) {
        this.mContext = mContext;
        this.studentDetailsList = studentDetailsList;
        //   firestoreCallback = (FirestoreCallback) mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.student_list_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!studentDetailsList.get(position).getImage().isEmpty()) {
            Picasso.get().load(studentDetailsList.get(position).getImage()).into(holder.profileImage, new Callback() {
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
        holder.branch.setText(studentDetailsList.get(position).getBranch());
        holder.name.setText(studentDetailsList.get(position).getName());
        holder.studentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentProfileActivity.class);
                intent.putExtra("studentDetails", studentDetailsList.get(position));
                intent.putExtra("from",2);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

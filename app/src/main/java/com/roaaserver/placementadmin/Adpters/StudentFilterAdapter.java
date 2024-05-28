package com.roaaserver.placementadmin.Adpters;

import android.app.Activity;
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

public class StudentFilterAdapter extends RecyclerView.Adapter<StudentFilterAdapter.ViewHolder> {
    private List<StudentInfoClass> studenList = new ArrayList<>();
    private Context mContext;
    //private FirestoreCallback firestoreCallback;

    public StudentFilterAdapter(List<StudentInfoClass> studenList, Context mContext) {
        this.studenList = studenList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public StudentFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_recycler_view, parent, false);
        return new StudentFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentFilterAdapter.ViewHolder holder, int position) {
        if (!studenList.get(position).getImage().isEmpty()) {

            Picasso.get().load(studenList.get(position).getImage()).into(holder.profileImage, new Callback() {
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
        holder.name.setText(studenList.get(position).getName());
        holder.branch.setText(studenList.get(position).getBranch());
        holder.studentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StudentProfileActivity.class);
                intent.putExtra("studentDetails", studenList.get(position));
                intent.putExtra("position", position);
                intent.putExtra("from",1);
                ((Activity)mContext).startActivityForResult(intent,11);
            }
        });
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){

    }

    @Override
    public int getItemCount() {
        return studenList.size();
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

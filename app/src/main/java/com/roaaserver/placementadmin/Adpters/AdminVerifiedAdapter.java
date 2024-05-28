package com.roaaserver.placementadmin.Adpters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.roaaserver.placementadmin.Other.FirestoreCallback;
import com.roaaserver.placementadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminVerifiedAdapter extends RecyclerView.Adapter<AdminVerifiedAdapter.ViewHolder> {

    private List<AdminDetailsClass> adminList = new ArrayList<>();
    private Context mContext;
    private FirestoreCallback firestoreCallback;


    public AdminVerifiedAdapter(List<AdminDetailsClass> AdminList, Context mContext) {
        this.mContext = mContext;
        this.adminList = AdminList;
        firestoreCallback = (FirestoreCallback) mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.admin_recycler_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //  holder.dropdown.setVisibility(View.GONE);
        AdminDetailsClass adminDetailsClass = adminList.get(position);
        holder.email.setText(adminDetailsClass.getEmail());
        Picasso.get().load(adminDetailsClass.getImage()).into(holder.adminImage, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
        holder.adminName.setText(adminDetailsClass.getUserName());
        holder.status.setImageResource(R.drawable.ic_verified_icon);
        //  holder.dropdownButton.setOnClickListener(view -> setAdminDropDown(holder));
        holder.adminLayout.setOnClickListener(view -> setAdminDropDown(holder));
        holder.unverifiedButton.setText("Revoke Verification");
        holder.unverifiedButton.setTextColor(mContext.getColor(R.color.red));
        // holder.unverifiedButton.setIconTint(ColorStateList.valueOf(mContext.getColor(R.color.yellow)));
        // holder.unverifiedButton.setIcon(mContext.getDrawable(R.drawable.ic_baseline_cancel_24));
        holder.unverifiedButton.setOnClickListener(view -> askUserForConfirmation(adminDetailsClass));

    }

    private void askUserForConfirmation(AdminDetailsClass adminDetailsClass) {

        new MaterialAlertDialogBuilder(mContext)
                .setTitle("Confirm ")
                .setMessage("Are you sure about this.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        firestoreCallback.markUnverified(adminDetailsClass);

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    private void setAdminDropDown(ViewHolder holder) {
        if (holder.adminButtons.getVisibility() == View.VISIBLE) {
            holder.adminButtons.setVisibility(View.GONE);
            // holder.dropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        } else {
            // holder.dropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            holder.adminButtons.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return adminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView adminImage;
        private TextView email;
        private ProgressBar progressBar;
        private ImageButton dropdownButton;
        private RelativeLayout adminLayout;
        private ImageButton dropdown;
        private LinearLayout adminButtons;
        private MaterialButton unverifiedButton;
        private TextView adminName;
        private ImageView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.admin_recycler_email);
            adminImage = itemView.findViewById(R.id.admin_recycler_admin_image);
            progressBar = itemView.findViewById(R.id.progress_bar);
            // dropdown = itemView.findViewById(R.id.details_dropdown);
            // dropdownButton = itemView.findViewById(R.id.details_dropdown);
            adminLayout = itemView.findViewById(R.id.admin_layout);
            unverifiedButton = itemView.findViewById(R.id.admin_verified_button);
            adminButtons = itemView.findViewById(R.id.admin_down_button);
            adminName = itemView.findViewById(R.id.admin_recycler_name);
            status = itemView.findViewById(R.id.status);
        }
    }
}

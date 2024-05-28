package com.roaaserver.placementadmin.Holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roaaserver.placementadmin.R;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "RequestViewHolder";
    public TextView name, branch, modifyCount;
    public LinearLayout requestLayout;

    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        branch = itemView.findViewById(R.id.branch);
        modifyCount = itemView.findViewById(R.id.modify_count);
        requestLayout = itemView.findViewById(R.id.requests_cardview);
    }

}

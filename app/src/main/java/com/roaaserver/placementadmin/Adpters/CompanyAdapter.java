package com.roaaserver.placementadmin.Adpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roaaserver.placementadmin.CompanyDetails;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;
import com.roaaserver.placementadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    private List<CompanyDetailsClass> companyList = new ArrayList<>();
    private Context mContext;


    public CompanyAdapter(List<CompanyDetailsClass> CompanyList, Context mContext) {
        this.mContext = mContext;
        this.companyList = CompanyList;
    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.company_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder holder, int position) {
        CompanyDetailsClass companyDetailsClass = companyList.get(position);
        holder.role.setText(companyDetailsClass.getRole());
        holder.name.setText(companyDetailsClass.getCompanyName());
        holder.salary.setText("₹"+companyDetailsClass.getSalary()+" LPA");
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(companyDetailsClass.getCompanyImage()).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });

        holder.companyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CompanyDetails.class);
                intent.putExtra("companyDetails", companyList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView role, name, salary;
        private ImageView image;
        private LinearLayout companyLayout;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            role = itemView.findViewById(R.id.company_recycler_role);
            name = itemView.findViewById(R.id.company_recycler_name);
            salary = itemView.findViewById(R.id.company_recycler_salary);
            image = itemView.findViewById(R.id.company_recycler_company_image);
            companyLayout = itemView.findViewById(R.id.company_layout);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}

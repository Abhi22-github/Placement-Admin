package com.roaaserver.placementadmin.Holders;

import android.app.Application;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roaaserver.placementadmin.R;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolder extends RecyclerView.ViewHolder {


    public TextView senderTv, receiverTv, senderName, receiverName;
    public RelativeLayout senderLayout, receiverLayout;
    public CircleImageView senderImage, receiverImage,finalImage;
    public TextView finalName, finalText,departmentName;

    public MessageViewHolder(@NonNull View itemView, boolean isSender) {
        super(itemView);

        senderName = itemView.findViewById(R.id.sender_name);
        receiverName = itemView.findViewById(R.id.receiver_name);
        senderTv = itemView.findViewById(R.id.sender_tv);
        senderLayout = itemView.findViewById(R.id.sender_layout);
        receiverTv = itemView.findViewById(R.id.receiver_tv);
        receiverLayout = itemView.findViewById(R.id.receiver_layout);
        senderImage = itemView.findViewById(R.id.sender_image);
        receiverImage = itemView.findViewById(R.id.receiver_image);
        finalName = itemView.findViewById(R.id.final_name);
        finalText = itemView.findViewById(R.id.final_text);
        finalImage = itemView.findViewById(R.id.final_circle_image_view);
        departmentName = itemView.findViewById(R.id.department_name);
    }

    public void setMessage(Application application, String message, Date time,
                           String senderUid, String name, String image, String department) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

//        if (currentUid.equals(senderUid)) {
//            senderLayout.setVisibility(View.VISIBLE);
//            receiverLayout.setVisibility(View.GONE);
//            senderTv.setText(message);
//            senderName.setText(name);
//            Picasso.get().load(image).into(senderImage);
//            setImage(image, senderImage);
//        } else {
//            senderLayout.setVisibility(View.GONE);
//            receiverLayout.setVisibility(View.VISIBLE);
//            receiverTv.setText(message);
//            receiverName.setText(name);
//            Picasso.get().load(image).into(receiverImage);
//            setImage(image, receiverImage);
//        }

        finalName.setText(name);
        finalText.setText(message);
        Picasso.get().load(image).into(finalImage);
departmentName.setText(department);

    }

    private void setImage(String image, CircleImageView imageView) {
        try {
            if (image == null || image.isEmpty()) {
                imageView.setImageResource(R.drawable.avcoe_logo);
            }
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.avcoe_logo);
        }
    }
}

package com.roaaserver.placementadmin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private RelativeLayout profileLayout, feedbackLayout, inviteFriendsLayout, rateLayout, libraryLayout, privacyLayout, agreementLayout;
    private MaterialButton signOutButton;
    private TextView name, email;
    private CircleImageView profileImage;
    private ImageButton backButton, profileButton, feedbackButton, inviteFriendsButton, rateButton, libraryButton, privacyButton, agreementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initFields();
        initFirebase();


        backButton.setOnClickListener(view -> onBackPressed());

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                sendUserToAdminLogin();
            }
        });

        profileLayout.setOnClickListener(view -> sendUserToProfileEditActivity());
        profileButton.setOnClickListener(view -> sendUserToProfileEditActivity());

        feedbackLayout.setOnClickListener(view -> sendEmail());
        feedbackButton.setOnClickListener(view -> sendEmail());

        inviteFriendsLayout.setOnClickListener(view -> inviteFriends());
        inviteFriendsButton.setOnClickListener(view -> inviteFriends());

        rateLayout.setOnClickListener(view -> rateUsMethod());
        rateButton.setOnClickListener(view -> rateUsMethod());

        libraryLayout.setOnClickListener(view -> sendToOpenSourceLibrary());
        libraryButton.setOnClickListener(view -> sendToOpenSourceLibrary());

        privacyLayout.setOnClickListener(view -> sendToPrivacyPolicy());
        privacyButton.setOnClickListener(view -> sendToPrivacyPolicy());

        agreementLayout.setOnClickListener(view -> sendToUserAgreementPage());
        agreementButton.setOnClickListener(view -> sendToUserAgreementPage());


    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpFields();
    }

    private void sendEmail() {

        Intent sendEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "services@avcoe.org")); // mention an email id here
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "subject"); //subject of the email
        sendEmail.putExtra(Intent.EXTRA_TEXT, "body"); //body of the email
        startActivity(Intent.createChooser(sendEmail, "Choose one"));
    }

    private void inviteFriends() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Placement");
            String shareMessage = "\nManage student data easily.\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void rateUsMethod() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    private void sendUserToProfileEditActivity() {
        Intent Intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
        startActivity(Intent);
    }

    private void sendToOpenSourceLibrary() {
    }

    private void sendToPrivacyPolicy() {
    }

    private void sendToUserAgreementPage() {
    }

    private void setUpFields() {
        firestore.collection("Admins").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                AdminDetailsClass adminDetailsClass = documentSnapshot.toObject(AdminDetailsClass.class);
                Picasso.get().load(adminDetailsClass.getImage()).into(profileImage);
                name.setText(adminDetailsClass.getUserName());
                email.setText(adminDetailsClass.getEmail());

            }
        });
    }

    private void sendUserToAdminLogin() {
        Intent Intent = new Intent(ProfileActivity.this, AdminLogin.class);
        Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent);
        finish();
    }


    private void initFields() {
        profileButton = findViewById(R.id.profile_button);
        profileLayout = findViewById(R.id.profile_relative_layout);
        signOutButton = findViewById(R.id.sign_out_button);
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        backButton = findViewById(R.id.back_button);

        feedbackLayout = findViewById(R.id.feedback_layout);
        feedbackButton = findViewById(R.id.feedback_button);

        inviteFriendsLayout = findViewById(R.id.invite_friends_layout);
        inviteFriendsButton = findViewById(R.id.invite_button);

        rateLayout = findViewById(R.id.rate_the_app_layout);
        rateButton = findViewById(R.id.rate_button);

        libraryLayout = findViewById(R.id.open_source_library);
        libraryButton = findViewById(R.id.library_button);

        privacyLayout = findViewById(R.id.privacy_layout);
        privacyButton = findViewById(R.id.privacy_button);

        agreementLayout = findViewById(R.id.agreement_layout);
        agreementButton = findViewById(R.id.agreement_button);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }
}
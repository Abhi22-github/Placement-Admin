package com.roaaserver.placementadmin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roaaserver.placementadmin.Adpters.AdminPendingAdapter;
import com.roaaserver.placementadmin.Adpters.AdminVerifiedAdapter;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.roaaserver.placementadmin.Models.CarousalImageClass;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;
import com.roaaserver.placementadmin.Other.FirestoreCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.shahabazimi.instagrampicker.InstagramPicker;

public class ConsoleActivity extends AppCompatActivity implements FirestoreCallback {
    private static final String TAG = "ConsoleActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private FirestoreRecyclerAdapter firestoreCarousalRecyclerAdapter;


    // private ConstraintLayout bottomSheetLayout;
    //  private CoordinatorLayout bottomSheetContainer;
    //  private BottomSheetBehavior bottomSheetBehavior;
    //  private MaterialButton addMemberButton, bottomSheetSaveButton;
    private ImageButton backButton;
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText email, password;
    private RecyclerView adminPendingRecyclerView;
    private AdminPendingAdapter adminPendingAdapter;
    private RelativeLayout addMemeberLayout;
    private MaterialButton addCarousalImagesButton;
    private RecyclerView carousalRecyclerView;
    private RecyclerView adminVerifiedRecyclerView;
    private AdminVerifiedAdapter adminVerifiedAdapter;
    private RelativeLayout pendingLayout, verifiedLayout;
    private ImageButton pendingDropdownButton, verifiedDropdownButton;
    private RelativeLayout dotLayout;
    //private TextView bottomSheetHeading;

    // private String id;
    private Uri imageUri;
    private String downloadUri;
    private List<String> carousalIDImages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);

        initFields();

        initFirebase();

        //setAddMemberButton();

//        addMemberButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bottomSheetHeading.setText("Add Member");
//                email.getText().clear();
//                password.getText().clear();
//                bottomSheetContainer.setVisibility(View.VISIBLE);
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });


        backButton.setOnClickListener(view -> onBackPressed());

        //  allAdmins.setOnClickListener(view -> senUserToAdminList());

        // bottomSheetSaveButton.setOnClickListener(view -> checkCredentials());

        addCarousalImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstagramPicker instagramPicker = new InstagramPicker(ConsoleActivity.this);
                instagramPicker.show(14, 9, address -> {
                    imageUri = Uri.parse(address);
                    UploadImageToStorage();

                });
            }
        });

        setUpCatalogRecyclerView();

        setPendingDropdownInitial();

        setVerifiedDropdownInitial();


        pendingLayout.setOnClickListener(view -> setPendingDropdown());
        pendingDropdownButton.setOnClickListener(view -> setPendingDropdown());

        verifiedLayout.setOnClickListener(view -> setVerifiedDropdown());
        verifiedDropdownButton.setOnClickListener(view -> setVerifiedDropdown());
    }

    private void setVerifiedDropdownInitial() {
        if (adminVerifiedRecyclerView.getVisibility() == View.GONE) {
            verifiedDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        } else {
            verifiedDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        }
    }

    private void setPendingDropdownInitial() {
        if (adminPendingRecyclerView.getVisibility() == View.GONE) {
            pendingDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        } else {
            pendingDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        }
    }

    private void setPendingDropdown() {
        if (adminPendingRecyclerView.getVisibility() == View.GONE) {
            pendingDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            adminPendingRecyclerView.setVisibility(View.VISIBLE);
        } else {
            pendingDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            adminPendingRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setVerifiedDropdown() {
        if (adminVerifiedRecyclerView.getVisibility() == View.GONE) {
            verifiedDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            adminVerifiedRecyclerView.setVisibility(View.VISIBLE);
        } else {
            verifiedDropdownButton.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            adminVerifiedRecyclerView.setVisibility(View.GONE);
        }
    }


    private void UploadImageToStorage() {
        //getting date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date).toString();
        StorageReference fileRef = storageReference.child("Carousal Images").child(mAuth.getCurrentUser().getUid()).
                child(mAuth.getCurrentUser().getUid() + "_" + time + ".jpg");

        if (imageUri != null) {
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //here you can choose quality factor in third parameter(ex. quality factor = 25)
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] fileInBytes = baos.toByteArray();

            Log.d(TAG, "uploadImageToDatabaseStorage: 2");
            fileRef.putBytes(fileInBytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "uploadImageToDatabaseStorage: 3");
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "uploadImageToDatabaseStorage: 4");
                                downloadUri = uri.toString();
                                Log.d(TAG, "onSuccess: " + downloadUri);
                                saveCarousalImageInDatabase(downloadUri, time);
                            }
                        });
                    } else {
                        Log.d(TAG, "onComplete: error while saving images");
                    }
                }
            });
        } else {

        }

    }

    private void saveCarousalImageInDatabase(String downloadUri, String time) {
        ;

        CarousalImageClass carousalImageClass = new CarousalImageClass();
        carousalImageClass.setCatalogImage(downloadUri);
        carousalImageClass.setCatalogImageDate(time);

        firestore.collection("Carousal").document()
                .set(carousalImageClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: catalog image is saved");
                } else {

                }
            }
        });

    }

    private void setUpCatalogRecyclerView() {
        //Query
        Query query = firestore.collection("Carousal").orderBy("catalogImageDate");
        //Recycler options
        FirestoreRecyclerOptions<CarousalImageClass> options = new FirestoreRecyclerOptions.Builder<CarousalImageClass>()
                .setQuery(query, CarousalImageClass.class)
                .build();

        firestoreCarousalRecyclerAdapter = new FirestoreRecyclerAdapter<CarousalImageClass, CarousalViewHolder>(options) {
            @NonNull
            @Override
            public CarousalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousal_item_recycler_view, parent, false);
                return new CarousalViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CarousalViewHolder holder, int position, @NonNull CarousalImageClass model) {
                try {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    Picasso.get().load(model.getCatalogImage()).into(holder.carousalImage,
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    holder.progressBar.setVisibility(View.GONE);
                                }
                            });

                } catch (Exception e) {

                }
//                holder.catalogImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //getting document id
//                        String documentId = getSnapshots().getSnapshot(position).getId();
//
//                        Pair[] pairs = new Pair[1];
//                        pairs[0] = new Pair<View, String>(holder.catalogImage, "image");
//                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(), pairs);
//                        Intent in = new Intent(getContext(), FullImageViewActivity.class);
//                        in.putExtra("image", model.getCatalogImage());
//                        in.putExtra("date", model.getCatalogImageDate());
//                        in.putExtra("id", documentId);
//                        startActivity(in, activityOptions.toBundle());
//                    }
//                });
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(ConsoleActivity.this, LinearLayoutManager.HORIZONTAL, false);
        carousalRecyclerView.setLayoutManager(layoutManager);
        carousalRecyclerView.setAdapter(firestoreCarousalRecyclerAdapter);
    }


    private class CarousalViewHolder extends RecyclerView.ViewHolder {
        private ImageView carousalImage;
        private ProgressBar progressBar;

        public CarousalViewHolder(@NonNull View itemView) {
            super(itemView);
            carousalImage = itemView.findViewById(R.id.catalog_recycler_image);
            progressBar = itemView.findViewById(R.id.catalog_image_progress_bar);
        }
    }

//    private void senUserToAdminList() {
//        Intent intent = new Intent(ConsoleActivity.this, AdminList.class);
//        startActivity(intent);
//    }

//    private void setAddMemberButton() {
//        DocumentReference documentReference = firestore.collection("Users").document(mAuth.getCurrentUser().getUid());
//
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    AdminDetailsClass adminDetailsClass = document.toObject(AdminDetailsClass.class);
//
//                    if (adminDetailsClass.isMainAdmin()) {
//                        //Toast.makeText(ConsoleActivity.this, "true", Toast.LENGTH_SHORT).show();
//                        addMemeberLayout.setVisibility(View.VISIBLE);
//                        addMemberButton.setEnabled(true);
//                    } else {
//                        addMemeberLayout.setVisibility(View.GONE);
//                        addMemberButton.setEnabled(false);
//                    }
//
//
//                } else {
//
//                }
//
//
//            }
//        });
//
//
//    }

    @Override
    protected void onStart() {
        super.onStart();
        adminPendingRecycleAdapter();
        adminVerifiedRecycleAdapter();
        firestoreCarousalRecyclerAdapter.startListening();

    }

    private void adminVerifiedRecycleAdapter() {
        ArrayList<AdminDetailsClass> adminList = new ArrayList<AdminDetailsClass>();
        adminVerifiedAdapter = new AdminVerifiedAdapter(adminList, this);
        adminList.clear();
        adminVerifiedRecyclerView.setAdapter(adminVerifiedAdapter);

        adminVerifiedRecyclerView.setHasFixedSize(true);
        adminVerifiedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection("Admins").whereEqualTo("verified", true).whereEqualTo("admin", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "onEvent: " + error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                adminList.add(dc.getDocument().toObject(AdminDetailsClass.class));
                                Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                            }

                            adminVerifiedAdapter.notifyDataSetChanged();

                        }
                        if(adminList.isEmpty()){
                            adminVerifiedRecyclerView.setVisibility(View.GONE);
                            setVerifiedDropdownInitial();
                        }

                    }
                });


    }

//    private void checkCredentials() {
//        boolean checkAll = true;
//        if (email.getText().toString().isEmpty()) {
//            emailLayout.setError("Fields can't be Empty");
//            checkAll = false;
//        }
//        if (password.getText().toString().isEmpty()) {
//            passwordLayout.setError("Fields can't be Empty");
//            checkAll = false;
//        }
//        if (checkAll) {
//          //  createUserAccountInDatabaseAndStoreData();
//        }
//    }

//    private void createUserAccountInDatabaseAndStoreData() {
//
//        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            AdminDetailsClass adminDetailsClass = new AdminDetailsClass();
//                            adminDetailsClass.setAdminID(task.getResult().getUser().getUid());
//                            adminDetailsClass.setEmail(email.getText().toString());
//                            adminDetailsClass.setPassword(password.getText().toString());
//                            adminDetailsClass.setAdmin(true);
//                            adminDetailsClass.setMainAdmin(false);
//                            firestore.collection("Users").document(task.getResult().getUser().getUid())
//                                    .set(adminDetailsClass).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        //successfully addded
//                                        //show dialog
//                                        email.getText().clear();
//                                        password.getText().clear();
//                                        hideKeyboard(ConsoleActivity.this);
//                                        bottomSheetContainer.setVisibility(View.GONE);
//
//                                        onBackPressed();
//
//
//                                    } else {
//                                        Log.d(TAG, "onComplete: error while saving" + task.getException());
//                                        Toast.makeText(ConsoleActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        } else {
//                            //Toast.makeText(ConsoleActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            //Log.d(TAG, "onComplete: " + task.getException());
//                            mAuth.fetchSignInMethodsForEmail(email.getText().toString())
//                                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task1) {
//                                            Log.d(TAG, "onComplete: email is present");
//                                            HashMap<String, Object> map = new HashMap<>();
//                                            map.put("email", email.getText().toString());
//                                            map.put("password", password.getText().toString());
//                                            firestore.collection("Users").document(id)
//                                                    .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    //successfully addded
//                                                    //show dialog
//                                                    email.getText().clear();
//                                                    password.getText().clear();
//                                                    hideKeyboard(ConsoleActivity.this);
//                                                    bottomSheetContainer.setVisibility(View.GONE);
//                                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                                                    onStart();
//                                                }
//                                            });
//
//
//                                        }
//                                    });
//
//
//                        }
//
//
//                    }
//                });
//
//
//    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }

    private void initFields() {
        //  bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        //  bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        //  bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        //   bottomSheetContainer = findViewById(R.id.bottom_sheet_container);

        addCarousalImagesButton = findViewById(R.id.add_carousal_image_button);
        carousalRecyclerView = findViewById(R.id.carousal_images_recycler);

        pendingLayout = findViewById(R.id.pending_verification_layout);
        pendingDropdownButton = findViewById(R.id.pending_verification_dropdown);

        verifiedDropdownButton = findViewById(R.id.completed_verification_dropdown);
        verifiedLayout = findViewById(R.id.completed_verification_layout);

        backButton = findViewById(R.id.back_button);
        dotLayout = findViewById(R.id.content_present_symbol);
        //   addMemberButton = findViewById(R.id.add_member_button);
        //  addMemeberLayout = findViewById(R.id.add_member_layout);
        //   bottomSheetSaveButton = findViewById(R.id.bottom_sheet_save_button);

        //   bottomSheetHeading = findViewById(R.id.bottom_heading);

        emailLayout = findViewById(R.id.bottom_sheet_email_layout);
        email = findViewById(R.id.bottom_sheet_email_text);

        passwordLayout = findViewById(R.id.bottom_sheet_password_layout);
        password = findViewById(R.id.bottom_sheet_password_text);

        adminPendingRecyclerView = findViewById(R.id.admin_unverified_recycler_view);
        adminVerifiedRecyclerView = findViewById(R.id.admin_verified_recycler_view);

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void adminPendingRecycleAdapter() {
        ArrayList<AdminDetailsClass> adminList = new ArrayList<AdminDetailsClass>();
        adminPendingAdapter = new AdminPendingAdapter(adminList, this);
        adminList.clear();
        adminPendingRecyclerView.setAdapter(adminPendingAdapter);

        adminPendingRecyclerView.setHasFixedSize(true);
        adminPendingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore.collection("Admins").whereEqualTo("verified", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, "onEvent: " + error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                adminList.add(dc.getDocument().toObject(AdminDetailsClass.class));
                                Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(CompanyDetailsClass.class));
                            }

                            adminPendingAdapter.notifyDataSetChanged();

                        }
                        if(adminList.isEmpty()){
                            adminPendingRecyclerView.setVisibility(View.GONE);
                            setPendingDropdownInitial();
                            dotLayout.setVisibility(View.GONE);
                        }else {
                            dotLayout.setVisibility(View.VISIBLE);
                        }

                    }
                });


    }


    @Override
    public void markVerified(AdminDetailsClass adminDetailsClass) {
        firestore.collection("Admins").document(adminDetailsClass.getAdminID())
                .update("verified", true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onStart();
                } else {
                    Log.d(TAG, "onComplete: " + task.getException());
                }
            }
        });

    }

    @Override
    public void markUnverified(AdminDetailsClass adminDetailsClass) {
        firestore.collection("Admins").document(adminDetailsClass.getAdminID())
                .update("verified", false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onStart();
                } else {
                    Log.d(TAG, "onComplete: " + task.getException());
                }
            }
        });


    }
}
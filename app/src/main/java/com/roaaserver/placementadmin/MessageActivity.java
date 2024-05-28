package com.roaaserver.placementadmin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.roaaserver.placementadmin.Holders.MessageViewHolder;
import com.roaaserver.placementadmin.Models.AdminDetailsClass;
import com.roaaserver.placementadmin.Models.MessageClass;
import com.roaaserver.placementadmin.Nofifications.FcmNotificationsSender;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private static String userMail;
    private String senderUid, name, image, department;

    private ImageButton backButton;
    private EditText messageText;
    private RelativeLayout sendButton;
    private RecyclerView messageRecyclerView;
    private CheckBox checkbox;

    private FirestoreRecyclerAdapter firestoreMessageRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initFields();

        initFirebase();

        getCurrentUserDetails();

        sendButton.setOnClickListener(view -> sendMessage());
        //retrieveData();
        setUpMessageRecyclerView();

        backButton.setOnClickListener(view -> onBackPressed());

    }

    private void getCurrentUserDetails() {
        firestore.collection("Admins").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                AdminDetailsClass adminDetailsClass = documentSnapshot.toObject(AdminDetailsClass.class);
                name = adminDetailsClass.getUserName();
                image = adminDetailsClass.getImage();
                department = adminDetailsClass.getDepartment();
            }
        });

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        senderUid = mAuth.getCurrentUser().getUid();
    }

    private void sendMessage() {
        if (!messageText.getText().toString().isEmpty()) {
            MessageClass messageClass = new MessageClass();
            messageClass.setMessage(messageText.getText().toString());

            Calendar cDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
            final String saveDate = currentDate.format(cDate.getTime());

            Calendar cTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:mm", Locale.getDefault());
            final String saveTime = currentTime.format(cTime.getTime());

            String time = saveDate + ":" + saveTime;
            //messageClass.setDate(saveDate);


            messageClass.setTime(null);
            messageClass.setSenderUid(senderUid);
            messageClass.setName(name);
            messageClass.setImage(image);
            messageClass.setDepartment(department);
            Log.d(TAG, "sendMessage: " + FieldValue.serverTimestamp());

            firestore.collection("Messages").document()
                    .set(messageClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        if (checkbox.isChecked()) {
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                                    userMail, messageText.getText().toString(), getApplicationContext(), MessageActivity.this);
                            notificationsSender.SendNotifications();
                        }
                        messageText.setText("");
                        onStart();
                        firestoreMessageRecyclerAdapter.notifyDataSetChanged();
                        messageRecyclerView.smoothScrollToPosition(messageRecyclerView.getBottom());
                    } else {
                        Log.d(TAG, "onComplete: message sending error" + task.getException());
                    }
                }
            });
        }
    }


    private void initFields() {
        messageText = findViewById(R.id.message_text);
        sendButton = findViewById(R.id.send_button);
        backButton = findViewById(R.id.back_button);
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        messageRecyclerView.setHasFixedSize(true);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(false);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        checkbox = findViewById(R.id.checkbox);


    }

    @Override
    protected void onStart() {
        super.onStart();

        firestoreMessageRecyclerAdapter.startListening();

    }

    private void setUpMessageRecyclerView() {


        //Query
        Query query = firestore.collection("Messages").orderBy("time", Query.Direction.ASCENDING);
        //Recycler options
        FirestoreRecyclerOptions<MessageClass> options = new FirestoreRecyclerOptions.Builder<MessageClass>()
                .setQuery(query, MessageClass.class)
                .build();

        firestoreMessageRecyclerAdapter = new FirestoreRecyclerAdapter<MessageClass, MessageViewHolder>(options) {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_layout, parent, false);
                return new MessageViewHolder(view, false);


            }


            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageClass model) {
                holder.setMessage(getApplication(), model.getMessage(), model.getTime(),
                        model.getSenderUid(), model.getName(), model.getImage(), model.getDepartment());


            }


            @Override
            public void onDataChanged() {
                super.onDataChanged();
                messageRecyclerView.smoothScrollToPosition(messageRecyclerView.getBottom());
            }
        };


        messageRecyclerView.setAdapter(firestoreMessageRecyclerAdapter);

    }

//    private void retrieveData() {
//
//        ArrayList<MessageClass> messageList = new ArrayList<>();
//        messageAdapter = new MessageAdapter(messageList, this,senderUid);
//
//        messageRecyclerView.setAdapter(messageAdapter);
//
//        messageRecyclerView.setHasFixedSize(true);
//        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        firestore.collection("Messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.d(TAG, "onEvent: " + error.getMessage());
//                    return;
//                }
//
//
//                for (DocumentChange dc : value.getDocumentChanges()) {
//                    if (dc.getType() == DocumentChange.Type.ADDED) {
//                        messageList.add(dc.getDocument().toObject(MessageClass.class));
//                        Log.d(TAG, "onEvent: list of added" + dc.getDocument().toObject(MessageClass.class));
//                    }
//
//
//                }
//            }
//        });
//    }
}
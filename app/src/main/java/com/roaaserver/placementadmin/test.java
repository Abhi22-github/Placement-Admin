package com.roaaserver.placementadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.roaaserver.placementadmin.Models.CompanyDetailsClass;
import com.roaaserver.placementadmin.Models.testModel;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

public class test extends AppCompatActivity {
    private TextInputEditText role, company;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private MaterialButton createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        role = findViewById(R.id.role);
        company = findViewById(R.id.company);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        createButton = findViewById(R.id.create_button);

        Map<String, Object> map = new HashMap<>();
        map.put("Gender", "No Criteria");
        map.put("Internship", "6 Months");
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testModel testmodel = new testModel();
                testmodel.setRole(role.getText().toString());
                testmodel.setCompany(company.getText().toString());
                testmodel.setMap(map);

                firestore.collection("test").document(mAuth.getCurrentUser().getUid())
                        .set(testmodel);
            }
        });

        firestore.collection("test").whereEqualTo("map.Gender","Male")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                testModel testModel = dc.getDocument().toObject(testModel.class);
                                Toast.makeText(test.this, testModel.getMap().get("Gender").toString(),Toast.LENGTH_SHORT).show();
                            }



                        }
                    }
                });
    }
}
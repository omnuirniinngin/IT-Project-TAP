package com.tap.taskassigningandplanning.ui.profile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.R;

public class ActivityProfileUpdate extends AppCompatActivity {

    private EditText etName, etBio;
    private Button btnUpdate;

    String user_id;

    //FIREBASE
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setTitle("Update Profile");

        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        btnUpdate = findViewById(R.id.btnUpdate);

        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DocumentReference userRef = db.collection("Users").document(user_id);

        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            String name = (String) documentSnapshot.get("name");
                            String bio = (String) documentSnapshot.get("bio");

                            etName.setText(name);
                            etName.setSelection(name.length());

                            if(!bio.isEmpty()){
                                etBio.setText(bio);
                                etBio.setSelection(bio.length());
                            }

                        }
                    }
                });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
                setResult(Activity.RESULT_OK);
                onBackPressed();
            }
        });
    }

    private void updateProfile(){
        final String name = etName.getText().toString().trim();
        final String bio = etBio.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Display name required", null);
            etName.requestFocus();
            return;
        }

        DocumentReference userRef = db.collection("Users").document(user_id);

        userRef.update("name", name);
        userRef.update("bio", bio);

    }
}

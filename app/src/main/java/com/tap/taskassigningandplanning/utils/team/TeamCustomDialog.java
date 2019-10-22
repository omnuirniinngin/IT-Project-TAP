package com.tap.taskassigningandplanning.utils.team;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamCustomDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "TeamCustomDialog";

    private EditText etRequest;
    private Button btnAdd, btnCancel;
    private ProgressDialog progressDialog;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private User user;
    private List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_team, container, false);
        getDialog().setTitle("Add user");

        etRequest = view.findViewById(R.id.etRequest);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        return view;
    }

    private void addUser(){
        final String email = etRequest.getText().toString().trim();
        final boolean status = false;


        if(!email.isEmpty()){

            //Get plan id
            NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
            Bundle id_result = activity.getPlanId();
            final String plan_id = id_result.getString("plan_id");

            // Query user through email
            db.collection("Users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    final String name = (String) document.get("name");
                                    String user_id = document.getId();

                                    Log.d(TAG, "user_name: " + name);
                                    Log.d(TAG, "user_id: " + user_id);

                                    Team team = new Team(email, plan_id, name, user_id, status);

                                    // Making sub collections instead of making a new collection
                                    // Research for alternative use. Code is not yet working
//                                    db.collection("Plan").document(plan_id).collection("team").document(plan_id);

                                    FirebaseFirestore.getInstance()
                                            .collection("Team")
                                            .add(team)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "onSuccess: User added to team");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            getDialog().dismiss();
        }else {
            etRequest.setError("Please input field");
            etRequest.requestFocus();
            return;
        }


    }

    private void checkEmail(){
        progressDialog = ProgressDialog.show(getContext(), "", "Sending request...", true);
        mAuth.fetchSignInMethodsForEmail(etRequest.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.getResult().getSignInMethods().isEmpty()){
                            Log.d(TAG, "onComplete: invitation sent via email!");
                            progressDialog.dismiss();
                            sendMail();
                            Toast.makeText(getActivity(), "User doesn't exist. Invite via email.", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d(TAG, "onComplete: Send request via app notification");
                            Toast.makeText(getActivity(), "Invitation sent!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            queryEmail();
                            addUser();
                        }
                    }
                });
    }

    public void queryEmail(){
        final String email = etRequest.getText().toString().trim();

        NavigationBottomActivity navigationBottomActivity = (NavigationBottomActivity)getActivity();
        Bundle id_result = navigationBottomActivity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        // Query user through email
        db.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "User id is " + document.getId());
                                // UPDATE ARRAY IN USERS
                                String user_id = document.getId();
                                String user_name = (String) document.get("name");

                                //Use this when adding data within an array that has no value
                                DocumentReference userRef = db.collection("Plan").document(plan_id);
                                userRef.update("team", FieldValue.arrayUnion(user_id));

//                                DocumentReference docRef = db.collection("Plan").document(plan_id);
//
//
//
//                                Map<String, Boolean> plan = new HashMap<>();
//                                plan.put(user_id, false);
//
//                                docRef.update("team", FieldValue.arrayUnion(plan)).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Log.d(TAG, "onSuccess: Added to sub collections");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error writing in sub collection", e);
//                                    }
//                                });

/*                                db.collection("Plan").document(plan_id).update("team", FieldValue.arrayUnion(plan));*/

//                                db.collection("Plan")
//                                        .add(plan)
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w(TAG, "Error adding document", e);
//                                            }
//                                        });

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void sendMail(){
        String email = etRequest.getText().toString().trim();
        Log.d(TAG, "sendMail: ");

        String[] TO_EMAIL = {email};

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAIL);

        intent.putExtra(Intent.EXTRA_SUBJECT, "Invitation request.");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Good day! I would like you to be part of my on going plan and you are one of the selected person I want to work on this particular task." +
                        "To accept my request kindly download this app. ###LINK");

        startActivity(Intent.createChooser(intent, "Send request"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                checkEmail();
                break;
            case R.id.btnCancel:
                Log.d(TAG, "onClick: Closing Dialog");
                getDialog().dismiss();
                break;
        }
    }


}

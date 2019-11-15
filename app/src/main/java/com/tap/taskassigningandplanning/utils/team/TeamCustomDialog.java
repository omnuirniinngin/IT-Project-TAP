package com.tap.taskassigningandplanning.utils.team;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

    private boolean emailValidation(String email){

        if (email.isEmpty()) {
            etRequest.setError(getString(R.string.input_error_email), null);
            etRequest.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etRequest.setError(getString(R.string.input_error_email_invalid), null);
            etRequest.requestFocus();
            return true;
        }
        return false;
    }

    private void addUser(){
        final String email = etRequest.getText().toString().trim();

        final boolean status = false;

        if(!emailValidation(email)){

            //Get plan id
            NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
            Bundle id_result = activity.getPlanId();
            final String plan_id = id_result.getString("plan_id");
            final String plan_name = id_result.getString("plan_name");
            final String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

            final DocumentReference creatorRef = db.collection("Users").document(current_user);

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
                                    final String user_id = document.getId();

                                    Log.d(TAG, "user_name: " + name);
                                    Log.d(TAG, "user_id: " + user_id);

                                    // Get creators name to be display
                                    creatorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    String creator = (String) document.get("name");
                                                    String creator_id = current_user;
                                                    String request = "Request Pending";
                                                    int task_completed = 0;

                                                    Team team = new Team(email, plan_id, name, user_id, plan_name, creator, creator_id, request, task_completed, status, new Timestamp(new java.util.Date()));

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

                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
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
        String email = etRequest.getText().toString().trim();
        if(!emailValidation(email)){
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
                                queryEmail();
                                addUser();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
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

                                //Use this when adding data within an array that has no value
                                //Should use this later if the user accepted the plan
                                DocumentReference userRef = db.collection("Plan").document(plan_id);
                                userRef.update("team", FieldValue.arrayUnion(user_id));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void sendMail(){
        final String email = etRequest.getText().toString().trim();
        /*String password = "1234";
        final String name = "Setup profile";*/

/*        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(name, email);
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            db.collection("Users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: User added on Firestore");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: Error creating user");
                                        }
                                    });
                        }
                    }
                });*/

        Log.d(TAG, "sendMail: ");

        String[] TO_EMAIL = {email};

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAIL);

        intent.putExtra(Intent.EXTRA_SUBJECT, "Invitation request.");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Good day! I would like you to be part of my on going plan and you are one of the selected person I want to work on this particular task." +
                        "To accept my request kindly download this app. ***LINK***");

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

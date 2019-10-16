package com.tap.taskassigningandplanning.utils.team;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;

public class TeamCustomDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "TeamCustomDialog";

    private EditText etRequest;
    private Button btnAdd, btnCancel;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
        String email = etRequest.getText().toString().trim();

        if(!email.isEmpty()){
            checkEmail();
            //Get plan id
            NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
            Bundle id_result = activity.getPlanId();
            final String plan_id = id_result.getString("plan_id");


            Team team = new Team(email, plan_id);

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
            getDialog().dismiss();
        }else {
            etRequest.setError("Please input field");
            etRequest.requestFocus();
            return;
        }


    }

    private void checkEmail(){
        mAuth.fetchSignInMethodsForEmail(etRequest.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.getResult().getSignInMethods().isEmpty()){
                            Log.d(TAG, "onComplete: invitation sent via email!");
                        }else {
                            Log.d(TAG, "onComplete: Send request via app notification");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                addUser();
                break;
            case R.id.btnCancel:
                Log.d(TAG, "onClick: Closing Dialog");
                getDialog().dismiss();
                break;
        }
    }


}

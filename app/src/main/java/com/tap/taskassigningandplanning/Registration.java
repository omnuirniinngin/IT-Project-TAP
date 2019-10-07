package com.tap.taskassigningandplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    private static final String TAG = "Registration";

    EditText etFullName, etEmail, etPassword;
    String name, email, password;
    Button btnContinue;
    TextView tvHaveAccount;
    int formsuccess = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etFullName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                formsuccess = 3;

                if(name.equals("")){
                    etFullName.setError("This field is required.",null);
                    formsuccess--;
                }
                if(email.equals("")){
                    etEmail.setError("This field is required.",null);
                    formsuccess--;
                }
                if(password.equals("")){
                    etPassword.setError("This field is required.", null);
                    formsuccess--;
                }
                if(formsuccess == 3){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "onComplete: success!");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent (getApplicationContext(), Login.class));
                                    }else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Registration.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //Toast.makeText(getApplicationContext(), "Form Successfully Validated", Toast.LENGTH_SHORT).show();
                }


            }
        });


        tvHaveAccount = findViewById(R.id.tvHaveAccount);
        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

    }
}

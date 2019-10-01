package com.tap.taskassigningandplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText etEmail, etPassword;
    String email, password;
    Button btnLogin, btnCreateAccount;
    int formsuccess = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                formsuccess = 2;

                if(email.equals("")){
                    etEmail.setError("This field is required.");
                    formsuccess--;
                }
                if(password.equals("")){
                    etPassword.setError("This field is required.");
                    formsuccess--;
                }
                if(formsuccess == 2){
                    Intent i = new Intent(Login.this, DrawerActivity.class);
                    startActivity(i);
                    //Toast.makeText(getApplicationContext(), "Form Successfully Validated", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

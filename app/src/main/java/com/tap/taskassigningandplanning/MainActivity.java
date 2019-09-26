package com.tap.taskassigningandplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword;
    String name, email, password;
    Button btnContinue;
    TextView tvHaveAccount;
    int formsuccess = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    etFullName.setError("This field is required.");
                    formsuccess--;
                }
                if(email.equals("")){
                    etEmail.setError("This field is required.");
                    formsuccess--;
                }
                if(password.equals("")){
                    etPassword.setError("This field is required.");
                    formsuccess--;
                }
                if(formsuccess == 3){
                    Toast.makeText(getApplicationContext(), "Form Successfully Validated", Toast.LENGTH_SHORT).show();
                }


            }
        });


        tvHaveAccount = findViewById(R.id.tvHaveAccount);
        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

    }
}

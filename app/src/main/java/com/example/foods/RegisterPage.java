package com.example.foods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    EditText edmail, edpwd;
    Button btnlogin, btnregister;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        getSupportActionBar().setTitle("Register Here");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edmail = findViewById(R.id.editTextTextEmailAddress);
        edpwd = findViewById(R.id.editTextTextPassword);

        btnlogin = findViewById(R.id.button2);
        btnregister = findViewById(R.id.button);

        progressBar=findViewById(R.id.progressbarid);

        mAuth=FirebaseAuth.getInstance();



        //------------------------//
//        Navigate to Login Activity
        //------------------------//
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(in);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usermail = edmail.getText().toString().trim();
                String userpwd = edpwd.getText().toString().trim();

                if (usermail.equals("")) {
                    Toast.makeText(RegisterPage.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (userpwd.equals("")) {
                    Toast.makeText(RegisterPage.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(usermail, userpwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                           startActivity(new Intent(RegisterPage.this,Home.class));
                           finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                       progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                        }
                    });
                }


            }
        });
    }
}
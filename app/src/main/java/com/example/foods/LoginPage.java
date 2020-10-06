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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText edmail,edpwd;
    Button btnlogin,btnregister;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            startActivity(new Intent(LoginPage.this,Home.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().setTitle("Login Here");

        edmail=findViewById(R.id.editTextTextEmailAddress);
        edpwd=findViewById(R.id.editTextTextPassword);

        btnlogin=findViewById(R.id.button);
        btnregister=findViewById(R.id.button2);

        progressBar=findViewById(R.id.progressbarid);

        mAuth = FirebaseAuth.getInstance();
        //------------------------//
//        Navigate to Register Activity
        //------------------------//
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(LoginPage.this,RegisterPage.class);
                startActivity(in);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String usermail= edmail.getText().toString().trim();
                    String userpwd= edpwd.getText().toString().trim();

                    if(usermail.equals(""))
                    {
                        Toast.makeText(LoginPage.this, "Enter your Mail", Toast.LENGTH_SHORT).show();
                    }
                    else if(userpwd.equals(""))
                    {
                        Toast.makeText(LoginPage.this, "Enter the Password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.signInWithEmailAndPassword(usermail,userpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginPage.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                    Intent in= new Intent(LoginPage.this, Home.class);
                                    startActivity(in);
                                    finish();

                                }
                                else
                                {
                                    Toast.makeText(LoginPage.this, "Try Again", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });


                    }

            }
        });

    }
}
package com.example.foods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foods.Model.UserInfo;

public class UserPage extends AppCompatActivity {

    EditText edName, edMobile, edAddress;
    Button btnUpload,usersdelete;
    String userId;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        getSupportActionBar().setTitle("User Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edName=findViewById(R.id.editTextTextPersonName3);
        edMobile=findViewById(R.id.editTextPhone);
        edAddress=findViewById(R.id.editTextTextMultiLine);
        btnUpload=findViewById(R.id.button5);
        usersdelete=findViewById(R.id.userdelete);

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        userId= firebaseAuth.getCurrentUser().getUid();

        updateUserDetails();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edName.getText().toString().trim();
                String mobile=edMobile.getText().toString().trim();
                String address=edAddress.getText().toString().trim();

                if(name.equals(""))
                {
                    Toast.makeText(UserPage.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(mobile.equals(""))
                {
                    Toast.makeText(UserPage.this, "Enter Mobile No", Toast.LENGTH_SHORT).show();
                }
                else if(address.equals(""))
                {
                    Toast.makeText(UserPage.this, "Enter the Address", Toast.LENGTH_SHORT).show();
                }
                else
                {


                    UserInfo ui=new UserInfo();
                    ui.setUserName(name);
                    ui.setUserMobile(mobile);
                    ui.setUserAddress(address);
                    ui.setUserId(userId);

                    databaseReference.child(userId).setValue(ui).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(UserPage.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(UserPage.this, "Something went Wrong, check your connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
    }

    private void updateUserDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    UserInfo ui=dataSnapshot1.getValue(UserInfo.class);
                    if(ui.getUserId().equals(userId))
                    {
                        edName.setText(ui.getUserName());
                        edMobile.setText(ui.getUserMobile());
                        edAddress.setText(ui.getUserAddress());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        usersdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(userId).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UserPage.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            edName.setText("");
                            edAddress.setText("");
                            edMobile.setText("");
                            Intent intent = new Intent(UserPage.this, LoginPage.class);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(UserPage.this, "Something went Wrong, check your connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
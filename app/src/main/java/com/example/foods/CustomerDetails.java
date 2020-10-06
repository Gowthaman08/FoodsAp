package com.example.foods;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foods.Model.Delivery;

public class CustomerDetails extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView tvname,tvaddress,tvphn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);


        tvname=findViewById(R.id.tvname);
        tvaddress=findViewById(R.id.address);
        tvphn=findViewById(R.id.phone);


        final String itemid = getIntent().getStringExtra("itemid");


        databaseReference= FirebaseDatabase.getInstance().getReference("Delivery Details");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Delivery ui=dataSnapshot1.getValue(Delivery.class);
                    if(ui.getItemid().equals(itemid))
                    {

                        tvaddress.setText("Address"+ui.getAddress());
                        tvphn.setText("Phone No"+ui.getPhn());

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
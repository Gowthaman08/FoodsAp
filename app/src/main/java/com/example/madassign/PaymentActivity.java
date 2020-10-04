package com.example.madassign;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;



public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnDatePicker,next2,back;
    EditText txtDate,cardName,cardNumber,cardtype,paymenttype;
    private int mYear, mMonth, mDay;
    Button btnsave,BtnShow,btnUpdate,btndelete;
    private DatabaseReference db;

   Pay P;

    private void clearControls() {

        cardName.setText("");
       cardNumber.setText("");
        cardtype.setText("");
       paymenttype.setText("");
        txtDate.setText("");


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.in_date);
        btnDatePicker.setOnClickListener(this);
        next2=(Button)findViewById(R.id.next2);
        back=(Button)findViewById(R.id.back);

        cardName = findViewById(R.id.cardName);
        cardNumber = findViewById(R.id.cardNumber);
        cardtype=findViewById(R.id.cardtype);
        paymenttype=findViewById(R.id.paytype);
        txtDate=findViewById(R.id.in_date);

        btnsave = findViewById(R.id.add);
        BtnShow=findViewById(R.id.btnshow);
        btnUpdate=findViewById(R.id.btnupdate);
        btndelete=findViewById(R.id.btndelete);


        P=new Pay();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              db = FirebaseDatabase.getInstance().getReference().child("Request");

                try {
                    if (TextUtils.isEmpty(cardName.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter the card Name", Toast.LENGTH_SHORT);
                    else if (TextUtils.isEmpty(cardNumber.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter the card number", Toast.LENGTH_SHORT);
                    else if (TextUtils.isEmpty(cardtype.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter the card type", Toast.LENGTH_SHORT);
                    else if (TextUtils.isEmpty(paymenttype.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Please enter the payment type", Toast.LENGTH_SHORT);

                    else {

                        P.setCardname(cardName.getText().toString().trim());
                        P.setCardnumber(Integer.parseInt(cardNumber.getText().toString().trim()));
                        P.setCardtype(cardtype.getText().toString().trim());
                        P.setPaymenttype(paymenttype.getText().toString().trim());
                        P.setTxtDate(txtDate.getText().toString().trim());



                        //dbRef.push().setValue(RE);
                        db.child("R1").setValue(P);
                        Toast.makeText(getApplicationContext(), "Data entered success", Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid ContactNumber", Toast.LENGTH_SHORT).show();
                }
            }

        });
        //show the data what is in the database
        BtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("Request").child("R1");
                readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChildren()){
                            cardName.setText(dataSnapshot.child("Cardname").getValue().toString());
                            cardtype.setText(dataSnapshot.child("Cardtype").getValue().toString());
                            paymenttype.setText(dataSnapshot.child("Paymenttype").getValue().toString());
                            txtDate.setText(dataSnapshot.child("Date").getValue().toString());
                            cardNumber.setText(dataSnapshot.child("Cardnumber").getValue().toString());



                        }
                        else
                            Toast.makeText(getApplicationContext(),"NO DATA SOURCE TO DISPLAY",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference[] delRef = {FirebaseDatabase.getInstance().getReference().child("Request")};
                delRef[0].addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("R1")){
                            delRef[0] =FirebaseDatabase.getInstance().getReference().child("Request");
                            delRef[0].removeValue();
                            clearControls();
                            Toast.makeText(getApplicationContext(),"Data entered sucessful",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"no data resource",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference upRef=FirebaseDatabase.getInstance().getReference().child("Request");
                upRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("R1"))
                            try {
                                P.setCardname(cardName.getText().toString().trim());
                                P.setPaymenttype(paymenttype.getText().toString().trim());
                                P.setCardtype(cardtype.getText().toString().trim());
                                P.setTxtDate(txtDate.getText().toString().trim());
                                P.setCardnumber(Integer.parseInt(cardNumber.getText().toString().trim()));

                               db= FirebaseDatabase.getInstance().getReference().child("Request");
                               db.setValue(P);
                                clearControls();
                                Toast.makeText(getApplicationContext(), "Data Updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            catch (NumberFormatException e){
                                Toast.makeText(getApplicationContext(),"Invalid Contact Number",Toast.LENGTH_SHORT).show();
                            }
                        else
                            Toast.makeText(getApplicationContext(),"No source to update",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });




        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openOrderActivity();

            }

        });



        next2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openReviewActivity();

            }

        });



    }

    private void openReviewActivity() {
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }
    private void openOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v== btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }

}
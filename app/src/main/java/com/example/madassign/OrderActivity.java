package com.example.madassign;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    EditText Name;
    EditText Address;
    EditText Email;
    EditText Phoneno;
    Button save;
    Button next;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private DatabaseReference mdatareference;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Food order;


    private void clearControls() {

        Name.setText("");
        Address.setText("");
        Email.setText("");
        Phoneno.setText("");
        txtDate.setText("");
        txtTime.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        Name = findViewById(R.id.Name);
        Address = findViewById(R.id.Address);
        Email = findViewById(R.id.Email);
        Phoneno = findViewById(R.id.Phoneno);
        save = findViewById(R.id.save);
        next = findViewById(R.id.next);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        //date time picker
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        //save data


        next.setOnClickListener(new View.OnClickListener() {

            @Override
                public void onClick(View v2) {
                openPaymentActivity();

            }

        });


        order =new Food();
        mdatareference = FirebaseDatabase.getInstance().getReference();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();


                String getName = Name.getText().toString().trim();
                String getPhoneno = Phoneno.getText().toString().trim();
                String getEmail = Email.getText().toString().trim();
                String getTxtDate = txtDate.getText().toString().trim();
                String getTxtTime = txtTime.getText().toString().trim();
                String getAddress = Address.getText().toString().trim();

                HashMap datamap =new HashMap();
                datamap.put("Name", getName);
                datamap.put("Phonenumber", getPhoneno);
                datamap.put("Email", getEmail);
                datamap.put("Address", getAddress);
                datamap.put("Time", getTxtTime);
                datamap.put("Date", getTxtDate);
                mdatareference.push().setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(OrderActivity.this,"Sucessfull stored",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(OrderActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                clearControls();
            }
        });
    }
    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

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
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }


    }

    private void openPaymentActivity() {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isValidPhone(EditText text) {
        CharSequence Phoneno = text.getText().toString();
        return (!TextUtils.isEmpty(Phoneno) && Patterns.PHONE.matcher(Phoneno).matches());

    }
    void checkDataEntered() {

        if (isEmpty(Name)) {
            Name.setError("name is required!");
        }
        if(isEmpty(txtDate)){
            txtDate.setError("date is required!");

        }
        if(isEmpty(txtTime)){
            txtTime.setError("date is required!");
        }

        if (isEmpty(Address)) {
            Address.setError("address is required!");
        }
        if (isValidPhone(Phoneno) == false) {
            Email.setError("Enter validphoneno");
        }

        if (isEmail(Email) == false) {
            Email.setError("Enter valid email!");
        }
    }

}
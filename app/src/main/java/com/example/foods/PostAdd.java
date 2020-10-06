package com.example.foods;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.example.foods.Model.Item;

public class PostAdd extends AppCompatActivity {


    EditText edItemName,edItemPrice,edItemDescription;
    ImageButton imageButton;
    Button btnupload;
    ProgressBar progressBar;
    /////////---image selection-----///////////
    public static final int PIC_IMG_REQUEST=1;
    Uri uri;
    /////////---image selection-----///////////

    //////--------------Firebase -----------///////////
    private DatabaseReference databaseReference;
    private DatabaseReference mydatabaseReference;
    private StorageReference storageReference;
    //////--------------Firebase -----------///////////

    private FirebaseAuth firebaseAuth;
    String user="";

    String itemid="no",gimg="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);

        getSupportActionBar().setTitle("Post Details Here");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        edItemName=findViewById(R.id.editTextTextPersonName);
        edItemPrice=findViewById(R.id.editTextNumberDecimal);
        edItemDescription=findViewById(R.id.editTextTextPersonName2);

        imageButton=findViewById(R.id.imageButton);
        btnupload=findViewById(R.id.button3);


        final String itname=getIntent().getStringExtra("name");
        if(itname!=null)
        {
            String itemname=getIntent().getStringExtra("name");
            String itdes=getIntent().getStringExtra("description");
            String pri=getIntent().getStringExtra("price");
            String img=getIntent().getStringExtra("image");
            gimg=img;
            String useridd=getIntent().getStringExtra("userid");
            String itemidd=getIntent().getStringExtra("itemid");
            itemid=itemidd;

            edItemName.setText(itemname);
            edItemPrice.setText(pri);
            edItemDescription.setText(itdes);

            Picasso.get()
                    .load(img)
                    .centerCrop()
                    .fit()
                    .into(imageButton);

        }


        progressBar=findViewById(R.id.profressbarid);

        /////////////////////////////////////////////
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Items");
        mydatabaseReference= FirebaseDatabase.getInstance().getReference().child("My Items");
        storageReference= FirebaseStorage.getInstance().getReference().child("Items");
        /////////////////////////////////////////////
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser().getUid();


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(itname==null)
                {
                    if(edItemName.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(PostAdd.this, "Please Enter Item Name", Toast.LENGTH_SHORT).show();
                    }
                    else if(edItemPrice.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(PostAdd.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                    }
                    else if(edItemDescription.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(PostAdd.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                    }
                    else if(uri==null)
                    {
                        Toast.makeText(PostAdd.this, "Select the image", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        btnupload.setVisibility(View.INVISIBLE);
                        final StorageReference filestoregereference = storageReference.child(System.currentTimeMillis() + "."
                                + getpictureextenction(uri));


                        filestoregereference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return filestoregereference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful())
                                {
                                    final Uri downUri = task.getResult();

                                    final String key=databaseReference.push().getKey();

                                    final Item item=new Item();

                                    item.setUserid(user);
                                    item.setItemid(key);
                                    item.setItemName(edItemName.getText().toString().trim());
                                    item.setItemDescription(edItemDescription.getText().toString().trim());
                                    item.setItemImage(downUri.toString());
                                    item.setItemPrice(edItemPrice.getText().toString().trim());

                                    databaseReference.child(key).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                mydatabaseReference.child(user).child(key).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(PostAdd.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(PostAdd.this,MyItems.class));
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });


                                            }else
                                            {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(PostAdd.this, "Upload Failiure", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }
                                else
                                {
                                    Toast.makeText(PostAdd.this, "Try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }else
                {
                    if(edItemName.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(PostAdd.this, "Please Enter Item Name", Toast.LENGTH_SHORT).show();
                    }
                    else if(edItemPrice.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(PostAdd.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                    }
                    else if(edItemDescription.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(PostAdd.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        final Item item=new Item();

                        item.setUserid(user);
                        item.setItemid(itemid);
                        item.setItemName(edItemName.getText().toString().trim());
                        item.setItemDescription(edItemDescription.getText().toString().trim());
                        item.setItemImage(gimg);
                        item.setItemPrice(edItemPrice.getText().toString().trim());

                        databaseReference.child(itemid).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mydatabaseReference.child(user).child(itemid).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(PostAdd.this, "Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PostAdd.this,MyItems.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }


            }
        });

    }

    private String getpictureextenction(Uri uri) {
        ContentResolver cn=getApplication().getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cn.getType(uri));
    }

    private void selectimage() {
        Intent in=new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in,PIC_IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PIC_IMG_REQUEST && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null)
        {
            uri=data.getData();

            Picasso.get().load(uri).into(imageButton);

        }
    }
}
package com.example.foods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.foods.Adapter.AdapterMyItem;
import com.example.foods.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class MyItems extends AppCompatActivity implements AdapterMyItem.Onitemclicklistener {

    ProgressBar progressBar;
    FloatingActionButton fabbtn;

    RecyclerView recyclerView;
    DatabaseReference databaseReference,databaseReference2;
    List<Item> mlist;
    AdapterMyItem madapter;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        getSupportActionBar().setTitle("My Added Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressbarid);
        fabbtn = findViewById(R.id.fab);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerviewid);

        databaseReference= FirebaseDatabase.getInstance().getReference("My Items").child(firebaseAuth.getUid());
        databaseReference2= FirebaseDatabase.getInstance().getReference("Items");

        recyclerView = findViewById(R.id.recyclerviewid);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        mlist = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Item item1 = dataSnapshot1.getValue(Item.class);
                    mlist.add(item1);
                }

                madapter = new AdapterMyItem(MyItems.this, mlist);
                recyclerView.setAdapter(madapter);

                 madapter.setOnitemclicklistener(MyItems.this);

                madapter.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyItems.this,PostAdd.class));
            }
        });
    }


    @Override
    public void clickitem(int position) {

        Item item=mlist.get(position);

        Intent intent=new Intent(MyItems.this,CustomerDetails.class);
        intent.putExtra("itemid", item.getItemid());
        startActivity(intent);


    }

    @Override
    public void delete(int position) {
        final Item item=mlist.get(position);

        databaseReference.child(item.getItemid()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference2.child(item.getItemid()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                         Toast.makeText(MyItems.this, "Deleted", Toast.LENGTH_SHORT).show();

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

    @Override
    public void update(int position) {
        final Item item=mlist.get(position);

        String itemname=item.getItemName();
        String description=item.getItemDescription();
        String price=item.getItemPrice();
        String image= item.getItemImage();
        String userid=item.getUserid();
        String itemid=item.getItemid();

        Intent in=new Intent(MyItems.this,PostAdd.class);
        in.putExtra("name",itemname);
        in.putExtra("description",description);
        in.putExtra("price",price);
        in.putExtra("image",image);
        in.putExtra("userid",userid);
        in.putExtra("itemid",itemid);
        startActivity(in);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyItems.this,Home.class));
        finish();
    }
}
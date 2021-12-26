package com.example.controlpanalamal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Edit_shoes extends AppCompatActivity {
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
Bundle bundle;
TextView Type , Price , Description , Made ;
ImageView shoes;
Button edit_price , edit_made , edit_description;
RecyclerView sizes , colors ;
ArrayList<Integer> size_shoes;
ArrayList<String> color_shoes;
Adapter_color adapter_color;
Adapter_size adapter_size;
AlertDialog.Builder alert;
String type , image , Key;
Upload_image_price upload_image_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shoes);
        init();
        getInfoShoes();
        Edit_Price();
        Edit_Made();
        Edit_Description();
    }

    private void Edit_Description() {
    edit_description.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater li = LayoutInflater.from(Edit_shoes.this);
            View view = li.inflate(R.layout.alert_dialog_edit,null);
            alert = new AlertDialog.Builder(Edit_shoes.this);
            alert.setView(view);
            EditText edit_shoes = view.findViewById(R.id.edit_shoes);
            edit_shoes.setHint("enter a new description  please : ");
            edit_shoes.setInputType(InputType.TYPE_CLASS_TEXT);
            Button edit = view.findViewById(R.id.Click_Edit);
            Button cancel = view.findViewById(R.id.Click_Cancel);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edit_shoes.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(),
                                "Please edit shoes",Toast.LENGTH_SHORT).show();
                    else {
                        String element_new = edit_shoes.getText().toString();
                        Description.setText(" "+element_new);
                        alertDialog.cancel();
                        databaseReference.child("InfoShoes").child(bundle.getString("ID"))
                                .child(type).child("more").child("description").setValue(element_new);
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
        }
    });
    }

    private void Edit_Made() {
    edit_made.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater li = LayoutInflater.from(Edit_shoes.this);
            View view = li.inflate(R.layout.alert_dialog_edit,null);
            alert = new AlertDialog.Builder(Edit_shoes.this);
            alert.setView(view);
            EditText edit_shoes = view.findViewById(R.id.edit_shoes);
            edit_shoes.setHint("enter a new Made please : ");
            edit_shoes.setInputType(InputType.TYPE_CLASS_TEXT);
            Button edit = view.findViewById(R.id.Click_Edit);
            Button cancel = view.findViewById(R.id.Click_Cancel);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edit_shoes.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(),
                                "Please edit shoes",Toast.LENGTH_SHORT).show();
                    else {
                        String element_new = edit_shoes.getText().toString();
                        Made.setText("Made: "+element_new);
                        alertDialog.cancel();
                        databaseReference.child("InfoShoes").child(bundle.getString("ID"))
                                .child(type).child("more").child("Made").setValue(element_new);
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
        }
    });
        }

    private void Edit_Price() {
    edit_price.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater li = LayoutInflater.from(Edit_shoes.this);
            View view = li.inflate(R.layout.alert_dialog_edit,null);
            alert = new AlertDialog.Builder(Edit_shoes.this);
            alert.setView(view);
            EditText edit_shoes = view.findViewById(R.id.edit_shoes);
            edit_shoes.setHint("enter a new Price please : ");
            edit_shoes.setInputType(InputType.TYPE_CLASS_NUMBER);
            Button edit = view.findViewById(R.id.Click_Edit);
            Button cancel = view.findViewById(R.id.Click_Cancel);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edit_shoes.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(),
                                "Please edit shoes",Toast.LENGTH_SHORT).show();
                    else {
                        int element_new = Integer.parseInt(edit_shoes.getText().toString());
                        Price.setText("Price "+element_new + " SYR");
                        alertDialog.cancel();
                        databaseReference.child("InfoShoes").child(bundle.getString("ID"))
                                .child(type).child(Key).child("price").setValue(element_new);
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                }
            });
        }
    });
    }

    private void getInfoShoes() {
        databaseReference.child("InfoShoes").child(bundle.getString("ID")).
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                      Type.append(snapshot.getKey());
                      type = snapshot.getKey();
                      getImageAndPrice(type,bundle.getString("ID"));
                      Made.append(" "+snapshot.child("more").child("Made").getValue(String.class));
                        Description.append(" "+snapshot.child("more").child("description").getValue(String.class));
                        for (DataSnapshot object : snapshot.child("more").child("Sizes").getChildren()) {
                            size_shoes.add(object.getValue(Integer.class));
                        }
                        setAdapterSize(size_shoes);
                        for (DataSnapshot object : snapshot.child("more").child("colors").getChildren()) {
                            color_shoes.add(object.getValue(String.class));
                        }
                        setAdapterColor(color_shoes);
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getImageAndPrice(String type, String id) {
    databaseReference.child("InfoShoes").child(id).child(type).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if(!snapshot.getKey().equals("more"))
            {

                upload_image_price = snapshot.getValue(Upload_image_price.class);
            Price.append(" "+upload_image_price.getPrice()+" SYR");
            Glide.with(getApplicationContext()).load(upload_image_price.getImage())
                    .into(shoes);
            Key = snapshot.getKey();
            }
        }


        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    private void setAdapterColor(ArrayList<String> color_shoes) {
    adapter_color.setListColor(color_shoes,bundle.getString("ID"),type);
        RecyclerView.LayoutManager rec = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        colors.setLayoutManager(rec);
        colors.setAdapter(adapter_color);
    }
    private void setAdapterSize(ArrayList<Integer> size_shoes) {
    adapter_size.setListSize(size_shoes,bundle.getString("ID"),type);
        RecyclerView.LayoutManager rec = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        sizes.setLayoutManager(rec);
        sizes.setAdapter(adapter_size);
    }
    private  void init(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        bundle = getIntent().getExtras();
        Type = findViewById(R.id.DataCategory);
        Made = findViewById(R.id.DataMade);
        Description = findViewById(R.id.DataDescription);
        Price = findViewById(R.id.DataPrice);
        shoes = findViewById(R.id.DataImage);
        sizes = findViewById(R.id.recyclersize);
        colors = findViewById(R.id.recyclercolor);
        upload_image_price = new Upload_image_price();
        edit_price = findViewById(R.id.edit_prc);
        edit_made = findViewById(R.id.edit_Made);
        edit_description = findViewById(R.id.edit_description);
        size_shoes = new ArrayList<>();
        color_shoes = new ArrayList<>();
        adapter_color = new Adapter_color();
        adapter_size = new Adapter_size();
    }
}
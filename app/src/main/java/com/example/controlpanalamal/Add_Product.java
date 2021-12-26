package com.example.controlpanalamal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Add_Product extends AppCompatActivity implements background_Changed {
RecyclerView recyclerView1 , recyclerView2;
Adapter_Add_type adapter_add_type;
Adapter_Add_size adapter_add_size;
ArrayList<String> type;
String Type;
Upload_image_price upload_image_price;
FirebaseDatabase firebaseDatabase;
AlertDialog.Builder alert;
AlertDialog alertDialog;
DatabaseReference databaseReference;
StorageReference storageReference;
boolean[] selected_size = new boolean[32];
boolean[] selected_type = new boolean[10];
Button Add_product;
Uri uri;
int Price ;
FcmSender fcmSender;
TextView image_shoes;
boolean is_selected_Size = false;
EditText price , Made , Description , color;
ImageButton imageButton;
ArrayList<Integer> sizes , SizesSelected;
ArrayList<String> colors;
ImageView imageShoes;
ChipGroup chipGroup;
Chip chip;
Bundle bundle;
int ID;
private static final String TAG = "MainActivity";
public static final String TOPIC = "/topics/deals";
View.OnClickListener click_listener;
GradientDrawable gradientDrawable , gradientDrawable2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);
        init();
        SetAdapterType();
        SetAdapterSize();
        Add_color();
        ADD_PRODUCT();
        image_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK)
        {
            // fetch data from gallery //
            uri = data.getData();
            imageShoes.setImageURI(uri);
            image_shoes.setVisibility(View.INVISIBLE);
        }
    }
    private void SetAdapterSize() {
    for (int i =18 ; i<=48 ; i++)
        sizes.add(i);
    adapter_add_size.setSize(sizes,(background_Changed) this);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
            3,RecyclerView.HORIZONTAL,false);
    recyclerView2.setLayoutManager(layoutManager);
    recyclerView2.setAdapter(adapter_add_size);
    }
    private void ADD_PRODUCT() {

    Add_product.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Connected.IsConnect();
            //if(!Connected.isConnect)
              //  Toast.makeText(getApplicationContext(),"Error in Connection",Toast.LENGTH_LONG).show();
             if(price.getText().toString().isEmpty())
                Toast.makeText(getApplicationContext(),"Enter Price please",Toast.LENGTH_LONG).show();
            else if(uri==null)
                Toast.makeText(getApplicationContext(),"Image Not Found",Toast.LENGTH_LONG).show();
            else if(Made.getText().toString().isEmpty())
                Toast.makeText(getApplicationContext(),"Enter Made please",Toast.LENGTH_LONG).show();
            else if (!is_selected_Type())
                Toast.makeText(getApplicationContext(),"Enter one Type please",Toast.LENGTH_LONG).show();
            else if(!is_Size()) {
                Toast.makeText(getApplicationContext(),"No Size Selecte yet",Toast.LENGTH_SHORT).show();
            }
            else if(chipGroup.getChildCount()==0){
                Toast.makeText(getApplicationContext(),"Enter One Color Please",Toast.LENGTH_SHORT).show();
            }
            else
            {
                fcmSender = new FcmSender("/topics/all","Add Shoes","The Type is : " + Type,getApplicationContext(),Add_Product.this);
                fcmSender.SendNotifications();
                LayoutInflater li = LayoutInflater.from(Add_Product.this);
                View view = li.inflate(R.layout.alert_dialog_progress,null);
                alert = new AlertDialog.Builder(Add_Product.this);
                alert.setView(view);
                alertDialog = alert.create();
                alertDialog.show();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.hasChild("CountShoes")){
                            databaseReference.child("CountShoes").setValue(0);
                            ID = 0;
                        }
                        else{
                            ID = snapshot.child("CountShoes").getValue(Integer.class);
                        }
                        storageReference.child("Shoes").child(String.valueOf(ID)).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.child("Shoes").child(String.valueOf(ID)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                int id = ID + 1;
                                                databaseReference.child("CountShoes").setValue(id);
                                                Price = Integer.parseInt(price.getText().toString());
                                                upload_image_price.setPrice(Price);
                                                upload_image_price.setImage(task.getResult().toString());
                                                databaseReference.child("InfoShoes").child(String.valueOf(ID)).child(Type)
                                                        .push().setValue(upload_image_price);
                                                databaseReference.child("InfoShoes").child(String.valueOf(ID)).child(Type).child("more").
                                                        child("Made").setValue(Made.getText().toString());
                                                for (int i = 0; i < SizesSelected.size(); i++) {
                                                    databaseReference.child("InfoShoes").child(String.valueOf(ID)).child(Type).child("more").
                                                            child("Sizes").child(String.valueOf(i)).setValue(SizesSelected.get(i));
                                                }
                                                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                                                    Chip chip1 = (Chip) chipGroup.getChildAt(i);
                                                    databaseReference.child("InfoShoes").child(String.valueOf(ID)).child(Type).child("more").
                                                            child("colors").child(String.valueOf(i)).setValue(chip1.getText());
                                                }
                                                databaseReference.child("InfoShoes").child(String.valueOf(ID)).child(Type).child("more")
                                                        .child("description").setValue(Description.getText().toString());
                                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                Add_product.setClickable(true);
                                                MainActivity.main.finish();
                                                finish();
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        }
    });
    }
    private boolean is_Size() {
        for(int i=0;i<31;i++){
            if(selected_size[i])
            {
                is_selected_Size = true;
                SizesSelected.add(i+18);
            }
        }
        if(is_selected_Size)
            return true;
        return false;
    }
    private boolean is_selected_Type(){
        for(int i=0;i<10;i++){
            if(selected_type[i]) {
                Type = type.get(i);
                return true;
            }
        }
        return false;
    }
    private void SetAdapterType() {
    type.add("Men");
    type.add("Women");
    type.add("Boy");
    type.add("Girl");
    type.add("Batik");
    type.add("Baby");
    adapter_add_type.setType(type,(background_Changed)this);
    recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
    recyclerView1.setAdapter(adapter_add_type);
    }
    private void Add_color(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chip = (Chip)getLayoutInflater().inflate(R.layout.add_chips,null,false);
                if(color.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Color Please",Toast.LENGTH_LONG).show();
                }
                else {
                    chip.setText(color.getText().toString());
                    color.setText("");
                    color.setHint(R.string.colors);
                    chip.setOnCloseIconClickListener(click_listener);
                    chipGroup.addView(chip);
                }
            }
        });
        click_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(v);
            }
        };
    }
    public void init() {
        recyclerView1 = findViewById(R.id.recycler_Type);
        recyclerView2 = findViewById(R.id.recycler_AddSize);
        adapter_add_type = new Adapter_Add_type();
        adapter_add_size = new Adapter_Add_size();
        type = new ArrayList<>();
        sizes = new ArrayList<>();
        SizesSelected = new ArrayList<>();
        colors = new ArrayList<>();
        bundle = getIntent().getExtras();
        upload_image_price = new Upload_image_price();
        chipGroup = findViewById(R.id.chip_group);
        Add_product = findViewById(R.id.Addproduct);
        gradientDrawable = new GradientDrawable();
        gradientDrawable2 = new GradientDrawable();
        price = findViewById(R.id.add_price);
        Description = findViewById(R.id.add_Description);
        Made = findViewById(R.id.add_made);
        color = findViewById(R.id.add_color);
        imageShoes = findViewById(R.id.image_shoes_Added);
        image_shoes = findViewById(R.id.Add_image_shoes);
        imageButton = findViewById(R.id.click_button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        for (int i = 0; i < 31; i++) {
           selected_size[i] = false;
        }
        for (int i = 0; i < 10; i++) {
            selected_type[i] = false;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void clickButtonSize(View v , int pos , String s) {
        if(s.equals("one")){
            selected_size[pos] = true;
            v.setBackground(getResources().getDrawable(R.drawable.circle_two));
        }
        else{
            selected_size[pos] = false;
            v.setBackground(getResources().getDrawable(R.drawable.circle_one));
        }
    }
    @Override
    public void clickButtonType(View v, int pos, String s) {
        if(s.equals("one")){
                selected_type[pos] = true;
                v.setBackground(getResources().getDrawable(R.drawable.circle_two));
        }
        else{
            selected_type[pos] = false;
            v.setBackground(getResources().getDrawable(R.drawable.circle_one));
        }
    }
}

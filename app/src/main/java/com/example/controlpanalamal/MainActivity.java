package com.example.controlpanalamal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.SnapshotHolder;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView rec1;
    FirebaseMessaging firebaseMessaging;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    AdapterShoes adapterShoes;
    public static  Activity main;
    Upload_image_price upload_image_price;
    ArrayList<Shoes> shoes = new ArrayList<>();
    ProgressBar progressBar;
    public static final String Channel_ID = "simplified_coding";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        cheakFoundChild();
    }
    private void cheakFoundChild() {
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.hasChild("CountShoes")) {
                getShoesFromsDataBase();
            } else {
                Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }
    private void getShoesFromsDataBase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("CountShoes").getValue(Integer.class) > 0) {
                    getData();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void getData() {
        databaseReference.child("InfoShoes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                getDataMore(snapshot.getKey());
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

    private void getDataMore(String key) {
        databaseReference.child("InfoShoes").child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String Type = snapshot.getKey();
                getPriceAndImage(key,Type);
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

    private void getPriceAndImage(String key, String type) {
    databaseReference.child("InfoShoes").child(key).child(type).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if(!snapshot.getKey().equals("more")) {
                upload_image_price = snapshot.getValue(Upload_image_price.class);
                String Url = upload_image_price.getImage();
                int Price = upload_image_price.getPrice();
                shoes.add(new Shoes(type, Url, Price, key));
                setAdapter(shoes);
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

    private void setAdapter(ArrayList<Shoes> shoes) {
        adapterShoes.setList(shoes);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rec1.setLayoutManager(mLayoutManager);
        rec1.setAdapter(adapterShoes);
        progressBar.setVisibility(View.GONE);
     }

    private void init() {
        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        adapterShoes = new AdapterShoes();
        firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("all");
        upload_image_price = new Upload_image_price();
        progressBar = findViewById(R.id.progHome);
        rec1 = findViewById(R.id.recycler1);
        database = FirebaseDatabase.getInstance("https://fir-nabeel-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference();
        main = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     }

    @Override
    public void onClick(View v) {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification, menu);
        return true;
    }
        @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       if(menuItem.getItemId()==R.id.addProduct){
           Intent intent = new Intent(getApplicationContext(),Add_Product.class);
           startActivity(intent);
       }
       if(menuItem.getItemId()==R.id.home){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
       if(menuItem.getItemId()==R.id.aboutUs){

       }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        if (id == R.id.notification) {
            Intent intent = new Intent(getApplicationContext(),Notification_recycle.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.dashboard)
        {
            Intent intent = new Intent(getApplicationContext(),Dashboard.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
}
package com.example.controlpanalamal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Notification_recycle extends AppCompatActivity {
  RecyclerView recyclerView;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference;
  ProgressBar progressBar;
  DataBuying dataBuying;
  ArrayList<DataBuying> arrayList;
  Adapter_notification adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_recycle);
        init();
        getDataBaseFromDB();
    }
    private void getDataBaseFromDB() {
    databaseReference.child("Customers").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            getData(snapshot.getKey());
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

    private void getData(String key) {
        databaseReference.child("Customers").child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               getDataUser(key,snapshot.getKey());
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

    private void getDataUser(String key1 , String key) {
    databaseReference.child("Customers").child(key1).child(key).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            dataBuying = snapshot.getValue(DataBuying.class);
            arrayList.add(dataBuying);
            adapter.setList(arrayList);
            setAdapter();
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

    private void setAdapter() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
    recyclerView.setAdapter(adapter);
    progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        /*Connected.IsConnect();
        if(!Connected.isConnect){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Error in Connection",Toast.LENGTH_LONG).show();
        }*/
        super.onResume();
    }

    void init(){
     recyclerView = findViewById(R.id.recycle_notify);
     firebaseDatabase = FirebaseDatabase.getInstance();
     databaseReference = firebaseDatabase.getReference();
     progressBar = findViewById(R.id.prog_notify);
     dataBuying = new DataBuying();
     arrayList = new ArrayList<>();
     adapter = new Adapter_notification();
    }
}
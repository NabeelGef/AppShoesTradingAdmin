package com.example.controlpanalamal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    AdapterDashboard adapterDashboard;
    ArrayList<Profile>profiles;
    testUpload profile;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        getInformationFromDataBase();
    }
    private void getInformationFromDataBase() {
        databaseReference.child("Profiles").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                getMoreData(snapshot.getKey());

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

    @Override
    protected void onResume() {
        /*Connected.IsConnect();
        if(!Connected.isConnect){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Error in Connection",Toast.LENGTH_LONG).show();
        }*/
        super.onResume();
    }

    private void getMoreData(String key) {
        databaseReference.child("Profiles").child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                profile = snapshot.getValue(testUpload.class);
                String Name = profile.getName();
                String NumberTel = profile.getNumberTel();
                String Uri = profile.getUri();
                profiles.add(new Profile(Uri, Name, NumberTel));
                setAdapter(profiles);
                progressBar.setVisibility(View.GONE);
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
    private void setAdapter(ArrayList<Profile>profiles) {
    adapterDashboard.setList(profiles);
    adapterDashboard.notifyDataSetChanged();
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
    recyclerView.setAdapter(adapterDashboard);
    }

    private void init(){
        firebaseDatabase = FirebaseDatabase.getInstance("https://fir-nabeel-default-rtdb.firebaseio.com/");
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = firebaseStorage.getReference();
        profiles = new ArrayList<>();
        profile = new testUpload();
        progressBar = findViewById(R.id.prog_Dash);
        adapterDashboard = new AdapterDashboard();
        recyclerView = findViewById(R.id.recycler_Profile);
    }
}
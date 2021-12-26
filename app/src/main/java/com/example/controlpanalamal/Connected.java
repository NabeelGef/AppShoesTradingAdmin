package com.example.controlpanalamal;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.http.Body;

public class Connected {
    public static boolean isConnect;
    public  static DatabaseReference databaseReference;
    public static void IsConnect(){
    databaseReference = FirebaseDatabase.getInstance().getReference(".info/connected");
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
       isConnect = snapshot.getValue(Boolean.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }
}

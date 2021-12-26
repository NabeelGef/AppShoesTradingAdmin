package com.example.controlpanalamal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AdapterShoes extends RecyclerView.Adapter<AdapterShoes.ViewHolder> {
    ArrayList<Shoes> shoes = new ArrayList<>();
    boolean start = false;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.addedshoes,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(shoes.get(position).getUrl()).into(
                holder.imageView1);
        holder.price.append("" + shoes.get(position).getPrice() + "SYR");
        holder.type.append("" + shoes.get(position).getType());
        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID",shoes.get(position).getKey());
                Intent intent = new Intent(holder.itemView.getContext(),Edit_shoes.class);
                intent.putExtras(bundle);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteClicker(holder,position);
            }
        });

    }

    private void DeleteClicker(ViewHolder holder, int position) {
           AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
            alert.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    start = true;
                    Delete_from_DB(position);
                    dialog.cancel();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
    private void Delete_from_DB(int position) {
        databaseReference.child("InfoShoes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (start && shoes.get(position).getKey().equals((snapshot.getKey()))) {
                    shoes.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, shoes.size());
                    deleteElement(snapshot.getKey());
                    start = false;
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
        databaseReference.child("CountShoes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EditCountShoes(snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void EditCountShoes(int value) {
        value--;
     databaseReference.child("CountShoes").setValue(value);
    }

    private void deleteElement(String key) {
    databaseReference.child("InfoShoes").child(key).removeValue();
    }

    @Override
    public int getItemCount() {
        return shoes.size();
    }

    public void setList(ArrayList<Shoes> shoes) {
    this.shoes = shoes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1;
        TextView price , type ;
        ImageButton imageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.image1);
            price = itemView.findViewById(R.id.price);
            type = itemView.findViewById(R.id.type);
            imageButton = itemView.findViewById(R.id.delete_shoes);
        }
    }
}

package com.example.controlpanalamal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter_color extends RecyclerView.Adapter<Adapter_color.ViewHolder> {
    ArrayList<String> colors = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String element , element_new;
    String id , type;
    boolean start;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_color.ViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.color_recycle, parent, false));
        }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.color_shoes.setText(""+colors.get(position));
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Clicker(holder,position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_Clicker(holder,position);
            }
        });
    }

    private void Delete_Clicker(ViewHolder holder, int position) {
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

        databaseReference.child("InfoShoes").child(id).child(type).child("more").child("colors").
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(colors.get(position).equals(snapshot.getValue(String.class))&&start){
                            colors.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, colors.size());
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
    }

    private void deleteElement(String key) {
        databaseReference.child("InfoShoes").child(id).child(type).child("more").child("colors").
                child(key).removeValue();
    }
    private void Edit_Clicker(ViewHolder holder, int position) {
        element = colors.get(position);
        LayoutInflater li = LayoutInflater.from(holder.itemView.getContext());
        View view = li.inflate(R.layout.alert_dialog_edit,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setView(view);
        EditText edit_shoes = view.findViewById(R.id.edit_shoes);
        edit_shoes.setHint("enter a new Color please : ");
        edit_shoes.setInputType(InputType.TYPE_CLASS_TEXT);
        Button edit = view.findViewById(R.id.Click_Edit);
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
        Button cancel = view.findViewById(R.id.Click_Cancel);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_shoes.getText().toString().equals(""))
                    Toast.makeText(holder.itemView.getContext(),
                            "Please edit shoes",Toast.LENGTH_SHORT).show();
                else if (edit_shoes.getText().toString().equals(String.valueOf(element)))
                    Toast.makeText(holder.itemView.getContext(),
                            "You aren't editing shoes",Toast.LENGTH_SHORT).show();
                else {
                    element_new = edit_shoes.getText().toString();
                    colors.set(position, element_new);
                    Edit_the_DB();
                    holder.color_shoes.setText(""+colors.get(position));
                    alertDialog.cancel();
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

    private void Edit_the_DB() {
        databaseReference.child("InfoShoes").child(id).child(type).child("more").child("colors").
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(element == snapshot.getValue(String.class)){
                            editElement(snapshot.getKey());
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

    private void editElement(String key) {
        databaseReference.child("InfoShoes").child(id).child(type).child("more").child("colors").
                child(key).setValue(element_new);
    }


    @Override
    public int getItemCount() {
            return colors.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
     TextView  color_shoes ;
     Button edit , delete ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        color_shoes =itemView.findViewById(R.id.Color);
        edit = itemView.findViewById(R.id.edit_color);
        delete = itemView.findViewById(R.id.delete_color);
        }
    }
    public void setListColor(ArrayList<String>Colors,String ID ,String Type)
    {
        this.colors = Colors;
        this.id = ID;
        this.type = Type;
    }
}

package com.example.controlpanalamal;

import android.graphics.drawable.GradientDrawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Add_size extends RecyclerView.Adapter<Adapter_Add_size.Viewholder> {
    private final SparseBooleanArray array=new SparseBooleanArray();
    ArrayList<Integer> size  = new ArrayList<>();
    background_Changed background_changed;
    View view;
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycle_type,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
       holder.textView.setText(""+size.get(position));
               view = holder.textView;
               if(array.get(position)) {
                   background_changed.clickButtonSize(holder.textView, position , "one");
               }
               else {
                   background_changed.clickButtonSize(holder.textView, position , "two");
               }
           }
    @Override
    public int getItemCount() {
        return size.size();
    }
    public  class Viewholder extends RecyclerView.ViewHolder {
       TextView textView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Data_type);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!array.get(getAdapterPosition()))
                        array.put(getAdapterPosition(),true);
                    else
                        array.put(getAdapterPosition(),false);
                    notifyDataSetChanged();
                }
            });
        }
    }
    public void setSize(ArrayList<Integer> size1 , background_Changed background_changed1){
        this.size = size1;
        this.background_changed = background_changed1;
    }
    public View getView() {
        return view;
    }
}

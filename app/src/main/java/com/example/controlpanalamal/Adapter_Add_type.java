package com.example.controlpanalamal;

import android.graphics.drawable.GradientDrawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Add_type extends RecyclerView.Adapter<Adapter_Add_type.ViewHolder> {
    ArrayList<String>type = new ArrayList<>();
    final private  SparseBooleanArray array = new SparseBooleanArray();
    boolean[] selectType = new boolean[8];
    background_Changed background_changed;
    int Position = -1;
    String Type = "";
    View view;
    GradientDrawable gradientDrawable = new GradientDrawable();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycle_type,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.textView.setText(type.get(position));
      view = holder.textView;
              if(array.get(position)) {
                  background_changed.clickButtonType(holder.textView,position,"one");
              }
              else{
                  background_changed.clickButtonType(holder.textView,position,"two");
              }
          }
    @Override
    public int getItemCount() {
        return type.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Data_type);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(array.get(getAdapterPosition())){
                        array.put(getAdapterPosition(),false);
                    }
                    else{
                        if(CheakArray())
                          array.put(getAdapterPosition(),true);
                        else{
                            Toast.makeText(itemView.getContext(),"You are selecting Type yet",Toast.LENGTH_LONG).show();
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

        private boolean CheakArray() {
        for(int i=0;i<type.size();i++){
         if(array.get(i)){
             return false;
         }
        }
        return true;
        }
    }
    public void setType(ArrayList<String> type1 , background_Changed background_changed1)
    {
        this.type = type1;
        this.background_changed = background_changed1;
    }
    public String getType() {
        return Type;
    }

    public View getView() {
        return view;
    }
}

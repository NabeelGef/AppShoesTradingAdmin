package com.example.controlpanalamal;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter_notification extends RecyclerView.Adapter<Adapter_notification.ViewHolder> {
   ArrayList <DataBuying> dataBuyings;
    @NonNull
    @Override
    public Adapter_notification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.add_notify,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_notification.ViewHolder holder, int position) {
      holder.Name.setText("name : " + dataBuyings.get(position).getName());
      holder.Size.setText("size : " +dataBuyings.get(position).getSize());
      holder.Color.setText("color :"+dataBuyings.get(position).getColor());
        Glide.with(holder.itemView.getContext()).
                load(dataBuyings.get(position).getUrl()).into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return dataBuyings.size();
    }

    public void setList(ArrayList<DataBuying> arrayList) {
    this.dataBuyings = arrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name , Color , Size ;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name_notify);
            Color = itemView.findViewById(R.id.Color_notify);
            Size = itemView.findViewById(R.id.Size_notify);
            imageView = itemView.findViewById(R.id.image_shoes_notify);
        }
    }
}

package com.example.controlpanalamal;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.ViewHolder> {
   ArrayList<Profile> profiles = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.dashboard_recycle,parent,false));
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(profiles.get(position).getUri()).
                into(holder.profile);
        holder.name.setText("name: " + profiles.get(position).getName());
        holder.numberTel.setText("numberTel: " + profiles.get(position).getNumberTel());
    }
    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void setList(ArrayList<Profile> profiles) {
   this.profiles = profiles;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name , numberTel ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_photo);
            name = itemView.findViewById(R.id.name_customer);
            numberTel = itemView.findViewById(R.id.numberTel_customer);
           }

    }
}

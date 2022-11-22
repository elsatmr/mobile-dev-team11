package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team11.R;

public class RestaurantDishViewHolder extends RecyclerView.ViewHolder{
    public ImageView restDishImageView;
    public TextView restNameTextView;

    public RestaurantDishViewHolder(@NonNull View itemView) {
        super(itemView);
        this.restDishImageView = itemView.findViewById(R.id.restDishImageView);
        this.restNameTextView = itemView.findViewById(R.id.restNameTextView);
    }
}
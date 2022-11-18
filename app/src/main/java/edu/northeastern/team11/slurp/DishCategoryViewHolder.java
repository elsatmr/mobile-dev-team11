package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team11.R;

public class DishCategoryViewHolder extends RecyclerView.ViewHolder{
    public ImageView dishCategoryImageView;

    public DishCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.dishCategoryImageView = itemView.findViewById(R.id.dishCategoryImageView);

    }
}
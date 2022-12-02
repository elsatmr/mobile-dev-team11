package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team11.R;

public class DishCategoryViewHolder extends RecyclerView.ViewHolder{
    public ImageView dishCategoryImageView;
    public TextView dishCategoryLabel;

    public DishCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.dishCategoryImageView = itemView.findViewById(R.id.postImageView);
        this.dishCategoryLabel = itemView.findViewById(R.id.postUserNameText);
    }
}
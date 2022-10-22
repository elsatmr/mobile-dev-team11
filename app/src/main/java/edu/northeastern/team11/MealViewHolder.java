package edu.northeastern.team11;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealViewHolder extends RecyclerView.ViewHolder{
    public TextView mealTitle;
    public ImageView mealPhoto;
    public TextView mealDescription;
    public TextView mealIsEasyTV;
    public ImageView mealIsEasyIcon;
    public List<String> mealTags;

    public MealViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mealTitle = itemView.findViewById(R.id.tvTitle);
        this.mealPhoto = itemView.findViewById(R.id.ivMeal);
        this.mealDescription = itemView.findViewById(R.id.tvDescription);
        this.mealIsEasyTV = itemView.findViewById(R.id.tvEasy);
        this.mealIsEasyIcon = itemView.findViewById(R.id.ivEasy);
    }
}

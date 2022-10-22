package edu.northeastern.team11;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodViewHolder extends RecyclerView.ViewHolder{
    public TextView foodTitle;
    public ImageView foodPhoto;
    //public TextView foodIsEasyTV;
    //public ImageView foodIsEasyIcon;
    //public List<String> mealTags;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        this.foodTitle = itemView.findViewById(R.id.tvTitle);
        this.foodPhoto = itemView.findViewById(R.id.ivMeal);
        //this.foodIsEasyTV = itemView.findViewById(R.id.tvEasy);
        //this.foodIsEasyIcon = itemView.findViewById(R.id.ivEasy);
    }
}

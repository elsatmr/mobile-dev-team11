package edu.northeastern.team11;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    private final List<Food> foodList;
    private final Context context;

    public FoodAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(context).inflate(R.layout.food_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.foodTitle.setText(foodList.get(position).getmStrMeal());
        String imageUri = foodList.get(position).getmStrMealThumb();
        Picasso.get().load(imageUri).into(holder.foodPhoto);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

}

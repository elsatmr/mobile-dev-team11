package edu.northeastern.team11.slurp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.team11.R;
import edu.northeastern.team11.Sticker;
import edu.northeastern.team11.StickerScreen;
import edu.northeastern.team11.StickerViewHolder;

public class DishCategoryAdapter extends RecyclerView.Adapter<DishCategoryViewHolder>{

    private final List<DishCategory> dishCategoryList;
    private final Context context;

    public DishCategoryAdapter(List<DishCategory> dishCategoryList, Context context) {
        this.dishCategoryList = dishCategoryList;
        this.context = context;
    }


    @NonNull
    @Override
    public DishCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishCategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_dish_cateogry_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishCategoryViewHolder holder, int position) {
        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            String dishCategoryId = String.valueOf(position);
            Intent intent = new Intent(context, DishesMapListScreen.class);
            intent.putExtra("category", "Cuisine "+dishCategoryId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dishCategoryList.size();
    }
}

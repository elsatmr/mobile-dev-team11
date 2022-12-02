package edu.northeastern.team11;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    private final List<Food> foodList;
    private final Context context;
    private int chipID;

    public FoodAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodViewHolder(LayoutInflater.from(context).inflate(R.layout.food_view, parent, false));
    }

    private void addChip(String tag, ChipGroup chipGroup){
        Chip newChip = new Chip(context);
        newChip.setId(chipID);
        chipID++;
        newChip.setText(tag);
        newChip.setTextColor(Color.parseColor("#FFFFFF"));
        newChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FF8A74BC")));
        chipGroup.addView(newChip);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.foodTitle.setText(foodList.get(position).getmStrMeal().replaceAll("\"", ""));
        List<String> tags = new ArrayList<String>(Arrays.asList(foodList.get(position).getmStrTags().split(",")));
        holder.mealTags.removeAllViews();
        for (String tag: tags) {
            if (!tag.equals("null")) {
                addChip(tag.replaceAll("\"", ""), holder.mealTags);
            }
        }
        System.out.println(foodList.get(position).getmStrTags());

        String url = foodList.get(position).getmStrMealThumb().replaceAll("\"", "");
        BackgroundThread thread = new BackgroundThread(holder, url);
        thread.start();
    }


    class BackgroundThread extends Thread {
        FoodViewHolder threadHolder;
        String imageUri;

        BackgroundThread(FoodViewHolder threadHolder, String url) {
            this.threadHolder = threadHolder;
            this.imageUri = url;
        }

        @Override
        public void run() {
            try
            {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                threadHolder.foodPhoto.post(new Runnable()
                {
                    public void run()
                    {
                        if(bitmap !=null)
                        {
                            Log.d("THREAD", "image not null");
                            threadHolder.foodPhoto.setImageBitmap(bitmap);
                        }
                    }
                });
            } catch (MalformedURLException e)
            {
                Log.d("THREAD", e.getMessage());
                System.out.println("The URL is not valid.");
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

}

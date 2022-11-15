package edu.northeastern.team11.slurp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.northeastern.team11.R;
import edu.northeastern.team11.Sticker;
import edu.northeastern.team11.StickerAdapter;
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
        String currentCategory = dishCategoryList.get(position).getCategoryName();
        BackgroundThread thread = new BackgroundThread(holder, currentCategory);
        thread.start();
        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();
            String dishCategoryId = String.valueOf(position);
            Intent intent = new Intent(context, DishesMapListScreen.class);
            intent.putExtra("category", "Cuisine "+dishCategoryId);
            context.startActivity(intent);
        });
    }


    class BackgroundThread extends Thread {
        DishCategoryViewHolder threadHolder;
        String imageUri;

        BackgroundThread(DishCategoryViewHolder threadHolder, String url) {
            this.threadHolder = threadHolder;
            this.imageUri = url;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                threadHolder.dishCategoryImageView.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            threadHolder.dishCategoryImageView.setImageBitmap(bitmap);
                        }
                    }
                });
            } catch (MalformedURLException e) {
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
        return dishCategoryList.size();
    }
}

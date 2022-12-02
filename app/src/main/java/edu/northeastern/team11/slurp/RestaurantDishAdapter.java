package edu.northeastern.team11.slurp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import edu.northeastern.team11.HomeFragment;
import edu.northeastern.team11.R;

public class RestaurantDishAdapter extends RecyclerView.Adapter<RestaurantDishViewHolder> {

    private final List<RestaurantDish> restDishList;
    private final Context context;
    ViewGroup parentView;
    edu.northeastern.team11.databinding.SlurpActivityMainBinding binding;

    public RestaurantDishAdapter(List<RestaurantDish> restDishList, Context context) {
        this.restDishList = restDishList;
        this.context = context;
    }


    @NonNull
    @Override
    public RestaurantDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentView = parent;
        return new RestaurantDishViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_restaurant_dish_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDishViewHolder holder, int position) {
        RestaurantDish r1 = restDishList.get(position);
        String imageUrl = r1.getRestImageUrl();
        String restName = r1.getRestName();
        String street = r1.getStreet();
        String city = r1.getCity();
        String state = r1.getState();
        String zip = r1.getZip();
        String reviewCount = String.valueOf(r1.getReviewCount());
        String score = String.valueOf(r1.getSlurpScore());
        BackgroundThread thread = new BackgroundThread(holder, imageUrl, restName);
        holder.restNameTextView.setText(restName);
        holder.streetText.setText(street);
        holder.cityText.setText(city);
        holder.stateText.setText(state);
        holder.zipText.setText(zip);
        holder.reviewCount.setText("Reviews: " + reviewCount);
        String dishName = r1.getDishName();
        holder.slurpScore.setText(dishName + " score: " + score);
        thread.start();
        holder.itemView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Bundle bundle = new Bundle();

            bundle.putParcelable("restDish", (Parcelable) r1);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, RestaurantDishFragment.class, bundle).commit();
        });
    }


    class BackgroundThread extends Thread {
        RestaurantDishViewHolder threadHolder;
        String imageUri;
        String restName;

        BackgroundThread(RestaurantDishViewHolder threadHolder, String url, String restName) {
            this.threadHolder = threadHolder;
            this.imageUri = url;
            this.restName = restName;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                threadHolder.restDishImageView.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            threadHolder.restDishImageView.setImageBitmap(bitmap);
                        }
                    }
                });
//                threadHolder.dishCategoryLabel.setText(label);
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
        return restDishList.size();
    }
}
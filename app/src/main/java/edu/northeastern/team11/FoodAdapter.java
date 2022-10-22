package edu.northeastern.team11;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;


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
        return new FoodViewHolder(LayoutInflater.from(context).inflate(R.layout.food_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.foodTitle.setText(foodList.get(position).getmStrMeal());
        String imageUri = foodList.get(position).getmStrMealThumb().replace("http:", "https:");

        //Picasso.get().load(imageUri).resize(126, 126)

        Glide.with(context).load(imageUri).override(126, 126).into(holder.foodPhoto);

//        final OkHttpClient client = new OkHttpClient.Builder()
//                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
//                .build();
//
//        final Picasso picasso = new Picasso.Builder(context)
//                .downloader(new OkHttp3Downloader(client))
//                .build();
//
//        Picasso.setSingletonInstance(picasso);
//
//        Picasso.get().load(imageUri).into(holder.foodPhoto, new Callback() {
//            @Override
//            public void onSuccess() {
//                Log.d("PICASSO", "Success");
//            }
//            @Override
//            public void onError(Exception e) {
//                Log.d("PICASSO", "Fail");
//                Log.d("ERROR", e.getMessage());
//            }
//        });

//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round)
//                .error(R.mipmap.ic_launcher_round);

        //Glide.with(context).load(imageUri).apply(options).into(holder.foodPhoto);
//        Glide.with(context)
//                .asBitmap()
//                .load(imageUri)
//                .into(holder.foodPhoto);

//        Glide.with(context)
//                .load(imageUri)
//                .override(126, 126)
//                .centerCrop()
//                .into(holder.foodPhoto);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

}

package edu.northeastern.team11.slurp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.northeastern.team11.R;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsViewHolder> {
    private final ArrayList<Dish> dishesList;
    private final Context context;

    public MyPostsAdapter(ArrayList<Dish> dishesList, Context context) {
        this.dishesList = dishesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyPostsViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_my_post_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position) {
        Dish currDish = dishesList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        String url = currDish.getImageUrl();
        BackgroundThread thread = new BackgroundThread(holder, url);
        thread.start();
    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    class BackgroundThread extends Thread {
        MyPostsViewHolder threadHolder;
        String imageUri;

        BackgroundThread(MyPostsViewHolder threadHolder, String url) {
            this.threadHolder = threadHolder;
            this.imageUri = url;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                threadHolder.postImageView.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            threadHolder.postImageView.setImageBitmap(bitmap);
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
}

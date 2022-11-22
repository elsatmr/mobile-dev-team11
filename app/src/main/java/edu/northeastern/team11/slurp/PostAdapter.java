package edu.northeastern.team11.slurp;

import android.content.Context;
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
import java.util.ArrayList;

import edu.northeastern.team11.R;
import edu.northeastern.team11.StickerAdapter;
import edu.northeastern.team11.StickerViewHolder;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private final ArrayList<Dish> dishesList;
    private final Context context;

    public PostAdapter(ArrayList<Dish> dishesList, Context context) {
        this.dishesList = dishesList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_post_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Dish currDish = dishesList.get(position);
        String url = currDish.getImageUrl();
        BackgroundThread thread = new BackgroundThread(holder, url);
        thread.start();
    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    class BackgroundThread extends Thread {
        PostViewHolder threadHolder;
        String imageUri;

        BackgroundThread(PostViewHolder threadHolder, String url) {
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

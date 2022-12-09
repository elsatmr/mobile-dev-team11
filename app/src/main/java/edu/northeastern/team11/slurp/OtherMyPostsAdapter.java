package edu.northeastern.team11.slurp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.northeastern.team11.R;

public class OtherMyPostsAdapter extends RecyclerView.Adapter<OtherMyPostsViewHolder> {
    private final ArrayList<Dish> dishesList;
    private final Context context;


    public OtherMyPostsAdapter(ArrayList<Dish> dishesList, Context context) {
        this.dishesList = dishesList;
        this.context = context;
    }

    @NonNull
    @Override
    public OtherMyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OtherMyPostsViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_my_post_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OtherMyPostsViewHolder holder, int position) {
        Dish currDish = dishesList.get(position);
        String url = currDish.getImageUrl();
        OtherMyPostsAdapter.BackgroundThread2 thread = new OtherMyPostsAdapter.BackgroundThread2(holder.postImageView, url);
        thread.start();

    }
//
    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    class BackgroundThread2 extends Thread {
        ImageView imageView;
        String imageUri;

        BackgroundThread2(ImageView imageView, String url) {
            this.imageView = imageView;
            this.imageUri = url;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                imageView.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
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

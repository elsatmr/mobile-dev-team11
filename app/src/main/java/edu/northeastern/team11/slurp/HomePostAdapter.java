package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
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
import java.util.List;

import edu.northeastern.team11.R;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostViewHolder> {
    private final List<HomePostItem> postsList;
    private final Context context;

    public HomePostAdapter(List<HomePostItem> postsList, Context context) {
        this.postsList = postsList;
        this.context = context;
    }


    @NonNull
    @Override
    public HomePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomePostViewHolder((LayoutInflater.from(context).inflate(R.layout.slurp_home_post_item, parent, false)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HomePostViewHolder holder, int position) {
        String imgUrl = postsList.get(position).getImgUrl();
        String dishName = postsList.get(position).getDishName();
        String slurperScore = postsList.get(position).getSlurperScore();
        String author = postsList.get(position).getAuthor();

        holder.dishNameTv.setText(dishName);
        holder.scoreTv.setText("Slurper Score: " + slurperScore);
        holder.authorTv.setText("Posted By: " + author);

        BackgroundThread thread = new BackgroundThread(holder, imgUrl);
        thread.start();

    }




    class BackgroundThread extends Thread {
        HomePostViewHolder threadHolder;
        String imageUri;

        BackgroundThread(HomePostViewHolder threadHolder, String url) {
            this.threadHolder = threadHolder;
            this.imageUri = url;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                threadHolder.postIv.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            // set postIv
                            threadHolder.postIv.setImageBitmap(bitmap);
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
        return postsList.size();
    }
}

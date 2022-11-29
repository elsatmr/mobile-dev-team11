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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.northeastern.team11.R;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder>{

    private final List<Post> postList;
    private final Context context;
    ViewGroup parentView;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentView = parent;
        return new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_post_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post p1 = postList.get(position);
        new BackgroundThread(holder, p1.getImageUrl()).start();
        holder.userName.setText(p1.getUserName());
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy h:mm a");
        Date date = new java.util.Date(p1.getTimestamp()*1000L);
        String formattedDate = sdf.format(date);
        holder.timestamp.setText(formattedDate);
        Log.d("SCORE", p1.getSlurpScore().toString());
        holder.scoreChip.setText(p1.getSlurpScore().toString());
    }

    @Override
    public int getItemCount() {
        return postList.size();
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
                threadHolder.postImage.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            threadHolder.postImage.setImageBitmap(bitmap);
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
}

package edu.northeastern.team11.slurp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialog = LayoutInflater.from(context).inflate(R.layout.voting_dialog, null);
                ImageView dialogImage = dialog.findViewById(R.id.dishImageDialogIV);
                ImageButton favButton = dialog.findViewById(R.id.favButtonDialog);
                favButton.setTag("1");
                String url = currDish.getImageUrl();
                BackgroundThread2 thread = new BackgroundThread2(dialogImage, url);
                thread.start();
                TextView dishNameDialogTV = dialog.findViewById(R.id.dishNameDialogTV);
                dishNameDialogTV.setText(currDish.getDishName());
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        confirmMarkFavorite(view2, favButton);
                    }
                });
                builder.setView(dialog)
                        .setPositiveButton("Close", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        String url = currDish.getImageUrl();
        BackgroundThread2 thread = new BackgroundThread2(holder.postImageView, url);
        thread.start();
    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

    private void confirmMarkFavorite(View currView, ImageButton favButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View favDialog = LayoutInflater.from(context).inflate(R.layout.confirm_fav_dialog, null);
        builder.setView(favDialog)
                .setTitle("Confirm changing your favorite restaurant?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeMarkFavState(favButton);
                    }
                })
                .setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeMarkFavState(ImageButton favButton) {
        if (favButton.getTag() == "1") {
            Log.d("HEART ONE", "HEART ONE");
            favButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
            favButton.setTag("2");
        } else {
            Log.d("HEART TWO", "HEART TWO");
            favButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
            favButton.setTag("1");
        }
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

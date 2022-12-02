package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.team11.R;

public class MyPostsViewHolder extends RecyclerView.ViewHolder {
    public ImageView postImageView;
    public MyPostsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.postImageView = itemView.findViewById(R.id.postImageView);
    }
}

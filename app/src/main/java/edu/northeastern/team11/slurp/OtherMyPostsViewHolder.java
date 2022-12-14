package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team11.R;

// separate viewHolder when viewing another user's profile
public class OtherMyPostsViewHolder extends RecyclerView.ViewHolder {
    public ImageView postImageView;
    public OtherMyPostsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.postImageView = itemView.findViewById(R.id.postImageView);
    }
}

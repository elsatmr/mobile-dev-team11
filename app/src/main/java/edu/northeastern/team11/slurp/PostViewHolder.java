package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import edu.northeastern.team11.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public ImageView postImage;
    public TextView userName;
    public TextView timestamp;
    public Chip scoreChip;



    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        this.postImage = itemView.findViewById(R.id.postImageView);
        this.userName = itemView.findViewById(R.id.postUserNameText);
        this.timestamp = itemView.findViewById(R.id.postDateText);
        this.scoreChip = itemView.findViewById(R.id.postScoreChip);
    }

}

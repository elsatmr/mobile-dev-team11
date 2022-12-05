package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team11.R;

public class HomePostViewHolder extends RecyclerView.ViewHolder {
    public ImageView postIv;
    public TextView dishNameTv;
    public TextView scoreTv;
    public TextView authorTv;

    public HomePostViewHolder(@NonNull View itemView) {
        super(itemView);
        this.postIv = itemView.findViewById(R.id.post_iv);
        this.dishNameTv = itemView.findViewById(R.id.dish_name_tv);
        this.scoreTv = itemView.findViewById(R.id.score_tv);
        this.authorTv = itemView.findViewById(R.id.author_tv);
    }

    public ImageView getPostIv() {
        return postIv;
    }

    public void setPostIv(ImageView postIv) {
        this.postIv = postIv;
    }

    public TextView getDishNameTv() {
        return dishNameTv;
    }

    public void setDishNameTv(TextView dishNameTv) {
        this.dishNameTv = dishNameTv;
    }

    public TextView getScoreTv() {
        return scoreTv;
    }

    public void setScoreTv(TextView scoreTv) {
        this.scoreTv = scoreTv;
    }

    public TextView getAuthorTv() {
        return authorTv;
    }

    public void setAuthorTv(TextView authorTv) {
        this.authorTv = authorTv;
    }
}

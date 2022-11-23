package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import edu.northeastern.team11.R;

public class RestaurantDishViewHolder extends RecyclerView.ViewHolder{
    public ImageView restDishImageView;
    public TextView restNameTextView;
    public TextView streetText;
    public TextView cityText;
    public TextView stateText;
    public TextView zipText;
    public Chip reviewCount;
    public Chip slurpScore;



    public RestaurantDishViewHolder(@NonNull View itemView) {
        super(itemView);
        this.restDishImageView = itemView.findViewById(R.id.restDishImageView);
        this.restNameTextView = itemView.findViewById(R.id.restNameTextView);
        this.streetText = itemView.findViewById(R.id.addressText);
        this.cityText = itemView.findViewById(R.id.cityText);
        this.stateText = itemView.findViewById(R.id.stateText);
        this.zipText = itemView.findViewById(R.id.zipText);
        this.reviewCount = itemView.findViewById(R.id.chipReviewCount);
        this.slurpScore = itemView.findViewById(R.id.chipSlurpScore);
    }

    public Chip getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Chip reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Chip getSlurpScore() {
        return slurpScore;
    }

    public void setSlurpScore(Chip slurpScore) {
        this.slurpScore = slurpScore;
    }

    public ImageView getRestDishImageView() {
        return restDishImageView;
    }

    public void setRestDishImageView(ImageView restDishImageView) {
        this.restDishImageView = restDishImageView;
    }

    public TextView getRestNameTextView() {
        return restNameTextView;
    }

    public void setRestNameTextView(TextView restNameTextView) {
        this.restNameTextView = restNameTextView;
    }

    public TextView getStreetText() {
        return streetText;
    }

    public void setStreetText(TextView streetText) {
        this.streetText = streetText;
    }

    public TextView getCityText() {
        return cityText;
    }

    public void setCityText(TextView cityText) {
        this.cityText = cityText;
    }

    public TextView getStateText() {
        return stateText;
    }

    public void setStateText(TextView stateText) {
        this.stateText = stateText;
    }

    public TextView getZipText() {
        return zipText;
    }

    public void setZipText(TextView zipText) {
        this.zipText = zipText;
    }
}
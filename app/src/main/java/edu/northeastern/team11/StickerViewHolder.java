package edu.northeastern.team11;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

public class StickerViewHolder extends RecyclerView.ViewHolder{
    public ImageView stickerImageView;
    public ChipGroup recSendCount;

    public StickerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.stickerImageView = itemView.findViewById(R.id.stickerImageView);
        this.recSendCount = itemView.findViewById(R.id.chipGroup);
    }
}

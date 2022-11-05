package edu.northeastern.team11;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerViewHolder> {
    private final List<Sticker> stickerList;
    private final Context context;

    public StickerAdapter(List<Sticker> stickerList, Context context) {
        this.stickerList = stickerList;
        this.context = context;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerViewHolder(LayoutInflater.from(context).inflate(R.layout.sticker_vholder, parent, false));
    }

    private void addChip(String count, ChipGroup chipGroup, boolean isRecCount, boolean isSentCount){
        Chip newChip = new Chip(context);
        if (isRecCount) {
            newChip.setText("Rec: " + count);
        }
        if (isSentCount) {
            newChip.setText("Sent: " + count);
        }
        newChip.setTextColor(Color.parseColor("#FFFFFF"));
        newChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FF8A74BC")));
//        chipGroup.addView(newChip);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Sticker currSticker = stickerList.get(position);
        String url = currSticker.getUrlString();
        Log.d("url", url + currSticker.getSentCount());
        addChip(String.valueOf(currSticker.getReceivedCount()), holder.recSendCount, true, false);
        addChip(String.valueOf(currSticker.getSentCount()), holder.recSendCount, false, true);
        BackgroundThread thread = new BackgroundThread(holder, url);
        thread.start();

    }

    class BackgroundThread extends Thread {
        StickerViewHolder threadHolder;
        String imageUri;

        BackgroundThread(StickerViewHolder threadHolder, String url) {
            this.threadHolder = threadHolder;
            this.imageUri = url;
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUri.toString()).getContent());
                threadHolder.stickerImageView.post(new Runnable() {
                    public void run() {
                        if (bitmap != null) {
                            Log.d("THREAD", "image not null");
                            threadHolder.stickerImageView.setImageBitmap(bitmap);
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
        return stickerList.size();
    }
}

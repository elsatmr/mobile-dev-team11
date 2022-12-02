package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.team11.R;

public class MyFriendsViewHolder extends RecyclerView.ViewHolder {
    public TextView myFriendsTextView;
    public MyFriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.myFriendsTextView = itemView.findViewById(R.id.myFriendsTextView);
    }


}

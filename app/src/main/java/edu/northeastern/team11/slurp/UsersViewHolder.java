package edu.northeastern.team11.slurp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.team11.R;

public class UsersViewHolder extends RecyclerView.ViewHolder {
    public TextView userTv;
    public Button button;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        this.userTv = itemView.findViewById(R.id.username_tv);
        this.button = itemView.findViewById(R.id.get_slurper_status_button);
    }

    public TextView getUserTv() {
        return userTv;
    }

    public Button getButton() {
        return button;
    }

}

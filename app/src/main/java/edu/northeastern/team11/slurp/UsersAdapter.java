package edu.northeastern.team11.slurp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.team11.R;

public class UsersAdapter  extends RecyclerView.Adapter<UsersViewHolder>{
    private final List<UsersItem> usersList;
    private final Context context;

    public UsersAdapter(List<UsersItem> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        String username = usersList.get(position).getUsername();
        Button button = usersList.get(position).getAddFriendButton();
        holder.getUserTv().setText(username);
        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update database to add friend
                Log.i("add friend button clicked", "clicked");
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}

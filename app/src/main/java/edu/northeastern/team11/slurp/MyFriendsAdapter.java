package edu.northeastern.team11.slurp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.team11.R;

public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsViewHolder> {
    private ArrayList<String> myFriendsList;
    private final Context context;

    public MyFriendsAdapter(ArrayList<String> myFriendsList, Context context) {
        this.myFriendsList = myFriendsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFriendsViewHolder(LayoutInflater.from(context).inflate(R.layout.slurp_myfriends_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyFriendsViewHolder holder, int position) {
        String currFriend = myFriendsList.get(position);
        Log.d("CURRFRIEND", currFriend);
        holder.myFriendsTextView.setText(currFriend);
        Log.d("HELP", "help");
    }

    @Override
    public int getItemCount() {
        return myFriendsList.size();
    }
}

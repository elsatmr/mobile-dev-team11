package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.northeastern.team11.R;

public class UsersAdapter  extends RecyclerView.Adapter<UsersViewHolder>{
    private final List<UsersItem> usersList;
    private final Context context;
    DatabaseReference db;

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
//        Button button = usersList.get(position).getAddFriendButton();
        holder.getUserTv().setText(username);


        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance().getReference();
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", 0);
                String loggedInUser = sharedPreferences.getString("username", null);
                if (holder.getButton().getText().toString().equals("Add Friend")) {
                    // update database to add friend
//                    String key = db.child("users_slurp").child(loggedInUser).child("friends").push().getKey();
//                    db.child("users_slurp").child(loggedInUser).child("friends").child(key).setValue(username);



                    holder.getButton().setText("Unfriend");


//                    addToDb(username);

//                    Bundle bundle = new Bundle();
//                    bundle.putString("friendAdded", username);
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, SearchUserFragment.class, bundle).commit();

////
//                    FragmentTransaction ft =  activity.getFragmentManager().beginTransaction();
//                    ft.detach(SearchUserFragment.class)

                    // display toast that slurper points have been added

                } else {
                    // update database to remove friend
                    holder.getButton().setText("Add Friend");

                }
            }
        });
//        addToDb(username);





    }

//    private void addToDb(String username) {
//        db = FirebaseDatabase.getInstance().getReference();
//        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", 0);
//        String loggedInUser = sharedPreferences.getString("username", null);
//        String key = db.child("users_slurp").child(loggedInUser).child("friends").push().getKey();
//        db.child("users_slurp").child(loggedInUser).child("friends").child(key).setValue(username);
//
//    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}

//class WritingThread extends Thread {
//    String username;
//    String loggedInUser;
//
//    public WritingThread(String username, String loggedInUser) {
//        this.username = username;
//        this.loggedInUser = loggedInUser;
//    }
//
//    @Override
//    public void run() {
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//
//        String key = db.child("users_slurp").child(loggedInUser).child("friends").push().getKey();
//        db.child("users_slurp").child(loggedInUser).child("friends").child(key).setValue(username);
//
//    }
//
//}

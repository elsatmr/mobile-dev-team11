package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.temporal.ValueRange;
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
        Button button = usersList.get(position).getAddFriendButton();
        holder.getUserTv().setText(username);
        holder.getButton().setText("Get Slurper Status");


        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance().getReference();
                SharedPreferences sharedPreferences = context.getSharedPreferences("settings", 0);
                String loggedInUser = sharedPreferences.getString("username", null);

                db.child("slurperStatusPoints").child(username).child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer totalPoints = Integer.valueOf(task.getResult().getValue().toString());
                            ValueRange babySlurper = ValueRange.of(10, 15);
                            if (babySlurper.isValidIntValue(totalPoints)) {
                                Toast.makeText(context, "Baby Slurper", Toast.LENGTH_LONG).show();
                            }

//                            if (totalPoints < 100) {
//                                Toast.makeText(context, "Baby Slurper", Toast.LENGTH_LONG).show();
//                            }
                        }

                    }
                });

//
//                Toast.makeText(context, "Slurper Status ", Toast.LENGTH_LONG).show();

//                Snackbar.make(v, "Cannot unfriend " + clickedOnUser +
//                        " since you're not currently friends.", Snackbar.LENGTH_LONG).show();


//                if (holder.getButton().getText().toString().equals("Add Friend")) {
//                    // update database to add friend
////                    String key = db.child("users_slurp").child(loggedInUser).child("friends").push().getKey();
////                    db.child("users_slurp").child(loggedInUser).child("friends").child(key).setValue(username);
//
//
//
//                    holder.getButton().setText("Unfriend");
//
//
////                    addToDb(username);
//
////                    Bundle bundle = new Bundle();
////                    bundle.putString("friendAdded", username);
////                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
////                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, SearchUserFragment.class, bundle).commit();
//
//////
////                    FragmentTransaction ft =  activity.getFragmentManager().beginTransaction();
////                    ft.detach(SearchUserFragment.class)
//
//                    // display toast that slurper points have been added
//
//                } else {
//                    // update database to remove friend
//                    holder.getButton().setText("Add Friend");
//
//                }
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

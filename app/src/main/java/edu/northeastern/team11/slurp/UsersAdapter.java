package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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

    // slurper status levels - every user in the app has a Slurper Status Level
    // which is calculated by the total number of points they have (+1 point for
    // adding a friend, +1 making a post, +1 for voting)
    ValueRange babySlurper = ValueRange.of(0, 4);
    ValueRange slurperJr = ValueRange.of(5,9);
    ValueRange slurperSr = ValueRange.of(10,19);
    ValueRange slurperEliteForce = ValueRange.of(20,99);
    ValueRange chiefOfSlurper = ValueRange.of(100, 1000);

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
        holder.getUserTv().setText(username);
        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                // when loggedInUser clicks on "Get Slurper Status" button for a desired user,
                // display that clickedOn user's slurper status in the app
                db = FirebaseDatabase.getInstance().getReference();
                db.child("slurperStatusPoints").child(username).child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            int totalPoints = Integer.parseInt(task.getResult().getValue().toString());
                            if (babySlurper.isValidIntValue(totalPoints)) {
                                Toast.makeText(context, "Baby Slurper", Toast.LENGTH_LONG).show();
                            } else if (slurperJr.isValidIntValue(totalPoints)) {
                                Toast.makeText(context, "Slurper Jr", Toast.LENGTH_LONG).show();
                            } else if (slurperSr.isValidIntValue(totalPoints)) {
                                Toast.makeText(context, "Slurper Sr", Toast.LENGTH_LONG).show();
                            } else if (slurperEliteForce.isValidIntValue(totalPoints)) {
                                Toast.makeText(context, "Slurper Elite Force", Toast.LENGTH_LONG).show();
                            } else if (chiefOfSlurper.isValidIntValue(totalPoints)) {
                                Toast.makeText(context, "Chief Of Slurper", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userClickedOn", username);
                Log.i("line96", username);
                Navigation.findNavController(view).navigate(R.id.otherUserProfileFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
package edu.northeastern.team11.slurp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.maps.Value;

import edu.northeastern.team11.R;

public class OtherSlurperRewardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    DatabaseReference db;
    private String mParam1;
    private String mParam2;
    private String userClickedOn;
    private int totalSlurperPoints;
    private int numPosts;
    public OtherSlurperRewardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SlurperRewardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherSlurperRewardFragment newInstance(String param1, String param2) {
        OtherSlurperRewardFragment fragment = new OtherSlurperRewardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slurper_reward, container, false);

        // get user that was clicked on
        SharedPreferences prefs = getActivity().getSharedPreferences("userClickedOn", Context.MODE_PRIVATE);
        userClickedOn = prefs.getString("userClickedOn", null);

        db = FirebaseDatabase.getInstance().getReference();

        getSlurperStatusPoints(view);
        getNumVotes(view);
        getNumPosts(view);

        return view;
    }

    private void getNumPosts(View view) {
        // read from db here to get numPoints once this is set up in db
        TextView slurperNumPostsTV = view.findViewById(R.id.numPostsTextView);
        db.child("numPosts").child(userClickedOn).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = String.valueOf(snapshot.child("count").getValue());
                numPosts = Integer.parseInt(s);
                slurperNumPostsTV.setText("Number of posts: " + numPosts + " \uD83E\uDD73");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNumVotes(View view) {
        // read from db here to get numPosts once this is set up in db
    }

    // get slurper status points for the user, evaluate the points to determine slurper status
    private void getSlurperStatusPoints(View view) {
        db.child("slurperStatusPoints").child(userClickedOn).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = String.valueOf(snapshot.child("count").getValue());
                totalSlurperPoints = Integer.parseInt(s);

                TextView slurperNumFriendsTV = view.findViewById(R.id.numFriendsTextView);
                slurperNumFriendsTV.setText("Number of friends: " + totalSlurperPoints + " \uD83D\uDE0E");

                TextView slurperStatusTextView = view.findViewById(R.id.slurperStatusTextView);
                ImageView slurperStatusImageView = view.findViewById(R.id.slurperStatusEmblem);
                if (totalSlurperPoints < 5) {
                    slurperStatusTextView.setText("Baby Slurper \uD83C\uDF7C");
                    slurperStatusImageView.setImageResource(R.drawable.baby);
                } else if (totalSlurperPoints >= 5 && totalSlurperPoints < 10) {
                    slurperStatusTextView.setText("Slurper Jr. ✨");
                    slurperStatusImageView.setImageResource(R.drawable.junior);
                } else if (totalSlurperPoints >= 10 && totalSlurperPoints < 20) {
                    slurperStatusTextView.setText("Slurper Sr. \uD83D\uDCAA");
                    slurperStatusImageView.setImageResource(R.drawable.senior);
                } else if (totalSlurperPoints >= 20 && totalSlurperPoints < 100) {
                    slurperStatusTextView.setText("Slurper Elite Force \uD83D\uDC6E");
                    slurperStatusImageView.setImageResource(R.drawable.elite);
                } else {
                    slurperStatusTextView.setText("Chief of Slurper \uD83E\uDDB9");
                    slurperStatusImageView.setImageResource(R.drawable.chief);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void setSlurperStatus(View view) {
//        TextView slurperStatusTextView = view.findViewById(R.id.slurperStatusTextView);
//        TextView slurperNumFriendsTV = view.findViewById(R.id.numFriendsTextView);
//        TextView slurperNumVotesTV = view.findViewById(R.id.numVotesTextView);
//        TextView slurperNumPostsTV = view.findViewById(R.id.numPostsTextView);
//        ImageView slurperStatusImageView = view.findViewById(R.id.slurperStatusEmblem);
//        if (totalSlurperPoints < 5) {
//            slurperStatusTextView.setText("Baby Slurper \uD83C\uDF7C");
//            slurperStatusImageView.setImageResource(R.drawable.baby);
//        } else if (totalSlurperPoints >= 5 && totalSlurperPoints < 10) {
//            slurperStatusTextView.setText("Slurper Jr. ✨");
//            slurperStatusImageView.setImageResource(R.drawable.junior);
//        } else if (totalSlurperPoints >= 10 && totalSlurperPoints < 20) {
//            slurperStatusTextView.setText("Slurper Sr. \uD83D\uDCAA");
//            slurperStatusImageView.setImageResource(R.drawable.senior);
//        } else if (totalSlurperPoints >= 20 && totalSlurperPoints < 100) {
//            slurperStatusTextView.setText("Slurper Elite Force \uD83D\uDC6E");
//            slurperStatusImageView.setImageResource(R.drawable.elite);
//        } else {
//            slurperStatusTextView.setText("Chief of Slurper \uD83E\uDDB9");
//            slurperStatusImageView.setImageResource(R.drawable.chief);
//        }
//        slurperNumFriendsTV.setText("Number of friends: " + totalSlurperPoints + " \uD83D\uDE0E");
//        Log.i("totalhere", String.valueOf(totalSlurperPoints));
//        // update these two once numPosts and numVotes has been configured in db
//        slurperNumPostsTV.setText("Number of posts: " + "N/A" + " \uD83E\uDD73");
//        slurperNumVotesTV.setText("Number of votes: " + "N/A" + " ❤️");
//    }


}

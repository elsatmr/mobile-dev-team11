package edu.northeastern.team11.slurp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.team11.R;

public class OtherSlurperRewardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        String username = this.getUserClickedOn();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://stickers-19c0f-default-rtdb.firebaseio.com/");
        dbRef.child("users_slurp").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                setSlurperStatus(user, view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private String getUserClickedOn() {
        SharedPreferences prefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        return prefs.getString("userClickedOn", null);
    }

    private void setSlurperStatus(User user, View view) {
        long number = user.getSlurperStatusPoints();
        TextView slurperStatusTextView = view.findViewById(R.id.slurperStatusTextView);
        TextView slurperNumFriendsTV = view.findViewById(R.id.numFriendsTextView);
        TextView slurperNumVotesTV = view.findViewById(R.id.numVotesTextView);
        TextView slurperNumPostsTV = view.findViewById(R.id.numPostsTextView);
        ImageView slurperStatusImageView = view.findViewById(R.id.slurperStatusEmblem);
        if (number < 5) {
            slurperStatusTextView.setText("Baby Slurper \uD83C\uDF7C");
            slurperStatusImageView.setImageResource(R.drawable.baby);
        } else if (number >= 5 && number < 10) {
            slurperStatusTextView.setText("Slurper Jr. ✨");
            slurperStatusImageView.setImageResource(R.drawable.junior);
        } else if (number >= 10 && number < 20) {
            slurperStatusTextView.setText("Slurper Sr. \uD83D\uDCAA");
            slurperStatusImageView.setImageResource(R.drawable.senior);
        } else if (number >= 20 && number < 100) {
            slurperStatusTextView.setText("Slurper Elite Force \uD83D\uDC6E");
            slurperStatusImageView.setImageResource(R.drawable.elite);
        } else {
            slurperStatusTextView.setText("Chief of Slurper \uD83E\uDDB9");
            slurperStatusImageView.setImageResource(R.drawable.chief);
        }
        slurperNumFriendsTV.setText("Number of friends: " + String.valueOf(user.getNumFriends()) + " \uD83D\uDE0E");
        slurperNumPostsTV.setText("Number of posts: " + String.valueOf(user.getNumPosts()) + " \uD83E\uDD73");
        slurperNumVotesTV.setText("Number of votes: " + String.valueOf(user.getNumTimesVoted()) + " ❤️");
    }


}

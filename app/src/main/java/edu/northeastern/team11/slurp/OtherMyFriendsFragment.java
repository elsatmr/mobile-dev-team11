package edu.northeastern.team11.slurp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.northeastern.team11.R;

public class OtherMyFriendsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variable
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDb;
    RecyclerView myFriendsRecyclerView;
    ArrayList<String> friendList;
    MyFriendsAdapter myFriendsAdapter;

    public OtherMyFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFavoritedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherMyFriendsFragment newInstance(String param1, String param2) {
        OtherMyFriendsFragment fragment = new OtherMyFriendsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
//        String username = this.getCurUserProfileFrag();
        friendList = new ArrayList<>();
        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReferenceFromUrl("https://stickers-19c0f-default-rtdb.firebaseio.com/");
        myFriendsRecyclerView = view.findViewById(R.id.myFriends_recycler_view);
//        dbRef.child("users_slurp").child(username).child("friends").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                friendList.clear();
//                for (DataSnapshot s : snapshot.getChildren()) {
//                    String friend = String.valueOf(s.getKey());
//                    friendList.add(friend);
//                }
//                myFriendsAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        myFriendsAdapter = new MyFriendsAdapter(friendList, getActivity());
        myFriendsRecyclerView.setAdapter(myFriendsAdapter);
        myFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myFriendsRecyclerView.setHasFixedSize(true);
        return view;
    }

//    private String getCurUserProfileFrag() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
//        return sharedPreferences.getString("username", null);
//    }
}

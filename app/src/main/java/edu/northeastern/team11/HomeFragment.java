package edu.northeastern.team11;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.maps.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.team11.slurp.HomePostAdapter;
import edu.northeastern.team11.slurp.HomePostItem;
import edu.northeastern.team11.slurp.MainSlurpActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    private TextView curUser;
    private String curUser;
    DatabaseReference db;
    RecyclerView rv;
    HomePostAdapter adapter;
    private List<HomePostItem> postsItemList;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slurp_fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get the user that's logged in
        curUser = getCurUserHomeFrag();

        // get ref to db
        db = FirebaseDatabase.getInstance().getReference();
        getPosts();

        // initialize postItem list
        postsItemList = new ArrayList<>();

        // set up rv logic
        rv = view.findViewById(R.id.all_posts_rv);
        adapter = new HomePostAdapter(postsItemList, view.getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(adapter);



    }

    private void getPosts() {
        db.child("slurpPosts").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()) {
                    String author = post.child("userName").getValue().toString();
                    // don't add postItems that were created by the loggedInUser
                    if (!Objects.equals(author, curUser)) {
                        String imgUrl = post.child("imageUrl").getValue().toString();
                        String dishName = post.child("dishName").getValue().toString();
                        String slurpScore = post.child("slurpScore").getValue().toString();
                        HomePostItem homePostItem = new HomePostItem(imgUrl, dishName, slurpScore, author);
                        postsItemList.add(homePostItem);
                    }
                }
                adapter.notifyDataSetChanged();
                Log.i("size", String.valueOf(postsItemList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // get the current user from shared preferences
    private String getCurUserHomeFrag() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        return sharedPreferences.getString("username", null);
    }
}

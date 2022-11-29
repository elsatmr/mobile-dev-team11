package edu.northeastern.team11.slurp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.team11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPostsFragment extends Fragment {

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
    ArrayList<Dish> dishesList;
    RecyclerView postRecyclerView;
    PostAdapter adapter;

    public MyPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPostsFragment newInstance(String param1, String param2) {
        MyPostsFragment fragment = new MyPostsFragment();
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
        View view = inflater.inflate(R.layout.slurp_fragment_my_posts, container, false);
        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReferenceFromUrl("https://stickers-19c0f-default-rtdb.firebaseio.com/");
        String username = this.getCurUserProfileFrag();
        dishesList = new ArrayList<>();
        dbRef.child("slurpPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishesList.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Dish dish = s.getValue(Dish.class);
                    if (dish.getUserName().equals(username)) {
                        dishesList.add(dish);
                    }
                }
                Log.d("SIZEEE", String.valueOf(dishesList.size()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postRecyclerView = view.findViewById(R.id.posts_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        adapter = new PostAdapter(dishesList, getActivity());
        postRecyclerView.setAdapter(adapter);

        return view;
    }

    private String getCurUserProfileFrag() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        return sharedPreferences.getString("username", null);
    }



}
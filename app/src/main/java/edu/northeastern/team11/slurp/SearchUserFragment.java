package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.team11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference db;
    RecyclerView rv;
    UsersAdapter adapter;
    private List<UsersItem> usersList;
    EditText userSearched;
    TextView errMsg;
    Button searchButton;
    String curUser;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchUserFragment newInstance(String param1, String param2) {
        SearchUserFragment fragment = new SearchUserFragment();
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
        View view = inflater.inflate(R.layout.slurp_fragment_search_user, container, false);
        curUser = getCurUserSearchUserFrag();

        // get reference to the db
        db = FirebaseDatabase.getInstance().getReference();
        getAllUsers(view);

        // get ui elements
        userSearched = view.findViewById(R.id.search_username_et);
        searchButton = view.findViewById(R.id.search_for_user_button);
        errMsg = view.findViewById(R.id.search_user_error_msg);
        errMsg.setVisibility(View.INVISIBLE);

        // set up recyclerView to initially include all users in db
        usersList = new ArrayList<>();
        rv = view.findViewById(R.id.users_rv);
        adapter = new UsersAdapter(usersList, view.getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(adapter);


        // search database for the username input
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findUser();
            }
        });

        return view;
    }



    // get all users from the db and add to usersList
    private void getAllUsers(View view) {
        db.child("users_slurp").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    String username = user.getKey();
                    if (!Objects.equals(username, curUser)) {
                        Button addFriendButton = view.findViewById(R.id.add_friend_button);
                        UsersItem userItem = new UsersItem(username, addFriendButton);
                        usersList.add(userItem);
                    }
                }
                Log.i("userList size: ", String.valueOf(usersList.size()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // update recyclerView to only show the user that was searched for
    private void findUser() {
        db.child("users_slurp").child(userSearched.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (userSearched.getText().toString().length()!= 0) {
                    if (task.isSuccessful() && task.getResult().getValue() != null) {
                        // username exists
                        Log.d("here", String.valueOf(task.getResult().getKey()));
                        errMsg.setVisibility(View.INVISIBLE);

                    } else {
                        // username does not exist
                        Log.d("error getting data: ", String.valueOf(task.getException()));
                        errMsg.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("error","EditText username is empty");
                }
            }

        });
    }


    // get the current user from shared preferences
    private String getCurUserSearchUserFrag() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        return sharedPreferences.getString("username", null);
    }

}
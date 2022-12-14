package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
// */
public class SearchUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference db;
    RecyclerView rv;
    UsersAdapter adapter;
    private List<UsersItem> usersList;
    private List<UsersItem> usersListCopy;
    private List<String> friendsList;
    EditText userSearched;
    TextView errMsg;
    TextView suggestedUsersMsg;
    Button searchButton;
    String curUser;
    int backPressedCount = 0;
//    String addedFriend;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get the user that is logged in
        curUser = getCurUserSearchUserFrag();

        // get reference to the db
        db = FirebaseDatabase.getInstance().getReference();
        getFriends();
        getAllUsers(view);

        friendsList = new ArrayList<>();

        // get ui elements
        userSearched = view.findViewById(R.id.search_username_et);
        searchButton = view.findViewById(R.id.search_for_user_button);
        errMsg = view.findViewById(R.id.search_user_error_msg);
        errMsg.setVisibility(View.INVISIBLE);
        suggestedUsersMsg = view.findViewById(R.id.suggested_users_tv);

        // set up recyclerView logic
        usersList = new ArrayList<>();
        usersListCopy = new ArrayList<>();
        rv = view.findViewById(R.id.users_rv);
        adapter = new UsersAdapter(usersList, view.getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(adapter);

//        // search database for the username input
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // hide keyboard
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                // search for user
//                findUser(view);
//            }
//        });

        // add a friend to the loggedInUser's friendsList in database
        ItemTouchHelper.SimpleCallback callbackRight = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_SWIPE,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                UsersItem usersItem = usersList.get(viewHolder.getAdapterPosition());
                String addFriendUsername = usersItem.getUsername();

                // add friend to the loggedInUser's friends list
                db.child("friends").child(curUser).child(addFriendUsername).setValue(addFriendUsername).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        friendsList.remove(usersItem.getUsername());
                        usersList.remove(usersItem);
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        suggestUsers(view);

                        // update loggedInUser's slurper points - +1 for adding a friend
                        db.child("slurperStatusPoints").child(curUser).child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Integer curCount = Integer.valueOf(task.getResult().getValue().toString());
                                    db.child("slurperStatusPoints").child(curUser).child("count").setValue(curCount + 1);
                                }
                            }
                        });

                        View v = getActivity().findViewById(android.R.id.content);
                        Snackbar.make(v, "+1 Slurper Points for adding " + addFriendUsername
                                + " to your friends list!", Snackbar.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // if the loggedInUser swipes on a user that they are already friends with, disable
                // swipe functionality for this viewHolder (avoids adding duplicate friends to db)
                UsersItem usersItem = usersList.get(viewHolder.getAdapterPosition());
                String clickedOnUser = usersItem.getUsername();

                if (friendsList.contains(clickedOnUser)) {
                    View v = getActivity().findViewById(android.R.id.content);
                    Snackbar.make(v, "Cannot add " + clickedOnUser + " since you're already friends.",
                            Snackbar.LENGTH_LONG).show();
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

        };

        // remove a friend to the loggedInUser's friendsList in database
        ItemTouchHelper.SimpleCallback callbackLeft = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                UsersItem usersItem = usersList.get(viewHolder.getAdapterPosition());
                String addFriendUsername = usersItem.getUsername();

                db.child("friends").child(curUser).child(addFriendUsername).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        friendsList.add(usersItem.getUsername());
                        usersList.add(usersItem);
                        adapter.notifyItemInserted(viewHolder.getAdapterPosition());

                        suggestUsers(view);

                        db.child("slurperStatusPoints").child(curUser).child("count").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Integer curCount = Integer.valueOf(task.getResult().getValue().toString());
                                    db.child("slurperStatusPoints").child(curUser).child("count").setValue(curCount -1);

                                }
                            }
                        });
                        View v = getActivity().findViewById(android.R.id.content);
                        Snackbar.make(v, "-1 Slurper Points for unfriending " + addFriendUsername
                                + ".", Snackbar.LENGTH_LONG).show();

                    }
                });

            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                UsersItem usersItem = usersList.get(viewHolder.getAdapterPosition());
                String clickedOnUser = usersItem.getUsername();

                if (!friendsList.contains(clickedOnUser)) {
                    View v = getActivity().findViewById(android.R.id.content);
                    Snackbar.make(v, "Cannot unfriend " + clickedOnUser +
                            " since you're not currently friends.", Snackbar.LENGTH_LONG).show();
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

        };




        ItemTouchHelper helper = new ItemTouchHelper(callbackRight);
        helper.attachToRecyclerView(rv);

        ItemTouchHelper helper2 = new ItemTouchHelper(callbackLeft);
        helper2.attachToRecyclerView(rv);

        // search database for the username input
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // search for user
                findUser(view, callbackRight, callbackLeft);
            }
        });

    }

    // edited
    public void getFriends() {
        db.child("friends").child(curUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot friend : snapshot.getChildren()) {
                    friendsList.add(friend.getKey());
                }
                Log.i("friendsList", friendsList.toString());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    // set up recyclerView to initially include users that the loggedInUser is not
    // currently friends with (suggests to the loggedInUser friends to add)
    @SuppressLint("SetTextI18n")
    private void getAllUsers(View view) {

        db.child("users_slurp").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    String username = user.getKey();
                    // don't add the logged in user to the usersList
                    if (!Objects.equals(username, curUser)) {
                        // only add users that the loggedInUser is not currently friends with
                        if (!friendsList.contains(username)) {
                            Button addFriendButton = view.findViewById(R.id.get_slurper_status_button);
                            UsersItem userItem = new UsersItem(username, addFriendButton);
                            usersList.add(userItem);
                        }

                        // edge case: if loggedInUser already added all the user's in the app as
                        // friends, update the suggestedUserMsg
                        if (usersList.size() == 0) {
                            Log.i("edge", "edge");
                            suggestedUsersMsg.setText("You're already friends with all users in the app!");
                        } else {
                            suggestedUsersMsg.setText("Suggested Users To Befriend:\n(Swipe right to add friend, swipe left to remove)");
                        }
                    }
                }
                Log.i("userList size: ", String.valueOf(usersList.size()));
                adapter.notifyDataSetChanged();
                // make a copy to keep track of the suggested users
                usersListCopy.addAll(usersList);
                Log.i("usersListCopy: ", String.valueOf(usersListCopy.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // update recyclerView to only show the user that was searched for
    private void findUser(View view, ItemTouchHelper.SimpleCallback helper1, ItemTouchHelper.SimpleCallback helper2) {

        db.child("users_slurp").child(userSearched.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (userSearched.getText().toString().length() != 0) {
                    if (task.isSuccessful() && task.getResult().getValue() != null) {
                        // username exists
                        Log.d("here", String.valueOf(task.getResult().getKey()));
                        errMsg.setVisibility(View.INVISIBLE);

                        // update recyclerView to only display the user that was searched for
                        usersList.clear();
                        String userName = String.valueOf(task.getResult().getKey());
                        Button addFriendButton = view.findViewById(R.id.get_slurper_status_button);
                        UsersItem searchedUser = new UsersItem(userName, addFriendButton);

                        usersList.add(searchedUser);

                        adapter.notifyDataSetChanged();


                        suggestedUsersMsg.setVisibility(View.INVISIBLE);


                    } else {
                        // username does not exist
                        Log.d("error getting data: ", String.valueOf(task.getException()));
                        errMsg.setVisibility(View.VISIBLE);
                        suggestUsers(view);

                    }
                } else {
                    // make sure rv has the most up to date list of friends
                    Log.d("error", "EditText username is empty");
                    suggestUsers(view);
                }
            }

        });
    }

    // ensure that suggested users recyclerView is always displaying users that rae not
    // friends with the current loggedInUser, after user dcatgusoes an action like adding/removing
    // a friend
    @SuppressLint("NotifyDataSetChanged")
    private void suggestUsers(View view) {
        usersList.clear();
        getFriends();
        getAllUsers(view);
        adapter.notifyDataSetChanged();
        suggestedUsersMsg.setVisibility(View.VISIBLE);
    }


    // get the current user from shared preferences
    private String getCurUserSearchUserFrag() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        return sharedPreferences.getString("username", null);
    }



}
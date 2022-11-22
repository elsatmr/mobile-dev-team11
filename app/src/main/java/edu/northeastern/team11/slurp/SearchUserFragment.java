package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
    EditText userSearched;
    TextView errMsg;
    Button searchButton;
    CardView cardView;
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
//        TextView tv = (TextView) view.findViewById(R.id.searchUser_frag_user);
//        tv.setText("SEARCH USER FRAG, Current User: " + userName);

        userSearched = view.findViewById(R.id.search_username_et);
        searchButton = view.findViewById(R.id.search_for_user_button);
        cardView = view.findViewById(R.id.user_cardView);
        errMsg = view.findViewById(R.id.search_user_error_msg);
        errMsg.setVisibility(View.INVISIBLE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findUser();
            }
        });

        return view;
    }

    private void findUser() {
        db = FirebaseDatabase.getInstance().getReference();

        db.child("users_slurp").child(userSearched.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (userSearched.getText().toString().length()!= 0) {
                    if (task.isSuccessful() && task.getResult().getValue() != null) {
                        // username exists
                        Log.d("here", String.valueOf(task.getResult().getKey()));
                        errMsg.setVisibility(View.INVISIBLE);
                    } else {
                        // searched username does not exist
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
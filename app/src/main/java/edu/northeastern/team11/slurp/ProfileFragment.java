package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.team11.FoodViewHolder;
import edu.northeastern.team11.R;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //VARIABLE
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDb;
    private Query userQuery;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    View view;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view = inflater.inflate(R.layout.slurp_fragment_profile, container, false);
        String username = getCurUserProfileFrag();
        TextView usernameTv = view.findViewById(R.id.slurpUsernameTextView);
        usernameTv.setText(username);
        this.populateTopFragment(username);
        this.manageTabLayout();
        return view;
    }

    // get the current user from shared preferences
    private String getCurUserProfileFrag() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", 0);
        return sharedPreferences.getString("username", null);
    }

    private void populateTopFragment(String username) {
        firebaseDb = FirebaseDatabase.getInstance();
        dbRef = firebaseDb.getReferenceFromUrl("https://stickers-19c0f-default-rtdb.firebaseio.com/");
        userQuery = dbRef.child("users_slurp").child(username);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                TextView statusTv = view.findViewById(R.id.slurperStatusTextView);
                TextView locationTv = view.findViewById(R.id.slurpUserLocationTextView);
                statusTv.setText(String.valueOf(user.getSlurperStatusPoints()));
                locationTv.setText(user.getCityState());
                if (user.getProfilePhotoLink() != "no_profile_pic_set") {
                    ImageView profilePicture = view.findViewById(R.id.userProfilePicture);
                    new ImageThread(user.getProfilePhotoLink(), profilePicture).start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void manageTabLayout() {
        List<String> tabTitles = Arrays.asList("My Posts", "Favorites", "Slurper Award");
        tabLayout = view.findViewById(R.id.userProfileTabLayout);
        viewPager = view.findViewById(R.id.profile_viewpager);
        ProfileFragmentAdapter profileFragmentAdapter = new ProfileFragmentAdapter(this);
        viewPager.setAdapter(profileFragmentAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles.get(position))
        ).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAB POSITION", String.valueOf(tab.getPosition()));
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }) ;
    }

    class ImageThread extends Thread {
        private String url;
        private ImageView imageView;

        public ImageThread(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            try {
                Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bmp != null) {
                            imageView.setImageBitmap(bmp);
                        }
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


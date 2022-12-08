package edu.northeastern.team11.slurp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.team11.R;

public class OtherUserProfile extends Fragment {
    private String userClickedOn;
    View view;
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDb;
    private Query userQuery;

    ViewPager2 viewPager;
    TabLayout tabLayout;

    public OtherUserProfile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userClickedOn = getArguments().getString("userClickedOn");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.slurp_other_user_profile, container, false);
//        String username = getCurUserProfileFrag();

        // share the userClickedOn with the OtherUserProfile's 3 tab fragments
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userClickedOn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userClickedOn", userClickedOn);
        editor.apply();

        TextView usernameTv = view.findViewById(R.id.slurpUsernameTextView);
        usernameTv.setText(userClickedOn);
        this.populateTopFragment(userClickedOn);
        this.manageTabLayout();
        return view;
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
        List<String> tabTitles = Arrays.asList("My Posts", "My Friends", "Slurper Award");
        tabLayout = view.findViewById(R.id.userProfileTabLayout);
        viewPager = view.findViewById(R.id.profile_viewpager);
        OtherProfileFragmentAdapter otherProfileFragmentAdapter = new OtherProfileFragmentAdapter(this);
        viewPager.setAdapter(otherProfileFragmentAdapter);
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

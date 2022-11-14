package edu.northeastern.team11.slurp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.northeastern.team11.R;

public class SearchUsersScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slurp_search_users);

        // Setup the bottom navigation tabs
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.search_users);
        bottomNavigationView.setOnItemSelectedListener(new BottomTabListener(this));
    }

}

package edu.northeastern.team11.slurp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.northeastern.team11.AboutActivity;
import edu.northeastern.team11.HomeFragment;
import edu.northeastern.team11.R;
import edu.northeastern.team11.databinding.SlurpActivityMainBinding;
import edu.northeastern.team11.slurp.AddItemFragment;
import edu.northeastern.team11.slurp.CategoryFragment;
import edu.northeastern.team11.slurp.ProfileFragment;
import edu.northeastern.team11.slurp.SearchUserFragment;

public class MainSlurpActivity extends AppCompatActivity {

    SlurpActivityMainBinding binding;
    Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SlurpActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

}
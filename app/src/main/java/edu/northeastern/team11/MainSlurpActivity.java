package edu.northeastern.team11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import edu.northeastern.team11.databinding.ActivityMainBinding;
import edu.northeastern.team11.databinding.ActivityMainSlurpBinding;

public class MainSlurpActivity extends AppCompatActivity {

    ActivityMainSlurpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainSlurpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCurrentFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeMenuItem:
                    setCurrentFragment(new HomeFragment());
                    break;
                case R.id.foodCategoryMenuItem:
                    setCurrentFragment(new CategoryFragment());
                    break;
                case R.id.addItemMenuItem:
                    setCurrentFragment(new AddItemFragment());
                    break;
                case R.id.userSearchMenuItem:
                    setCurrentFragment(new SearchUserFragment());
                    break;
                case R.id.profileMenuItem:
                    setCurrentFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }
}
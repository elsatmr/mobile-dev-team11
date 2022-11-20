package edu.northeastern.team11.slurp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import edu.northeastern.team11.HomeFragment;
import edu.northeastern.team11.R;
import edu.northeastern.team11.databinding.SlurpActivityMainBinding;
import edu.northeastern.team11.slurp.AddItemFragment;
import edu.northeastern.team11.slurp.CategoryFragment;
import edu.northeastern.team11.slurp.ProfileFragment;
import edu.northeastern.team11.slurp.SearchUserFragment;

public class MainSlurpActivity extends AppCompatActivity {

    SlurpActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SlurpActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCurrentFragment(new HomeFragment());
        getSupportActionBar().hide();
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
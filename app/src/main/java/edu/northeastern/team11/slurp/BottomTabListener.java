package edu.northeastern.team11.slurp;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import edu.northeastern.team11.R;

public class BottomTabListener implements NavigationBarView.OnItemSelectedListener {
    Context mContext;

    public BottomTabListener (Context context){
        mContext = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.feed:
                goToFeed();
                return true;
            case R.id.search_dishes:
                goToDishes();
                return true;
            case R.id.my_profile:
                goToProfile();
                return true;
            case R.id.search_users:
                goToUsers();
                return true;
            case R.id.add_dish:
                goToAddDish();
                return true;
        }
        return false;
    }

    private void goToFeed(){
        Intent intent = new Intent(mContext, FeedScreen.class);
        mContext.startActivity(intent);
    }

    private void goToDishes(){
        Intent intent = new Intent(mContext, SearchDishesScreen.class);
        mContext.startActivity(intent);
    }

    private void goToUsers(){
        Intent intent = new Intent(mContext, SearchUsersScreen.class);
        mContext.startActivity(intent);
    }

    private void goToProfile(){
        Intent intent = new Intent(mContext, ProfileScreen.class);
        mContext.startActivity(intent);
    }

    private void goToAddDish(){
        Intent intent = new Intent(mContext, AddDishScreen.class);
        mContext.startActivity(intent);
    }

}

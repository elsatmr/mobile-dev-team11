package edu.northeastern.team11.slurp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragmentAdapter extends FragmentStateAdapter {

    public ProfileFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.d("FRAGMENT 0", "POSTS");
                return new UserFavoritedFragment();
            case 1:
                Log.d("FRAGMENT 1", "FAVORITE");
                return new UserFavoritedFragment();
            case 2:
                Log.d("FRAGMENT 2", "REWARD");
                return new SlurperRewardFragment();
            default:
                return new UserFavoritedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
//    private List<Fragment> fragmentList = new ArrayList<>();
//    private List<String> titleList = new ArrayList<>();
//
//    public ProfileFragmentAdapter(@NonNull FragmentManager fm) {
//        super(fm);
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return fragmentList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return fragmentList.size();
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titleList.get(position);
//    }
//
//    public void addFragment(Fragment fragment, String title) {
//        fragmentList.add(fragment);
//        titleList.add(title);
//    }


}

package edu.northeastern.team11.slurp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProfileFragmentAdapter extends FragmentStateAdapter {

    public ProfileFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new MyFriendsFragment();
            case 2:
                return new SlurperRewardFragment();
            default:
                return new MyPostsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}

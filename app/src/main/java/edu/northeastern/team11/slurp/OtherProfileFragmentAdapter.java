package edu.northeastern.team11.slurp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OtherProfileFragmentAdapter extends FragmentStateAdapter {

    public OtherProfileFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new OtherMyFriendsFragment();
            case 2:
                return new OtherSlurperRewardFragment();
            default:
                return new OtherMyPostsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

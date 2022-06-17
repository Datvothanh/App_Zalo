package hcmute.edu.vn.app_zalo.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.edu.vn.app_zalo.Fragments.ChatFragment;
import hcmute.edu.vn.app_zalo.Fragments.PeopleFragment;
import hcmute.edu.vn.app_zalo.Fragments.ProfileFragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {//Gắn mỗi trang Fragemnt cho mỗi số
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new PeopleFragment();
            default:
                return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {//Số lượng trang
        return 3;
    }
}

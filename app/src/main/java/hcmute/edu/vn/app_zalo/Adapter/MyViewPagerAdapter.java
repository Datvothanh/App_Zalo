package hcmute.edu.vn.app_zalo.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hcmute.edu.vn.app_zalo.Fragments.ChatFragment;
import hcmute.edu.vn.app_zalo.Fragments.PeopleFragment;
import hcmute.edu.vn.app_zalo.Fragments.ProfileFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {//Thiết lập số thứ tự cho từng Fragemnt
        if(position == 0)
            return ChatFragment.getInstance();
        else if (position == 1)
            return PeopleFragment.getInstance();
        else
            return ProfileFragment.getInstance();

    }

    @Override
    public int getItemCount() {
        return 3;
    }//Khai báo số lượng Fragment
}

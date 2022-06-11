package hcmute.edu.vn.app_zalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import hcmute.edu.vn.app_zalo.Adapter.MyViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.tabDots)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setupViewPager();
    }

    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return null;
            }
        }));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
           if(position == 0)
               tab.setText("chat");
           else
               tab.setText("People");

        }).attach();
    }

    private void init(){
        ButterKnife.bind(this);
    }
}
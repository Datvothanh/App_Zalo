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

    //Tạo các biến gán theo view
    @BindView(R.id.tabDots)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();//Hàm dùng layout
        setupViewPager();//Hàm dùng ViewPager
    }

    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(2);//Giới hạn Page
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
           if(position == 0)//Trang đầu
               tab.setText("chat");//Fragment chat
           else // Trang kế
               tab.setText("People");//Fragment People

        }).attach();
    }

    private void init(){
        ButterKnife.bind(this);//Sử dụng layout activity_home
    }
}
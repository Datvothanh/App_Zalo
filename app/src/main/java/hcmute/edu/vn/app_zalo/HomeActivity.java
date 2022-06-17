package hcmute.edu.vn.app_zalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import hcmute.edu.vn.app_zalo.Adapter.MyViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    //Tạo các biến gán theo view
//    @BindView(R.id.tabDots)
//    TabLayout tabLayout;
//    @BindView(R.id.view_pager)
     ViewPager viewPager;//Gọi biến layout viewPager
//    @BindView(R.id.bottom_navigation)
    private BottomNavigationView bottomNavigationView; //Gọi biến cho thanh menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager = findViewById(R.id.view_pager); //Anh xạ View
        bottomNavigationView = findViewById(R.id.bottom_navigation);//Ánh xạ View

        MyViewPagerAdapter adapter  = new MyViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);//Gọi và thiết lập hàm chuyển đổi của ViewPager

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {//Tạo sự kiện bấm được các nút trên menu
                 switch (position){
                     case 0:
                         bottomNavigationView.getMenu().findItem(R.id.menu_tab_1).setChecked(true);
                         break;
                     case 1:
                         bottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                         break;
                     default:
                         bottomNavigationView.getMenu().findItem(R.id.menu_tab_3).setChecked(true);
                         break;
                 }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {//Tạo sự kiện chuyễn viewPager giữa các trang
                switch (item.getItemId()){
                    case R.id.menu_tab_1:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_tab_2:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_tab_3:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
//        init();//Hàm dùng layout
//        setupViewPager();//Hàm dùng ViewPager
    }

    //    private void setupViewPager() {
//        viewPager.setOffscreenPageLimit(3);//Giới hạn Page
//        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), new Lifecycle() {
//            @Override
//            public void addObserver(@NonNull LifecycleObserver observer) {
//
//            }
//
//            @Override
//            public void removeObserver(@NonNull LifecycleObserver observer) {
//
//            }
//
//            @NonNull
//            @Override
//            public State getCurrentState() {
//                return null;
//            }
//        }));
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//           if(position == 0)//Trang đầu
//               tab.setText("chat");//Fragment chat
//           else if (position == 1)// Trang kế
//               tab.setText("People");//Fragment People
//           else //Trang cuối
//               tab.setText("Profile");//Fragment Profile
//        }).attach();
//    }

//    private void init(){
//        ButterKnife.bind(this);//Sử dụng layout activity_home
//    }
}
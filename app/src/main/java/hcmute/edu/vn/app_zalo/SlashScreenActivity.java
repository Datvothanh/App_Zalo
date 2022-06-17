package hcmute.edu.vn.app_zalo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SlashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;//Biến thời gian
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_flashscreen);

        new Handler().postDelayed(new Runnable() {//Thiết lập thời gian và chuyển sang trang tiếp
            @Override
            public void run() {
                Intent intent = new Intent(SlashScreenActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}

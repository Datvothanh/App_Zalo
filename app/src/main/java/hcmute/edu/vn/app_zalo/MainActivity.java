package hcmute.edu.vn.app_zalo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.List;

import hcmute.edu.vn.app_zalo.Common.Common;
import hcmute.edu.vn.app_zalo.Model.UserModel;

public class MainActivity extends AppCompatActivity {
    private final static int LOGIN_REQUEST_CODE = 7171;//Mã code yêu cầu xác minh
    private List<AuthUI.IdpConfig> providers;//Quy trình xác thực
    private FirebaseAuth firebaseAuth;//Công cụ cho việc đăng nhập
    private FirebaseAuth.AuthStateListener listener;//Được gọi khi có sự thay đổi trong trạng thái xác thực
    FirebaseDatabase database;//Điểm vào để truy cập cơ sở dữ liệu Firebase
    DatabaseReference userRef;//Tham chiếu đại diện cho một vị trí trong cơ sở dữ liệu

    @Override
    protected void onStart() {
        //Khởi tạo xác thực điện thoại
        super.onStart();
        firebaseAuth.addAuthStateListener(listener); //Cập nhật xác thực theo địa điểm người dùng
    }

    @Override
    protected void onStop() {
        //Ngừng xác thực khi đã đăng nhập
        if(firebaseAuth != null && listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login();//Hàm đăng nhập
    }

    private void login(){
        //Tạo mới một quy trình đăng nhập
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Common.USER_REFERENCES);//Biến dử liệu trong People

        //Yêu cầu kết nối các tính năng của điện thoại
        listener=myFirebaseAuth->{
            Dexter.withContext(this)
                    .withPermissions(Arrays.asList(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )).withListener(new MultiplePermissionsListener() {

                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {//Kiểm tra đăng nhập chưa

                    if(multiplePermissionsReport.areAllPermissionsGranted())//Kiểm tra xem đã cho truy cập các chức năng chưa)
                    {
                        FirebaseUser user=myFirebaseAuth.getCurrentUser();
                        if(user!=null)
                        {
                            //kiểm tra xem số điện thoại này có thông tin User chưa
                            checkUserFromFirebase();
                        }
                        else
                            //hiện trang đăng nhập sdt
                            showLoginLayout();
                    }
                    else
                        //Chưa thì thông báo phải cho phép truy cập
                        Toast.makeText(MainActivity.this, "Plese enable all permission", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                }
            }).check();
        };
    }

    private void showLoginLayout() {//Show trang đăng nhập
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers).build(),LOGIN_REQUEST_CODE);
    }

    private void checkUserFromFirebase() {//Kiểm tra thông tin sdt từ Firebase
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())//Nếu số điện thoại này có dữ liệu User thì sẽ chuyển vào trang Home
                        {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            userModel.setUid(snapshot.getKey());
                            goToHomeActivity(userModel);//Hàm chuyển vào trang Home
                        }
                        else
                            //Nâu chưa có đăng ký User thì chuyển sang trang đăng ký
                            showRegisterLayout();//hàm chuyển sang trang đăng ký
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
    }

    private void showRegisterLayout() {//Hàm chuyển sang trang đăng ký
        startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        finish();
    }

    private void goToHomeActivity(UserModel userModel) {//Hàm chuyển sang trang Home
        Common.currentUser=userModel;// IMPORTANT
        startActivity(new Intent(MainActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE)//Kiểm tra mã Code
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK)
            {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            }
            else
                Toast.makeText(this,"[ERROR]"+response.getError(),Toast.LENGTH_SHORT).show();//Báo lổi nhập sai mã code
        }
    }
}
package hcmute.edu.vn.app_zalo.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hcmute.edu.vn.app_zalo.R;

public class ProfileIHolder extends RecyclerView.ViewHolder {
    //Tạo các biến gán theo view
    @BindView(R.id.profile_img_avatar)
    public ImageView profile_img_avatar;
    @BindView(R.id.profile_edt_first_name)
    public TextView profile_first_name;
    @BindView(R.id.profile_edt_last_name)
    public TextView profile_second_name;
    @BindView(R.id.profile_edt_full_bio)
    public TextView profile_edt_bio;
    @BindView(R.id.profile_edt_phone)
    public TextView profile_edt_phone;
    @BindView(R.id.btn_logout_profile)
    public Button button_logout;

    private Unbinder unbinder;
    public ProfileIHolder(@NonNull View itemView) {
        super(itemView);
        unbinder = ButterKnife.bind(this , itemView);
    }
}

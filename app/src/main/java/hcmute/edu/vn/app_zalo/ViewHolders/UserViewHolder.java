package hcmute.edu.vn.app_zalo.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hcmute.edu.vn.app_zalo.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    //Tạo các biến gán theo view
    @BindView(R.id.img_avatar)
    public ImageView img_avatar;
    @BindView(R.id.txt_name)
    public TextView txt_name;
    @BindView(R.id.txt_bio)
    public TextView txt_bio;
    private Unbinder unbinder;
    //Hàm để mình có thể tái sử dụng các xml item.
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        unbinder = ButterKnife.bind(this , itemView);
    }
}

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


public class ChatPictureHolder extends RecyclerView.ViewHolder {
    private Unbinder unbinder;
    //Tạo các biến gán theo view
    @BindView(R.id.txt_time)
    public TextView txt_time;
    @BindView(R.id.txt_chat_message)
    public TextView txt_chat_message;
    @BindView(R.id.img_preview)
    public ImageView img_preview;
    //Hàm để mình có thể tái sử dụng các xml item.
    public ChatPictureHolder(@NonNull View itemView){
        super(itemView);
        unbinder = ButterKnife.bind(this, itemView);
    }
}

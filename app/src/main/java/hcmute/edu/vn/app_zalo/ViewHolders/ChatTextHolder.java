package hcmute.edu.vn.app_zalo.ViewHolders;

import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.Unbinder;
import hcmute.edu.vn.app_zalo.R;

public class ChatTextHolder extends RecyclerView.ViewHolder{
    private Unbinder unbinder;
    @BindView(R.id.txt_time)
    public TextView txt_time;
    @BindView(R.id.txt_chat_message)
    public TextView txt_chat_message;


    public ChatTextHolder(@NonNull View itemView){
        super(itemView);
        unbinder = ButterKnife.bind(this, itemView);
    }
}
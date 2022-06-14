package hcmute.edu.vn.app_zalo.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hcmute.edu.vn.app_zalo.ChatActivity;
import hcmute.edu.vn.app_zalo.Common.Common;
import hcmute.edu.vn.app_zalo.Model.UserModel;
import hcmute.edu.vn.app_zalo.ViewHolders.ChatInfoHolder;
import hcmute.edu.vn.app_zalo.Model.ChatInfoModel;
import hcmute.edu.vn.app_zalo.R;

public class ChatFragment extends Fragment {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
    FirebaseRecyclerAdapter adapter;

    @BindView(R.id.recycler_chat)
    RecyclerView recycler_chat;

    private Unbinder unbinder;

    private ChatViewModel mViewModel;

    static ChatFragment instance;

    public static ChatFragment getInstance(){
        return instance == null ? new ChatFragment() :instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View itemView =  inflater.inflate(R.layout.chat_fragment, container, false);
        initView(itemView);
        loadChatList();//Hiện thị danh sách các phòng chat
        return itemView;
    }

    private void loadChatList() {
        Query query = FirebaseDatabase.getInstance()//Lấy giữ liệu lần chat cuối cùng
                .getReference()
                .child(Common.CHAT_LIST_REFERENCE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseRecyclerOptions<ChatInfoModel> options = new FirebaseRecyclerOptions
                .Builder<ChatInfoModel>()
                .setQuery(query, ChatInfoModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<ChatInfoModel, ChatInfoHolder>(options) {


            @NonNull
            @Override
            public ChatInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_chat_item , parent , false);
                return new ChatInfoHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatInfoHolder holder, int position, @NonNull ChatInfoModel model) {
               if(!adapter.getRef((position))
                   .getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
               {
                   //Hide yourself
                   ColorGenerator generator = ColorGenerator.MATERIAL;
                   int color = generator.getColor(FirebaseAuth.getInstance().getCurrentUser().getUid());
                   TextDrawable.IBuilder builder = TextDrawable.builder()
                           .beginConfig()
                           .withBorder(4)
                           .endConfig()
                           .round();
                   String displayName = FirebaseAuth.getInstance().getCurrentUser().getUid()
                           .equals(model.getCreateId()) ? model.getFriendName() : model.getCreateName();

                   TextDrawable drawable = builder.build(displayName.substring(0,1),color);
                   //Lấy các thông tin đưa vào view
                   holder.img_avatar.setImageDrawable(drawable);
                   holder.txt_name.setText(displayName);
                   holder.txt_last_message.setText(model.getLastMessage());
                   holder.txt_time.setText(simpleDateFormat.format(model.getLastUpdate()));

                   //Event
                   holder.itemView.setOnClickListener(v -> {
                       //Den chat detail
                       FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCES)
                               .child(FirebaseAuth.getInstance().getCurrentUser().getUid()
                                       .equals(model.getCreateId()) ? model.getFriendId() : model.getCreateId())
                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if(snapshot.exists()){
                                           UserModel userModel = snapshot.getValue(UserModel.class);
                                           Common.chatUser = userModel;
                                           Common.chatUser.setUid(snapshot.getKey());
                                           startActivity((new Intent(getContext(), ChatActivity.class)));
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {
                                       Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                                   }
                               });
                   });

               }
               else {
                   //If equal key - hide your self
                   holder.itemView.setVisibility(View.GONE);
                   holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0 , 0));
               }
            }
        };

        adapter.startListening();
        recycler_chat.setAdapter(adapter);
    }

    private void initView(View itemView) {
        unbinder = ButterKnife.bind(this,itemView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_chat.setLayoutManager(layoutManager);
        recycler_chat.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null) adapter.startListening();
    }

    @Override
    public void onStop() {
        if(adapter != null) adapter.startListening();
        super.onStop();
    }

}
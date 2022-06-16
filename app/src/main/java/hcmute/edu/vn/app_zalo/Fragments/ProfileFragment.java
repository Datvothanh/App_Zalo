package hcmute.edu.vn.app_zalo.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hcmute.edu.vn.app_zalo.ChatActivity;
import hcmute.edu.vn.app_zalo.Common.Common;
import hcmute.edu.vn.app_zalo.MainActivity;
import hcmute.edu.vn.app_zalo.Model.ChatInfoModel;
import hcmute.edu.vn.app_zalo.Model.UserModel;
import hcmute.edu.vn.app_zalo.R;
import hcmute.edu.vn.app_zalo.ViewHolders.ProfileIHolder;
import hcmute.edu.vn.app_zalo.ViewHolders.UserViewHolder;

public class ProfileFragment extends Fragment {

    @BindView(R.id.recycler_profile)
    RecyclerView recycler_profile;
    FirebaseRecyclerAdapter adapter;
    private Button btnLogout;
    private Unbinder unbinder;

    private ProfileViewModel mViewModel;

    static ProfileFragment instance;

    public static ProfileFragment getInstance() {
        return instance == null ? new ProfileFragment(): instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.profile_fragment, container, false);
        initView(itemView);
        setUserInformation();
        return itemView;
    }

    private void setUserInformation() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Common.USER_REFERENCES);
        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions
                .Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<UserModel, ProfileIHolder>(options) {

            @NonNull
            @Override
            public ProfileIHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//Tạo hiển thị danh sách người
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_profile,parent,false);//Lấy view layout_people
                return new ProfileIHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProfileIHolder holder, int position, @NonNull UserModel model) {
                if(adapter.getRef(position).getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    //Hide yourself
                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    int color = generator.getColor(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    TextDrawable.IBuilder builder = TextDrawable.builder()
                            .beginConfig()
                            .withBorder(4)
                            .endConfig()
                            .round();
                    TextDrawable drawable = builder.build(model.getFirstName().substring(0,1),color);
                    holder.profile_img_avatar.setImageDrawable(drawable);
                    holder.profile_first_name.setText(model.getFirstName());
                    holder.profile_second_name.setText(model.getLastName());
                    holder.profile_edt_bio.setText(model.getBio());
                    holder.profile_edt_phone.setText(model.getPhone());
                    holder.button_logout.setOnClickListener(v -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    });

                }
                else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0 , 0));
                }
            }
        };

        adapter.startListening();
        recycler_profile.setAdapter(adapter);
    }

    private void initView(View itemView) {
        unbinder = ButterKnife.bind(this,itemView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_profile.setLayoutManager(layoutManager);
        recycler_profile.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
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
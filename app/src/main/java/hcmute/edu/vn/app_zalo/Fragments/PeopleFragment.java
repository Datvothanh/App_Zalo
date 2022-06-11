package hcmute.edu.vn.app_zalo.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.DownloadManager;
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
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hcmute.edu.vn.app_zalo.Common.Common;
import hcmute.edu.vn.app_zalo.Model.UserModel;
import hcmute.edu.vn.app_zalo.R;
import hcmute.edu.vn.app_zalo.ViewHolders.UserViewHolder;

public class PeopleFragment extends Fragment {
    @BindView(R.id.recycler_people)
    RecyclerView recycler_people;
    FirebaseRecyclerAdapter adapter;

    private Unbinder unbinder;

    private PeopleViewModel mViewModel;

    static PeopleFragment instance;

    public static PeopleFragment getInstance(){
        return instance == null ? new PeopleFragment(): instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.people_fragment, container, false);
        initView(itemView);
        loadPeople();
        return itemView;
    }

    private void loadPeople() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Common.USER_REFERENCES);
        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions
                .Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<UserModel, UserViewHolder>(options) {

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_people,parent,false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserModel model) {
                 if(!adapter.getRef(position).getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                 {
                     ColorGenerator generator = ColorGenerator.MATERIAL;
                     int color = generator.getColor(FirebaseAuth.getInstance().getCurrentUser().getUid());
                     TextDrawable.IBuilder builder = TextDrawable.builder()
                             .beginConfig()
                             .withBorder(4)
                             .endConfig()
                             .round();
                     TextDrawable drawable = builder.build(model.getFirstName().substring(0,1),color);
                     holder.img_avatar.setImageDrawable(drawable);
                     StringBuilder stringBuilder = new StringBuilder();
                     stringBuilder.append(model.getFirstName()).append(" ").append(model.getLastName());
                     holder.txt_name.setText(stringBuilder.toString());
                     holder.txt_bio.setText(model.getBio());

                     //Event
                     holder.itemView.setOnClickListener(v -> {
                         //Implement late;

                     });
                 }
                 else {
                     holder.itemView.setVisibility(View.GONE);
                     holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0 , 0));
                 }
            }
        };

        adapter.startListening();
        recycler_people.setAdapter(adapter);
    }

    private void initView(View itemView) {
        unbinder = ButterKnife.bind(this,itemView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_people.setLayoutManager(layoutManager);
        recycler_people.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PeopleViewModel.class);
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
package hcmute.edu.vn.app_zalo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hcmute.edu.vn.app_zalo.Common.Common;
import hcmute.edu.vn.app_zalo.Listener.IFirebaseLoadFailed;
import hcmute.edu.vn.app_zalo.Listener.ILoadTimeFromFirebaseListener;
import hcmute.edu.vn.app_zalo.Model.ChatInfoModel;
import hcmute.edu.vn.app_zalo.Model.ChatMessageModel;
import hcmute.edu.vn.app_zalo.ViewHolders.ChatPictureHolder;
import hcmute.edu.vn.app_zalo.ViewHolders.ChatPictureReceiverHolder;
import hcmute.edu.vn.app_zalo.ViewHolders.ChatTextHolder;
import hcmute.edu.vn.app_zalo.ViewHolders.ChatTextReceiverHolder;

public class ChatActivity extends AppCompatActivity implements ILoadTimeFromFirebaseListener, IFirebaseLoadFailed {

    //code để xác định hành động đang thực hiện
    private static final int MY_CAMERA_REQUEST_CODE = 7373;
    private static final int MY_RESULT_LOAD_IMAGE = 7374;

    //biến đại diện cho view
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_preview)
    ImageView img_preview;
    @BindView(R.id.img_camera)
    ImageView img_camera;
    @BindView(R.id.img_image)
    ImageView img_image;
    @BindView(R.id.edt_chat)
    AppCompatEditText edt_chat;
    @BindView(R.id.img_send)
    ImageView img_send;
    @BindView(R.id.recycler_chat)
    RecyclerView recycler_chat;
    @BindView(R.id.img_avatar)
    ImageView img_avatar;
    @BindView(R.id.txt_name)
    TextView txt_name;

    FirebaseDatabase database; //biến dại diện cho Firebase Realtime Database
    DatabaseReference chatRef, offsetRef; //biến trỏ dữ liệu
    ILoadTimeFromFirebaseListener listener; //thực thi code khi có event xảy ra
    IFirebaseLoadFailed errorListener;

    FirebaseRecyclerAdapter<ChatMessageModel, RecyclerView.ViewHolder> adapter; //kết nối database và view
    FirebaseRecyclerOptions<ChatMessageModel> options;
    //2 ChatMessageModel. 1 cho nguoi gui, 1 cho nguoi nhan

    StorageReference storageReference; //biến trỏ đến vị trí FirebaseStorage, dùng để đọc ghi dữ liệu
    Uri fileUri;
    LinearLayoutManager layoutManager;

    @OnClick(R.id.img_image)
    void onSelectImageClick(){ //khi nhấn vào nút chọn hình ảnh
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, MY_RESULT_LOAD_IMAGE);
    }

    @OnClick(R.id.img_camera)
    void onCaptureImageClick(){ //khi nhấn vào nút chụp ảnh
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your camera");
        fileUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
    }


    @Override //khi hoàn thành hành động
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_CAMERA_REQUEST_CODE){ //chọn ảnh
            if (resultCode == RESULT_OK){
                try {
                    Bitmap thumbnail = MediaStore.Images.Media
                            .getBitmap(
                                    getContentResolver(),
                                    fileUri
                            );
                    img_preview.setImageBitmap(thumbnail);
                    img_preview.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == MY_RESULT_LOAD_IMAGE){ //chụp ảnh
            if (resultCode == RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    InputStream inputStream = getContentResolver()
                            .openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                    img_preview.setImageBitmap(selectedImage);
                    img_preview.setVisibility(View.VISIBLE);
                    fileUri = imageUri;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(this, "Please choose image", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.img_send)
    void onSubmitChatClick(){ //khi gửi tin nhắn
        offsetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //khi cap nhat data
                long offset = snapshot.getValue(Long.class);
                long estimateedServerTimeInMs = System.currentTimeMillis() + offset;
                listener.onLoadOnlyTimeSuccess(estimateedServerTimeInMs); //tai thanh cong, lay thoi gian tu Firebase
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorListener.onError(error.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null){ //neu ket noi thanh cong, kiem tra thay doi tu database
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        if (adapter != null) {
            adapter.stopListening(); //ngung kiem tra thay doi
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //khi nhấn vào tin nhắn
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat); //chuyển sang activity chat

        initViews();  //khởi tạo view chat
        loadChatContent(); //load nội dung
    }

    //Load noi dung chat
    private void loadChatContent() {
        String receiverId = FirebaseAuth.getInstance()
                .getCurrentUser().getUid();
        adapter = new FirebaseRecyclerAdapter<ChatMessageModel, RecyclerView.ViewHolder>(options) {
            @Override //Kiem tra tin nhan den tu ai
            public int getItemViewType(int position) {
                if (adapter.getItem(position).getSenderId().equals(receiverId)) //Tin nhan cua minh
                {
                    return !adapter.getItem(position).isPicture() ? 0 : 1;
                } else return !adapter.getItem(position).isPicture() ? 2 : 3; //Tin nhan cua nguoi khac

            }
            //cập nhật nội dung mới cho holder cũ
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull ChatMessageModel model) {
                if (holder instanceof ChatTextHolder) { //neu tin nhan la cua ban than
                    ChatTextHolder chatTextHolder = (ChatTextHolder) holder;
                    chatTextHolder.txt_chat_message.setText(model.getContent());
                    chatTextHolder.txt_time.setText(
                            DateUtils.getRelativeTimeSpanString(model.getTimeStamp(),
                                            Calendar.getInstance().getTimeInMillis(), 0)
                                    .toString());
                }else if (holder instanceof ChatTextReceiverHolder){ //neu tin nhan la cua nguoi khac
                    ChatTextReceiverHolder chatTextHolder = (ChatTextReceiverHolder) holder;
                    chatTextHolder.txt_chat_message.setText(model.getContent());
                    chatTextHolder.txt_time.setText(
                            DateUtils.getRelativeTimeSpanString(model.getTimeStamp(),
                                            Calendar.getInstance().getTimeInMillis(), 0)
                                    .toString());
                }
                else if (holder instanceof ChatPictureHolder){ //neu hinh anh la cua minh
                    ChatPictureHolder chatPictureHolder = (ChatPictureHolder) holder;
                    chatPictureHolder.txt_chat_message.setText(model.getContent());
                    chatPictureHolder.txt_time.setText(
                            DateUtils.getRelativeTimeSpanString(model.getTimeStamp(),
                                            Calendar.getInstance().getTimeInMillis(), 0)
                                    .toString());
                    Glide.with(ChatActivity.this)
                            .load(model.getPictureLink())
                            .into(chatPictureHolder.img_preview);
                }
                else if (holder instanceof ChatPictureReceiverHolder){ //neu hinh anh la cua minh
                    ChatPictureReceiverHolder chatPictureHolder = (ChatPictureReceiverHolder) holder;
                    chatPictureHolder.txt_chat_message.setText(model.getContent());
                    chatPictureHolder.txt_time.setText(
                            DateUtils.getRelativeTimeSpanString(model.getTimeStamp(),
                                            Calendar.getInstance().getTimeInMillis(), 0)
                                    .toString());
                    Glide.with(ChatActivity.this)
                            .load(model.getPictureLink())
                            .into(chatPictureHolder.img_preview);
                }
            }


            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                if(viewType == 0){ //Tin nhan cua chinh minh
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_message_text_own, parent, false);
                    return new ChatTextReceiverHolder(view);
                }
                else if (viewType == 1 ) //Tin nhan hinh anh cua chinh minh
                {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_message_picture_own, parent, false);
                    return new ChatPictureReceiverHolder(view);
                }
                else if (viewType == 2 ) //Tin nhan cua ban be
                {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_message_text_friend, parent, false);
                    return new ChatTextHolder(view);
                }
                else //Tin nhan hinh anh cua ban be
                {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_message_picture_friend, parent, false);
                    return new ChatPictureHolder(view);
                }
            }
        };

        //Tu dong cuon xuong khi co tin nhan moi
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {

                    recycler_chat.scrollToPosition(positionStart);
                }
                recycler_chat.setAdapter(adapter);
            }
        });
    }

    //Tao o chua noi dung chat
    private void initViews() {
        listener = this;
        errorListener = this;
        database = FirebaseDatabase.getInstance(); //biến đại diện cho database
        chatRef = database.getReference(Common.CHAT_REFERENCE); //dùng để đọc ghi dữ liệu chat

        offsetRef = database.getReference(".info/serverTimeOffset");

        Query query = chatRef.child(Common.generateChatRoomId(
                Common.chatUser.getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        )).child(Common.CHAT_DETAIL_REFERENCE);

        options = new FirebaseRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();
        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(this);
        recycler_chat.setLayoutManager(layoutManager);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(Common.chatUser.getUid());
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();
        TextDrawable drawable = builder.build(Common.chatUser.getFirstName().substring(0, 1), color);
        img_avatar.setImageDrawable(drawable);
        txt_name.setText(Common.getName(Common.chatUser));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    @Override //tải tin nhắn thành công, lấy thời gian từ firebase
    public void onLoadOnlyTimeSuccess(long estimateTimeInMs) {
        ChatMessageModel chatMessageModel = new ChatMessageModel();
        chatMessageModel.setName(Common.getName(Common.currentUser));
        chatMessageModel.setContent(edt_chat.getText().toString());
        chatMessageModel.setTimeStamp(estimateTimeInMs);
        chatMessageModel.setSenderId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (fileUri == null){
            chatMessageModel.setPicture(false);
            submitChatToFirebase(chatMessageModel, chatMessageModel.isPicture(), estimateTimeInMs);
        }
        else
            uploadPictureToFirebase(fileUri, chatMessageModel,estimateTimeInMs);
    }

    //tải ảnh lên firebase
    private void uploadPictureToFirebase(Uri fileUri, ChatMessageModel chatMessageModel, long estimateTimeInMs) {
        AlertDialog dialog = new AlertDialog.Builder(ChatActivity.this)
                .setCancelable(false)
                .setMessage("Please wait...")
                .create();
        dialog.show();

        String filename = Common.getFilename(getContentResolver(), fileUri); //biến tên file hình ảnh
        String path = new StringBuilder(Common.chatUser.getUid()) //biến đường dẫn hình ảnh
                .append("/")
                .append(filename)
                .toString();
        storageReference = FirebaseStorage.getInstance().getReference().child(path);
        UploadTask uploadTask = storageReference.putFile(fileUri);

        Task<Uri> task = uploadTask.continueWithTask(task1 -> {
            if (!task1.isSuccessful()){
                Toast.makeText(this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
            return storageReference.getDownloadUrl();
        }).addOnCompleteListener(task12 -> {
            if(task12.isSuccessful()){
                String uri = task12.getResult().toString();
                dialog.dismiss();
                chatMessageModel.setPicture(true);
                chatMessageModel.setPictureLink(uri);

                submitChatToFirebase(chatMessageModel, chatMessageModel.isPicture(), estimateTimeInMs);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //đưa chat lên firebase
    private void submitChatToFirebase(ChatMessageModel chatMessageModel, boolean isPicture, long estimateTimeInMs) {
        chatRef.child(Common.generateChatRoomId(Common.chatUser.getUid(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) //nếu đã có tin nhắn từ trước
                            appendChat(chatMessageModel, isPicture, estimateTimeInMs); //thêm tin nhắn mới
                        else //chưa nhắn tin bao giờ
                            createChat(chatMessageModel, isPicture, estimateTimeInMs); //tạo tin nhắn mới
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Tao object tren Firebase
    private void createChat(ChatMessageModel chatMessageModel, boolean isPicture, long estimateTimeInMs) {
        ChatInfoModel chatInfoModel = new ChatInfoModel();
        chatInfoModel.setCreateId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chatInfoModel.setFriendName(Common.getName(Common.chatUser));
        chatInfoModel.setFriendId(Common.chatUser.getUid());
        chatInfoModel.setCreateName(Common.getName(Common.currentUser));

        if(isPicture){
            chatInfoModel.setLastMessage("<Image>");
        }
        else {
            chatInfoModel.setLastMessage(chatMessageModel.getContent());
        }
        chatInfoModel.setLastUpdate(estimateTimeInMs);
        chatInfoModel.setCreateDate(estimateTimeInMs);
        // Dua len Firebase
        FirebaseDatabase.getInstance()
                .getReference(Common.CHAT_LIST_REFERENCE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Common.chatUser.getUid())
                .setValue(chatInfoModel)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(aVoid -> {
                    //Submit thanh cong chatInfo
                    //Copy vao Friend chat list
                    FirebaseDatabase.getInstance()
                            .getReference(Common.CHAT_LIST_REFERENCE)
                            .child(Common.chatUser.getUid())    //Swap
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(chatInfoModel)
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            })
                            .addOnSuccessListener(aVoid1 -> {
                                //Add vao ChatRef
                                chatRef.child(Common.generateChatRoomId(Common.chatUser.getUid(),
                                                FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                        .child(Common.CHAT_DETAIL_REFERENCE)
                                        .push()
                                        .setValue(chatMessageModel)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //Clear khung chat sau khi submit
                                                    edt_chat.setText("");
                                                    edt_chat.requestFocus();
                                                    if (adapter != null) {
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    //clear thumbnail preview
                                                    if (isPicture)
                                                    {
                                                        fileUri = null;
                                                        img_preview.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        });
                            });
                });
    }

    //thêm tin nhắn mới
    private void appendChat(ChatMessageModel chatMessageModel, boolean isPicture, long estimateTimeInMs) {
        Map<String, Object> update_data = new HashMap<>();
        update_data.put("lastUpdate", estimateTimeInMs);

        if(isPicture)
            update_data.put("lastMessage", "<Image>");
        else
            update_data.put("lastMessage ", chatMessageModel.getContent());

        //Update on user 1ist
        FirebaseDatabase.getInstance()
                .getReference(Common.CHAT_LIST_REFERENCE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Common.chatUser.getUid())
                .updateChildren(update_data)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(aVoid -> {
                    //Submit thanh cong chatInfo
                    //Copy vao Friend chat list
                    FirebaseDatabase.getInstance()
                            .getReference(Common.CHAT_LIST_REFERENCE)
                            .child(Common.chatUser.getUid())    //Swap
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(update_data)
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            })
                            .addOnSuccessListener(aVoid1 -> {
                                //Add vao ChatRef
                                chatRef.child(Common.generateChatRoomId(Common.chatUser.getUid(),
                                                FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                        .child(Common.CHAT_DETAIL_REFERENCE)
                                        .push()
                                        .setValue(chatMessageModel)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) { //Clear o chat sau khi gui tin
                                                    //Clear
                                                    edt_chat.setText("");
                                                    edt_chat.requestFocus();
                                                    if (adapter != null) {
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    //clear thumbnail preview
                                                    if (isPicture)
                                                    {
                                                        fileUri = null;
                                                        img_preview.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        });
                            });
                });
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
package hcmute.edu.vn.app_zalo.Common;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;

import androidx.annotation.RequiresApi;

import java.util.Random;

import hcmute.edu.vn.app_zalo.Model.UserModel;

public class Common {
    public static final String CHAT_LIST_REFERENCE = "ChatList"; //Tạo tên static cho ChatList
    public static final String CHAT_REFERENCE = "Chat";//Tạo tên static cho Chat
    public static final String CHAT_DETAIL_REFERENCE = "Detail"; //Tạo tên static cho Detail
    public static UserModel currentUser = new UserModel(); //Tạo tên static cho tạo mới UserModel
    public static final String USER_REFERENCES = "People"; //Tạo tên static cho People
    public static UserModel chatUser = new UserModel(); //Tạo mới UserModel

    public static String generateChatRoomId(String a, String b) {//Tạo ra tên phòng chat
        if (a.compareTo(b) > 0)
            return new StringBuilder(a).append(b).toString();
        else if (a.compareTo(b) < 0)
            return new StringBuilder(b).append(a).toString();
        else return new StringBuilder("Chat_Yourself_Error")
                    .append(new Random().nextInt()).toString();
    }

    public static String getName(UserModel chatUser) {//Hàm lấy tên của User
        return new StringBuilder(chatUser.getFirstName())
                .append(" ")
                .append(chatUser.getLastName())
                .toString();
    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getFilename(ContentResolver contentResolver, Uri fileUri) {
        String result = null;
        if (fileUri.getScheme().equals("content")){
            Cursor cursor = contentResolver.query(fileUri, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                cursor.close();
            }
        }
        if (result == null){
            result = fileUri.getPath();
            int cut = result.lastIndexOf("/");
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}

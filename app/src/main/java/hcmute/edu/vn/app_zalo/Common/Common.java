package hcmute.edu.vn.app_zalo.Common;

import java.util.Random;

import hcmute.edu.vn.app_zalo.Model.UserModel;

public class Common {
    public static final String CHAT_LIST_REFERENCE = "ChatList"; //Tạo tên static cho ChatList
    public static final String CHAT_REFERENCE = "Chat";
    public static final String CHAT_DETAIL_REFERENCE = "Detail";
    public static UserModel currentUser = new UserModel(); //Tạo tên static cho tạo mới UserModel
    public static final String USER_REFERENCES = "People"; //Tạo tên static cho People
    public static UserModel chatUser = new UserModel();

    public static String generateChatRoomId(String a, String b) {
        if (a.compareTo(b) > 0)
            return new StringBuilder(a).append(b).toString();
        else if (a.compareTo(b) < 0)
            return new StringBuilder(b).append(a).toString();
        else return new StringBuilder("Chat_Yourself_Error")
                    .append(new Random().nextInt()).toString();
    }

    public static String getName(UserModel chatUser) {
        return new StringBuilder(chatUser.getFirstName())
                .append(" ")
                .append(chatUser.getLastName())
                .toString();
    }
}

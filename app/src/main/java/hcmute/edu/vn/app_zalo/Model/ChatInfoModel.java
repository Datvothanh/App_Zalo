package hcmute.edu.vn.app_zalo.Model;

public class ChatInfoModel {
    private String createName,lastMessage,createId,friendId,friendName;//create: tên phòng chat , lastMessage: tin nhắn cuối cùng , createId: tạo id phòng chat , friendId: id của người nhắn với mình, friend: tên người nhắn với mình
    private long createDate , lastUpdate; //createDate: ngày gửi , lastUpdate: thời gian gửi cách bao lâu

    public ChatInfoModel(){

    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

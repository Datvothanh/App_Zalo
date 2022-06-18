package hcmute.edu.vn.app_zalo.Model;

public class ChatMessageModel {
    private String uid, name, content, senderId, pictureLink;//uid: Mã tin nhắn , name : tên tin nhắn , content:nội dunh chat , senderId: id người gửi , pictureLink: link ảnh
    private boolean picture; //Biến đúng sai của hình ảnh
    private Long timeStamp;//Biến tính thời gian gửi

    public ChatMessageModel(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public boolean isPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture = picture;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timestamp) {
        this.timeStamp = timestamp;
    }
}

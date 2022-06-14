package hcmute.edu.vn.app_zalo.Model;

public class ChatMessageModel {
    private String uid, name, content, senderId, pictureLink;
    private boolean picture;
    private Long timeStamp;

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

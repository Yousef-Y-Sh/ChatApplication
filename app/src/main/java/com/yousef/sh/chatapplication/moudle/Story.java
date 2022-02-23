package com.yousef.sh.chatapplication.moudle;

public class Story {
    String UserId;
    String image;
    String name;
    String userImg;

    public Story(String userId, String image, String name, String userImg) {
        UserId = userId;
        this.image = image;
        this.name = name;
        this.userImg = userImg;
    }

    public Story() {
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}

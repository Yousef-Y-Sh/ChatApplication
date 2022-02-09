package com.yousef.sh.chatapplication.moudle;

public class Story {
    String name;
    String image;
    String UserId;

    public Story(String name, String image, String userId) {
        this.name = name;
        this.image = image;
        UserId = userId;
    }

    public Story() {
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

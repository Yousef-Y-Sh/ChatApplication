package com.yousef.sh.chatapplication.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Utils {
    Activity activity;
    public String ChatRoot = "chats";
    public String UsersRoot = "users";
    public String TimeServerRoot = "ServerTime";
    public String FriendObject = "friend_Object";
    public String StoryRoot = "story";
    public String FriendRoot = "friends";

    public Utils(Activity activity) {
        this.activity = activity;
    }

    public void Intent(Class aClass) {
        Intent intent = new Intent(activity, aClass);
        activity.startActivity(intent);
    }

    public void IntentClearTask(Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public Utils() {
    }

    public void PickImage(int reuestCode) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(i, reuestCode);
    }

    public void Toast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
}

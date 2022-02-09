package com.yousef.sh.chatapplication;

import android.app.Application;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;

public class EmojiClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new TwitterEmojiProvider());
    }
}

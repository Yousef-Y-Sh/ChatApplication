package com.yousef.sh.chatapplication.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yousef.sh.chatapplication.IncomingCallActivity;
import com.yousef.sh.chatapplication.SplashScreenActivity;
import com.yousef.sh.chatapplication.Utils.MessageNotification;
import com.yousef.sh.chatapplication.Utils.Utils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Utils utils;
    String firstname;
    String email;
    String image;
    String type;
    String mettingType;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String ChannelName = "";
    String AppId = "";
    String Uid = "";
    String RtcToken = "";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        pref = getApplicationContext().getSharedPreferences("DEVICE_TOKEN", MODE_PRIVATE);
        editor = pref.edit();
        firstname = remoteMessage.getData().get(Utils.KEY_FIRST_NAME);
        email = remoteMessage.getData().get(Utils.KEY_EMAIL);
        image = remoteMessage.getData().get(Utils.KEY_IMG);
        type = remoteMessage.getData().get(Utils.REMOTE_MSG_TYPE);
        mettingType = remoteMessage.getData().get(Utils.REMOTE_MSG_METTING_TYPE);
        ChannelName = remoteMessage.getData().get(Utils.ChannelName);
        AppId = remoteMessage.getData().get(Utils.AppId);
        Uid = remoteMessage.getData().get(Utils.Uid);
        RtcToken = remoteMessage.getData().get(Utils.RtcToken);

        PushNotification(MyFirebaseMessagingService.this, remoteMessage);
        if (type != null) {
            if (type.equals(Utils.REMOTE_MSG_INVITATION)) {
                if (mettingType.equals(Utils.METTING_TYPE_VOICE)) {
                    String mytoken = remoteMessage.getData().get("MyTOKEN");
                    String friendToken = remoteMessage.getData().get("friendTOKEN");

                    Log.e("111111111my", mytoken);
                    Log.e("111111111friend", friendToken);

                    Log.e("aaaaaaaa:MEETYPE", remoteMessage.getData().get(Utils.REMOTE_MSG_TYPE) + "");
                    Intent intent = new Intent(getApplicationContext(), IncomingCallActivity.class);
                    intent.putExtra("firstname", firstname);
                    intent.putExtra("email", email);
                    intent.putExtra("image", image);
                    intent.putExtra("mettingType", mettingType);
                    intent.putExtra("MyTOKEN", mytoken);
                    intent.putExtra("friendTOKEN", friendToken);

                    intent.putExtra(Utils.ChannelName, ChannelName);
                    intent.putExtra(Utils.AppId, AppId);
                    intent.putExtra(Utils.Uid, Uid);
                    intent.putExtra(Utils.RtcToken, RtcToken);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else if (type.equals(Utils.REMOTE_MSG_CANCLE)) {
                Intent intent = new Intent(Utils.REMOTE_MSG_TYPE);
                intent.putExtra("ans", remoteMessage.getData().get(Utils.REMOTE_MSG_TYPE));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            } else if (type.equals(Utils.REMOTE_MSG_ACCEPT)) {
                Intent intent = new Intent(Utils.REMOTE_MSG_TYPE);
                intent.putExtra("ans", remoteMessage.getData().get(Utils.REMOTE_MSG_TYPE));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }
    }

    private void PushNotification(Context mContext, RemoteMessage remote) {
        if (remote.getNotification().getTitle() != null && remote.getNotification().getBody() != null) {
            Intent intent = new Intent(mContext, SplashScreenActivity.class);
            MessageNotification.PushNotification(mContext, remote.getNotification().getTitle(), remote.getNotification().getBody(), intent);
        }
    }
}

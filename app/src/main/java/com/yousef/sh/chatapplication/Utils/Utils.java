package com.yousef.sh.chatapplication.Utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.CRC32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
    Activity activity;
    public static final String ChatRoot = "chats";
    public static final String UsersRoot = "users";
    public static final String TimeServerRoot = "ServerTime";
    public static final String FriendObject = "friend_Object";
    public static final String StoryRoot = "story";
    public static final String FriendRoot = "friends";


    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_IMG = "last_name";
    public static final String KEY_ID = "id";

    public static final String REMOTE_MSG_AUTHERIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_METTING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String METTING_TYPE_VOICE = "voice";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_CANCLE = "cancle";
    public static final String REMOTE_MSG_ACCEPT = "accept";

    public static final String ChannelName = "channelName";
    public static final String AppId = "appId";
    public static final String Uid = "uid";
    public static final String RtcToken = "RtcToken";


    public Utils(Activity activity) {
        this.activity = activity;
    }

    public Utils() {
    }

    public void _Intent(Class aClass) {
        Intent intent = new Intent(activity, aClass);
        activity.startActivity(intent);
    }

    public void _IntentClearTask(Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }


    public void _PickImage(int reuestCode) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(i, reuestCode);
    }

    public void _Toast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public String _getText(EditText editText) {
        return editText.getText().toString();
    }

    public String _getText(TextView textView) {
        return textView.getText().toString();
    }

    public void _showSnackBar(String message, String action, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.setAction(action, listener);
        snackbar.show();
    }

    public void _showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static HashMap<String, String> getRemoteMessageHeader() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Utils.REMOTE_MSG_AUTHERIZATION,
                "key=AAAAdozued0:APA91bEPyLLfDsJqJ_qeVZw5H4IlNpWxobPkCW5LcBq8p-CVZh06T_PNITq4TeRHmKE42y8V0AQU1Ar7fBcgD4tcsAM--U3evT0U125QjaoEwjo-Dd8hPuVwcIp0Ot0XvAduIkqc9VCB"
        );
        headers.put(
                Utils.REMOTE_MSG_CONTENT_TYPE,
                "application/json"
        );
        return headers;
    }

}

package com.yousef.sh.chatapplication.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.yousef.sh.chatapplication.ByteBuf;

import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
        activity.finish();
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

    public static final long HMAC_SHA256_LENGTH = 32;
    public static final int VERSION_LENGTH = 3;
    public static final int APP_ID_LENGTH = 32;

    public static byte[] hmacSign(String keyString, byte[] msg) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec keySpec = new SecretKeySpec(keyString.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keySpec);
        return mac.doFinal(msg);
    }

    public static byte[] pack(PackableEx packableEx) {
        ByteBuf buffer = new ByteBuf();
        packableEx.marshal(buffer);
        return buffer.asBytes();
    }

    public static void unpack(byte[] data, PackableEx packableEx) {
        ByteBuf buffer = new ByteBuf(data);
        packableEx.unmarshal(buffer);
    }

    public static String base64Encode(byte[] data) {
        byte[] encodedBytes = Base64.encodeBase64(data);
        return new String(encodedBytes);
    }

    public static byte[] base64Decode(String data) {
        return Base64.decodeBase64(data.getBytes());
    }

    public static int crc32(String data) {
        // get bytes from string
        byte[] bytes = data.getBytes();
        return crc32(bytes);
    }

    public static int crc32(byte[] bytes) {
        CRC32 checksum = new CRC32();
        checksum.update(bytes);
        return (int)checksum.getValue();
    }

    public static int getTimestamp() {
        return (int)((new Date().getTime())/1000);
    }

    public static int randomInt() {
        return new SecureRandom().nextInt();
    }

    public static boolean isUUID(String uuid) {
        if (uuid.length() != 32) {
            return false;
        }

        return uuid.matches("\\p{XDigit}+");
    }
}

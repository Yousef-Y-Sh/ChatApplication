package com.yousef.sh.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yousef.sh.chatapplication.Utils.Utils;

import java.util.Locale;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class Call extends AppCompatActivity {

    //
//    private static final String LOG_TAG = Call.class.getSimpleName();
//    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
//    private RtcEngine mRtcEngine;
//    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1
//        @Override
//        public void onUserOffline(final int uid, final int reason) { // Tutorial Step 4
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    onRemoteUserLeft(uid, reason);
//                }
//            });
//        }
//
//        @Override
//        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    onRemoteUserVoiceMuted(uid, muted);
//                }
//            });
//        }
//    };
//    String ChannelName = "";
//    String AppId = "";
////    String SUid = "";
//    String RtcToken = "";
    private ImageView finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        finish = (ImageView) findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        ChannelName = getIntent().getStringExtra(Utils.ChannelName);
//        AppId = getIntent().getStringExtra(Utils.AppId);
////        Log.e(AppId, SUid);
////        SUid = getIntent().getStringExtra(Utils.Uid);
//        RtcToken = getIntent().getStringExtra(Utils.RtcToken);
//
//        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
//            initAgoraEngineAndJoinChannel();
//        }
//
//    }
//
//    private void initAgoraEngineAndJoinChannel() {
//        initializeAgoraEngine();     // Tutorial Step 1
//        joinChannel();               // Tutorial Step 2
//    }
//
//    public boolean checkSelfPermission(String permission, int requestCode) {
//        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
//        if (ContextCompat.checkSelfPermission(this,
//                permission)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{permission},
//                    requestCode);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[], @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);
//
//        switch (requestCode) {
//            case PERMISSION_REQ_ID_RECORD_AUDIO: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    initAgoraEngineAndJoinChannel();
//                } else {
//                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
//                    finish();
//                }
//                break;
//            }
//        }
//    }
//
//    public final void showLongToast(final String msg) {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        leaveChannel();
//        RtcEngine.destroy();
//        mRtcEngine = null;
//    }
//
//    public void onLocalAudioMuteClicked(View view) {
//        ImageView iv = (ImageView) view;
//        if (iv.isSelected()) {
//            iv.setSelected(false);
//            iv.clearColorFilter();
//        } else {
//            iv.setSelected(true);
//            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
//        }
//        mRtcEngine.muteLocalAudioStream(iv.isSelected());
//    }
//
//    public void onSwitchSpeakerphoneClicked(View view) {
//        ImageView iv = (ImageView) view;
//        if (iv.isSelected()) {
//            iv.setSelected(false);
//            iv.clearColorFilter();
//        } else {
//            iv.setSelected(true);
//            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
//        }
//        mRtcEngine.setEnableSpeakerphone(view.isSelected());
//    }
//
//        public void onEncCallClicked (View view){
//            finish();
//        }
//
//    private void initializeAgoraEngine() {
//        try {
//            mRtcEngine = RtcEngine.create(getBaseContext(), AppId, mRtcEventHandler);
//        } catch (Exception e) {
//            Log.e(LOG_TAG, Log.getStackTraceString(e));
//
////            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
//        }
//    }
//
//    private void joinChannel() {
//        if (TextUtils.equals(RtcToken, "") || TextUtils.equals(RtcToken, "#YOUR ACCESS TOKEN#")) {
//            RtcToken = null; // default, no token
//        }
////        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
//
//        mRtcEngine.joinChannel(RtcToken, ChannelName, "Extra Optional Data", 40462389); // if you do not specify the uid, we will generate the uid for you
//    }
//
//    private void leaveChannel() {
//        mRtcEngine.leaveChannel();
//    }
//
//    private void onRemoteUserLeft(int uid, int reason) {
//        showLongToast(String.format(Locale.US, "user %d left %d", (uid & 0xFFFFFFFFL), reason));
//        View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk); // optional UI
//        tipMsg.setVisibility(View.VISIBLE);
//    }
//
//    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
//        showLongToast(String.format(Locale.US, "user %d muted or unmuted %b", (uid & 0xFFFFFFFFL), muted));
//    }
    }
}

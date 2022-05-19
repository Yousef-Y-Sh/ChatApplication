package com.yousef.sh.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yousef.sh.chatapplication.Utils.Utils;

public class SplashScreenActivity extends AppCompatActivity {
    Utils utils;
    String Token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        utils = new Utils(SplashScreenActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                utils.IntentClearTask(SignInActivity.class);
            }
        }, 2000);
    }

}
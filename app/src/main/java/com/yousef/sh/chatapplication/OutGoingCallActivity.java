package com.yousef.sh.chatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.API.ApiClient;
import com.yousef.sh.chatapplication.API.ApiService;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.databinding.ActivityOutGoingCallBinding;
import com.yousef.sh.chatapplication.moudle.UserM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutGoingCallActivity extends AppCompatActivity {
    ActivityOutGoingCallBinding binding;
    private Utils utils;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RequestQueue queue;
    UserM friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutGoingCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = getApplicationContext().getSharedPreferences("DEVICE_TOKEN", MODE_PRIVATE);
        editor = pref.edit();
        queue = Volley.newRequestQueue(this);
        utils = new Utils(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        friend = getIntent().getParcelableExtra("FRIENDOBJECT");

        Picasso.get().load(Uri.parse(friend.getImgUri())).into(binding.imgUser);
        binding.name.setText(friend.getName());
        binding.email.setText(friend.getEmail());
        Log.e("aaaaaaaFriendTokenOut", friend.getToken());

        binding.decline.setOnClickListener(v -> {

            try {
                JSONObject body = new JSONObject();
                JSONObject data = new JSONObject();
                JSONArray registration_ids = new JSONArray();
                registration_ids.put(friend.getToken());

                data.put(Utils.REMOTE_MSG_TYPE, Utils.REMOTE_MSG_CANCLE);
                data.put(Utils.REMOTE_MSG_METTING_TYPE, Utils.METTING_TYPE_VOICE);
                data.put("FRIENDTOKEN", friend.getToken());
                Log.e("aaaaaaFriendTokenOutDec", friend.getToken());

                body.put(Utils.REMOTE_MSG_REGISTRATION_IDS, registration_ids);
                body.put(Utils.REMOTE_MSG_DATA, data);
                Log.e("aaaaaa", body.toString());
                sendRemoteMessage(body.toString(), utils.REMOTE_MSG_CANCLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    void sendRemoteMessage(String messageBody, String type) {

        retrofit2.Call<String> call = ApiClient.getClient().create(ApiService.class)
                .sendRemoteMessage(Utils.getRemoteMessageHeader(), messageBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Utils.REMOTE_MSG_CANCLE)) {
                        utils.Toast("cancle");
                        finish();
                    }
                } else {
                    utils.Toast("error we are fixed");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                utils.Toast(t.getMessage());
                finish();
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("ans");
            Log.e("aaaaaaaaatypeOut", type);
            if (type.equals(Utils.REMOTE_MSG_CANCLE)) {
                finish();
            }
            if (type.equals(Utils.REMOTE_MSG_ACCEPT)) {
                intent = new Intent(OutGoingCallActivity.this, com.yousef.sh.chatapplication.Call.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                broadcastReceiver, new IntentFilter(Utils.REMOTE_MSG_TYPE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                broadcastReceiver
        );
    }
}
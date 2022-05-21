package com.yousef.sh.chatapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.API.ApiClient;
import com.yousef.sh.chatapplication.API.ApiService;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.databinding.IncomingLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallActivity extends AppCompatActivity {
    IncomingLayoutBinding binding;
    private Utils utils;
    String name;
    String email;
    String image;
    String id;
    String mettingType;
    String friendToken;

    String ChannelName = "";
    String AppId = "";
    String sUid = "";
    String RtcToken = "";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = IncomingLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = getApplicationContext().getSharedPreferences("DEVICE_TOKEN", MODE_PRIVATE);
        editor = pref.edit();
        utils = new Utils(this);
        name = getIntent().getStringExtra("firstname");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");
        id = getIntent().getStringExtra(Utils.KEY_ID);

        ChannelName = getIntent().getStringExtra(Utils.ChannelName);
        AppId = getIntent().getStringExtra(Utils.AppId);
        sUid = getIntent().getStringExtra(Utils.Uid);
        Log.e("SSSSSSS", sUid);
        RtcToken = getIntent().getStringExtra(Utils.RtcToken);


        String mytoken = getIntent().getStringExtra("MyTOKEN");
        String friendToken = getIntent().getStringExtra("friendTOKEN");

        mettingType = getIntent().getStringExtra("mettingType");
        binding.tvName.setText(Html.fromHtml("Incoming call from " + "<b>" + name + "<b>"));
        Picasso.get().load(Uri.parse(image)).into(binding.UserImgCall);
        binding.denied.setOnClickListener(v -> {
            try {
                JSONObject body = new JSONObject();
                JSONObject data = new JSONObject();
                JSONArray registration_ids = new JSONArray();
                registration_ids.put(mytoken);

                data.put(Utils.REMOTE_MSG_TYPE, Utils.REMOTE_MSG_CANCLE);
                data.put(Utils.REMOTE_MSG_METTING_TYPE, Utils.METTING_TYPE_VOICE);
                data.put(Utils.REMOTE_MSG_INVITER_TOKEN, mytoken);

                body.put(Utils.REMOTE_MSG_REGISTRATION_IDS, registration_ids);
                body.put(Utils.REMOTE_MSG_DATA, data);

                sendRemoteMessage(body.toString(), utils.REMOTE_MSG_CANCLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        binding.accept.setOnClickListener(v -> {
            try {
                JSONObject body = new JSONObject();
                JSONObject data = new JSONObject();
                JSONArray registration_ids = new JSONArray();
                registration_ids.put(mytoken);

                data.put(Utils.REMOTE_MSG_TYPE, Utils.REMOTE_MSG_ACCEPT);
                data.put(Utils.REMOTE_MSG_METTING_TYPE, Utils.METTING_TYPE_VOICE);
                data.put(Utils.REMOTE_MSG_INVITER_TOKEN, mytoken);

                body.put(Utils.REMOTE_MSG_REGISTRATION_IDS, registration_ids);
                body.put(Utils.REMOTE_MSG_DATA, data);
                sendRemoteMessage(body.toString(), utils.REMOTE_MSG_ACCEPT);
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
                        finish();
                    }
                    if (type.equals(Utils.REMOTE_MSG_ACCEPT)) {
                        utils._IntentClearTask(com.yousef.sh.chatapplication.Call.class);
                    }
                } else {
                    utils._Toast("error we are fixed");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                utils._Toast(t.getMessage());
                finish();
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("ans");
            Log.e("aaaaaaaaatypeIn", type);

            if (type.equals(Utils.REMOTE_MSG_ACCEPT)) {
                Toast.makeText(getApplicationContext(), "asd", Toast.LENGTH_SHORT).show();
                intent = new Intent(IncomingCallActivity.this, com.yousef.sh.chatapplication.Call.class);

                intent.putExtra(Utils.ChannelName, ChannelName);
                intent.putExtra(Utils.AppId, AppId);
                intent.putExtra(Utils.Uid, sUid);
                intent.putExtra(Utils.RtcToken, RtcToken);

                startActivity(intent);
            }
            if (type.equals(Utils.REMOTE_MSG_CANCLE)) {
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

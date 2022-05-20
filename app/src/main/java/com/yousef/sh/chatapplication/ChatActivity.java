package com.yousef.sh.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Util;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiPopup;
import com.yousef.sh.chatapplication.API.ApiClient;
import com.yousef.sh.chatapplication.API.ApiService;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.adapter.ChatAdapter;
import com.yousef.sh.chatapplication.databinding.ActivityChatBinding;
import com.yousef.sh.chatapplication.moudle.Chat;
import com.yousef.sh.chatapplication.moudle.UserM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    Utils utils;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    Chat chat;
    ArrayList<Chat> list;
    ChatAdapter adapter;
    UserM friend;
    EmojiPopup emojiPopup;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String msg;
    static String appId = "2a4abbb30eb84e16b72143fabd76cd81";
    static String appCertificate = "df2bad5d676a4c52bac34e32e1f752bd";
    static String channelName = "asdvfdfg";
    static String userAccount = "2082341273";
    static int uid = 52456205;
    static int expirationTimeInSeconds = 3600;
    String RtcToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        utils = new Utils(this);
        setSupportActionBar(binding.toolbarchat);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        friend = getIntent().getParcelableExtra(utils.FriendObject);
        emojiPopup = EmojiPopup.Builder.fromRootView(binding.contaier).build(binding.txtMsg);

        pref = getApplicationContext().getSharedPreferences("DEVICE_TOKEN", MODE_PRIVATE);
        editor = pref.edit();


        ClickMethods();
        GetIntentData();

    }

    void ClickMethods() {
        binding.back.setOnClickListener(view -> {
            utils.Intent(MainActivity.class);
            finish();
        });
        binding.send.setOnClickListener(view -> {
            msg = binding.txtMsg.getText().toString().trim();
            if (TextUtils.isEmpty(msg)) {

            } else {
                String datetime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                binding.txtMsg.setText("");
                chat = new Chat(user.getUid(), friend.getId(), msg, datetime);
                FirebaseDatabase.getInstance().getReference("chats")
                        .child(user.getUid())
                        .child(friend.getId()).push().setValue(chat);

                chat = new Chat(user.getUid(), friend.getId(), msg, datetime);
                FirebaseDatabase.getInstance().getReference("chats")
                        .child(friend.getId())
                        .child(user.getUid()).push().setValue(chat);

            }
        });

        binding.txtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.txtMsg.getText().toString().isEmpty()) {
                    binding.like.setVisibility(View.VISIBLE);
                    binding.send.setVisibility(View.INVISIBLE);
                } else {
                    binding.like.setVisibility(View.INVISIBLE);
                    binding.send.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.emoji.setOnClickListener(view -> {
            binding.cardView.setPaddingRelative(0, 0, 0, 30);
            emojiPopup.show();
        });
        binding.call.setOnClickListener(v -> {
            try {
                JSONObject body = new JSONObject();
                JSONObject data = new JSONObject();
                JSONArray registration_ids = new JSONArray();

                registration_ids.put(friend.getToken());

                data.put(Utils.KEY_FIRST_NAME, user.getDisplayName());
                data.put(Utils.KEY_EMAIL, user.getEmail());
                data.put(Utils.KEY_IMG, user.getPhotoUrl() + "");
                data.put(Utils.KEY_ID, user.getUid() + "");
                data.put("friendTOKEN", friend.getToken());
                data.put("MyTOKEN", pref.getString("Token", null));
                data.put(Utils.REMOTE_MSG_TYPE, Utils.REMOTE_MSG_INVITATION);
                data.put(Utils.REMOTE_MSG_METTING_TYPE, Utils.METTING_TYPE_VOICE);
                data.put(Utils.ChannelName, channelName);

                body.put(Utils.REMOTE_MSG_REGISTRATION_IDS, registration_ids);
                body.put(Utils.REMOTE_MSG_DATA, data);

                Log.e("aaaaaa", body.toString());
                sendRemoteMessage(body.toString(), utils.REMOTE_MSG_INVITATION);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    void GetIntentData() {
        if (friend != null) {
            binding.tvname.setText(friend.getName());
            Picasso.get().load(friend.getImgUri()).into(binding.img);
            list = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("chats")
                    .child(user.getUid())
                    .child(friend.getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Chat chat = dataSnapshot.getValue(Chat.class);
                                    list.add(chat);
                                }
                                adapter = new ChatAdapter(ChatActivity.this, list, friend);
                                LinearLayoutManager manager = new LinearLayoutManager(ChatActivity.this);
                                manager.setStackFromEnd(true);
                                binding.recyclerView.setLayoutManager(manager);
                                binding.recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            utils.Toast(error.getMessage());
                        }
                    });
        }
    }

    void sendRemoteMessage(String messageBody, String type) {

        Call<String> call = ApiClient.getClient().create(ApiService.class)
                .sendRemoteMessage(Utils.getRemoteMessageHeader(), messageBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Utils.REMOTE_MSG_INVITATION)) {
                        Intent intent = new Intent(getApplicationContext(), OutGoingCallActivity.class);
                        intent.putExtra("FRIENDOBJECT", friend);
                        startActivity(intent);
                    }
                } else {
                    utils.Toast("failer");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                utils.Toast(t.getMessage());
                finish();
            }
        });
    }


}
package com.yousef.sh.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiProvider;
import com.vanniktech.emoji.emoji.EmojiCategory;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.adapter.ChatAdapter;
import com.yousef.sh.chatapplication.databinding.ActivityChatBinding;
import com.yousef.sh.chatapplication.moudle.Chat;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        ClickMethods();
        GetIntentData();

    }

    void ClickMethods() {
        binding.back.setOnClickListener(view -> {
            utils.Intent(MainActivity.class);
            finish();
        });
        binding.send.setOnClickListener(view -> {
            String msg = binding.txtMsg.getText().toString().trim();
            if (TextUtils.isEmpty(msg)) {
                utils.Toast("no message enterd..");
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
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e("tokenaaa",refreshedToken);
            utils.Intent(Call.class);
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

}
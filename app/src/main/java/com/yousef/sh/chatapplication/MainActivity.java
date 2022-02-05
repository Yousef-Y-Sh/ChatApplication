package com.yousef.sh.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.databinding.ActivityMainBinding;
import com.yousef.sh.chatapplication.moudle.UserM;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Utils utils;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        utils = new Utils(this);
        Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.ic_baseline_more_horiz_24).into(binding.imageView2);
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserM newuser = snapshot.getValue(UserM.class);
                        binding.tv.setText(newuser.getName() + "\n" + newuser.getEmail() + "\n" + newuser.getPhone() + "\n" + newuser.getPassword() + "\n" + newuser.getId() + "\n" + user.isEmailVerified());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
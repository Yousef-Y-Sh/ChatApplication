package com.yousef.sh.chatapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.databinding.ActivityMainBinding;
import com.yousef.sh.chatapplication.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    Utils utils;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        utils = new Utils(this);
        onClickMethods();
    }

    void onClickMethods() {
        binding.textView3.setOnClickListener(view -> {
            utils.Intent(SignupActivity.class);
        });

        binding.loginBtn.setOnClickListener(view -> {
            if (Verification()) {
                ActiveEditTexts(true);
                SignInEmailAndPassword();
            }
        });
    }

    boolean Verification() {
        boolean arr[] = new boolean[2];
        String username = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            binding.username.setError("this field is required");
        } else {
            binding.username.setError(null);
            arr[0] = true;
        }
        if (TextUtils.isEmpty(password)) {
            binding.password.setError("this field is required");
        } else {
            binding.password.setError(null);
            arr[1] = true;
        }
        if (arr[0] && arr[1])
            return true;
        else
            return false;
    }

    void SignInEmailAndPassword() {
        auth.signInWithEmailAndPassword(binding.username.getText().toString().trim(), binding.password.getText().toString().trim())
                .addOnSuccessListener(authResult -> {
                    utils.Intent(MainActivity.class);
                }).addOnFailureListener(e -> {
            utils.Toast(e.getMessage());
            ActiveEditTexts(false);
        });
    }

    void ActiveEditTexts(boolean active) {
        if (active) {
            binding.progressBar3.setVisibility(View.VISIBLE);
            binding.loginBtn.setVisibility(View.INVISIBLE);
            binding.password.setEnabled(false);
            binding.username.setEnabled(false);
            binding.textView3.setClickable(false);
        } else {
            binding.progressBar3.setVisibility(View.INVISIBLE);
            binding.loginBtn.setVisibility(View.VISIBLE);
            binding.password.setEnabled(true);
            binding.username.setEnabled(true);
            binding.textView3.setClickable(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            utils.Intent(MainActivity.class);
        }
    }
}

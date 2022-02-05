package com.yousef.sh.chatapplication;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.databinding.ActivitySignUpBinding;
import com.yousef.sh.chatapplication.databinding.ActivityVerificationBinding;

public class VerificationActivity extends AppCompatActivity {
    ActivityVerificationBinding binding;
    Utils utils;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        utils = new Utils(this);
        binding.textView2.setText(Html.fromHtml("To continue using Chat, please verify your email address: We have sent an email to <b>" + user.getEmail() + "</b>"));
        onCLickMethods();
    }

    void onCLickMethods() {
        binding.gotologin.setOnClickListener(view -> {
            utils.Intent(SignInActivity.class);
        });
        binding.resend.setOnClickListener(view -> {
            ActiveProgress(true);
            SendVerificationEmail();
        });
    }

    void SendVerificationEmail() {
        user.sendEmailVerification()
                .addOnSuccessListener(unused -> {
                    utils.Intent(VerificationActivity.class);
                    ActiveProgress(false);
                    utils.Toast("Verification email has been sent successfully");
                }).addOnFailureListener(e -> {
            utils.Toast(e.getMessage());
            ActiveProgress(false);
            utils.Toast("Please try later");
        });
    }

    void ActiveProgress(boolean active) {
        if (active) {
            binding.resend.setVisibility(View.INVISIBLE);
            binding.progressBar2.setVisibility(View.VISIBLE);
            binding.gotologin.setEnabled(false);
        } else {
            binding.resend.setVisibility(View.VISIBLE);
            binding.progressBar2.setVisibility(View.INVISIBLE);
            binding.gotologin.setEnabled(true);
        }
    }

}

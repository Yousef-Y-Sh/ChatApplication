package com.yousef.sh.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.databinding.ActivitySignInBinding;
import com.yousef.sh.chatapplication.databinding.ActivitySignUpBinding;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    Utils utils;
    String name, username, phone, password, confirmPassword;
    Uri selectedImage;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabase;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String Token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utils = new Utils(this);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getToken();
        pref = getApplicationContext().getSharedPreferences("DEVICE_TOKEN", MODE_PRIVATE);
        editor = pref.edit();

        if (Token != null) {
            editor.putString("Token", Token);
            editor.commit();
        }
        onCLicksMethods();
    }

    void onCLicksMethods() {
        binding.image.setOnClickListener(view -> {
            utils._PickImage(888);
        });
        binding.tv2.setOnClickListener(view -> {
            utils._Intent(SignInActivity.class);
        });
        binding.SignupBtn.setOnClickListener(view -> {
            if (VerificationData()) {
                ActiveProgressAndEditTexts(true);
                createUser();
            }
        });
    }

    boolean VerificationData() {
        name = binding.name.getText().toString().trim();
        username = binding.username.getText().toString().trim();
        password = binding.password.getText().toString().trim();
        confirmPassword = binding.confirmPassword.getText().toString().trim();
        phone = binding.phone.getText().toString().trim();
        boolean arr[] = new boolean[5];

        if (TextUtils.isEmpty(name)) {
            binding.name.setError("this field is required");
        } else {
            binding.name.setError(null);
            arr[0] = true;
        }

        if (TextUtils.isEmpty(username)) {
            binding.username.setError("this field is required");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            binding.username.setError("This email address is not valid");
        } else {
            binding.username.setError(null);
            arr[1] = true;
        }

        if (TextUtils.isEmpty(phone)) {
            binding.phone.setError("this field is required");
        } else if (phone.length() < 10) {
            binding.phone.setError("this number is not validate");
        } else {
            binding.phone.setError(null);
            arr[2] = true;
        }

        if (TextUtils.isEmpty(password)) {
            binding.password.setError("this field is required");
        } else if (!password.equals(confirmPassword)) {
            binding.password.setError("Passwords do not match");
        } else {
            binding.password.setError(null);
            arr[3] = true;
        }

        if (selectedImage == null) {
            utils._Toast("Please select a picture");
        } else {
            arr[4] = true;
        }

        if (arr[0] && arr[1] && arr[2] && arr[3] && arr[4])
            return true;
        else
            return false;
    }

    void createUser() {
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e("111111 : createUser", "Create User Email and Password succefully");
                upload_Image();
            }
        }).addOnFailureListener(e -> {
            utils._Toast(e.getMessage());
            ActiveProgressAndEditTexts(false);
        });
    }

    void upload_Image() {
        String random_Key = UUID.randomUUID().toString();
        storageReference = storageReference.child("images/" + random_Key);
        storageReference.putFile(selectedImage)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.e("111111 : Upload Image", "upload image to firebase storage successfully");
                    GetImageUriAfterUpload();
                }).addOnFailureListener(e -> {
            utils._Toast(e.getMessage());
            ActiveProgressAndEditTexts(false);
        });
    }

    void GetImageUriAfterUpload() {
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            selectedImage = uri;
            Log.e("111111 : Get url image", "Get Url for image is successfully");
            createUserInformation();
        }).addOnFailureListener(e -> {
            utils._Toast(e.getMessage());
            ActiveProgressAndEditTexts(false);
        });
    }

    void createUserInformation() {
        user = auth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(selectedImage)
                    .build();
            user.updateProfile(profile).addOnSuccessListener(unused -> {
                Log.e("111111 : create user", " create and update informatino user is successfully");
                uploadInformationToRealTime();
            }).addOnFailureListener(e -> {
                utils._Toast(e.getMessage());
                ActiveProgressAndEditTexts(false);
            });
        }
    }

    void uploadInformationToRealTime() {
        user = auth.getCurrentUser();
        UserM userM = new UserM(user.getUid(), user.getDisplayName(), user.getEmail(), phone, password, user.getPhotoUrl() + "", Token);
        mDatabase.child("users").child(user.getUid()).setValue(userM)
                .addOnSuccessListener(unused -> {
                    Log.e("111111 : upload to Real", " Upload information to realtime is successfully");
                    SendVerificationEmail();
                }).addOnFailureListener(e -> {
            utils._Toast(e.getMessage());
            ActiveProgressAndEditTexts(false);
        });
    }

    void SendVerificationEmail() {
        user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnSuccessListener(unused -> {
                    utils._Intent(VerificationActivity.class);
                }).addOnFailureListener(e -> {
            utils._Toast(e.getMessage());
            ActiveProgressAndEditTexts(false);
        });
    }

    void ActiveProgressAndEditTexts(boolean Active) {
        if (Active) {
            binding.SignupBtn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.image.setClickable(false);
            binding.username.setEnabled(false);
            binding.name.setEnabled(false);
            binding.phone.setEnabled(false);
            binding.password.setEnabled(false);
            binding.confirmPassword.setEnabled(false);
            binding.tv2.setClickable(false);
        } else {
            binding.SignupBtn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.image.setClickable(true);
            binding.username.setEnabled(true);
            binding.name.setEnabled(true);
            binding.phone.setEnabled(true);
            binding.password.setEnabled(true);
            binding.confirmPassword.setEnabled(true);
            binding.tv2.setClickable(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 888) {
                selectedImage = data.getData();
                binding.image.setImageURI(selectedImage);
                binding.tvImage.setVisibility(View.INVISIBLE);
                Log.e("111111 : selected image", selectedImage.toString());
            }
        }
    }

    void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    Token = task.getResult().getToken();
                } else {
                    utils._Toast(task.getException().getMessage());
                }
            }
        });
    }
}

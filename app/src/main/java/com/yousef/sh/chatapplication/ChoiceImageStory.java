package com.yousef.sh.chatapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.moudle.Story;

import java.util.UUID;

public class ChoiceImageStory extends AppCompatActivity {
    Utils utils;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_image_story);
        utils = new Utils(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        utils.PickImage(777);
    }

    void upload_Image(Uri uri) {
        String random_Key = UUID.randomUUID().toString();
        storageReference = storageReference.child("story/" + random_Key);
        storageReference.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    GetImageUriAfterUpload();
                }).addOnFailureListener(e -> {
            utils.Toast(e.getMessage());
        });
    }

    void GetImageUriAfterUpload() {
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            CreateRealTimeStoryDB(uri);
        }).addOnFailureListener(e -> {
            utils.Toast(e.getMessage());
        });

    }

    void CreateRealTimeStoryDB(Uri uri) {
        Story story = new Story(user.getUid(), uri + "", user.getDisplayName(), user.getPhotoUrl() + "");
        FirebaseDatabase.getInstance().getReference(utils.StoryRoot)
                .child(user.getUid())
                .setValue(story).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                utils.Toast("succedd");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 777) {
                upload_Image(data.getData());
                utils.Intent(MainActivity.class);
            }
        } else {
            utils.Intent(MainActivity.class);
        }
    }
}
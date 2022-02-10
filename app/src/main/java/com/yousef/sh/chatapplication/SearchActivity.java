package com.yousef.sh.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.adapter.ListFriendAdpater;
import com.yousef.sh.chatapplication.adapter.SuggestedFriendAdapter;
import com.yousef.sh.chatapplication.databinding.ActivitySearchBinding;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding bind;
    ArrayList<UserM> list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    SuggestedFriendAdapter adapter;
    Utils utils;
    String a = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        utils = new Utils(this);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        GetSuggestedList();
        OnClicksMethods();


    }


    void OnClicksMethods() {
        bind.btnCancle.setOnClickListener(view -> {
            utils.Intent(MainActivity.class);
        });
        bind.etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String Text) {
                ArrayList<UserM> newList = new ArrayList<>();
                Query query = database.getReference(utils.UsersRoot)
                        .orderByChild("email")
                        .equalTo(Text);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                UserM newUser = dataSnapshot.getValue(UserM.class);
                                newList.add(newUser);
                            }
                            SuggestedFriendAdapter adapter2 = new SuggestedFriendAdapter(SearchActivity.this, newList);
                            bind.recycle.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                            bind.recycle.setAdapter(adapter2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<UserM> newList = new ArrayList<>();
                Query query = database.getReference(utils.UsersRoot)
                        .orderByChild("email")
                        .equalTo(newText);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                UserM newUser = dataSnapshot.getValue(UserM.class);
                                newList.add(newUser);
                            }
                            SuggestedFriendAdapter adapter2 = new SuggestedFriendAdapter(SearchActivity.this, newList);
                            bind.recycle.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                            bind.recycle.setAdapter(adapter2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return false;
            }
        });
        bind.etSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                list.clear();
                GetSuggestedList();
                return false;
            }
        });
    }

    void GetSuggestedList() {
        FirebaseDatabase.getInstance().getReference(utils.UsersRoot)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserM new_User = dataSnapshot.getValue(UserM.class);
                            if (!new_User.getEmail().equals(auth.getCurrentUser().getEmail()))
                                list.add(new_User);
                        }
                        adapter = new SuggestedFriendAdapter(SearchActivity.this, list);
                        bind.recycle.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                        bind.recycle.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        utils.Toast(error.getMessage());
                    }
                });
    }

}
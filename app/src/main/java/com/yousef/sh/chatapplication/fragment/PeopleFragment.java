package com.yousef.sh.chatapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.adapter.RequestAdapter;
import com.yousef.sh.chatapplication.databinding.FragmentPeopleBinding;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {
    FragmentPeopleBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    Utils utils;
    ArrayList<String> list;
    ArrayList<UserM> frienList;
    RequestAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PeopleFragment() {
    }

    public static PeopleFragment newInstance(String param1, String param2) {
        PeopleFragment fragment = new PeopleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPeopleBinding.inflate(inflater);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database.getReference();
        utils = new Utils(getActivity());
        list = new ArrayList<>();
        frienList = new ArrayList<>();
        GetRequestList();
        return binding.getRoot();
    }

    void GetRequestList() {
        database.getReference(utils.FriendRoot).child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String x = dataSnapshot.child("isFriend").getValue() + "";
                                if (x.equals("1")) {
                                    list.add(dataSnapshot.getKey());
                                }
                            }
                            LoopStringList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        utils.Toast(error.getMessage());
                    }
                });

    }

    void LoopStringList() {
        for (String s : list) {
            database.getReference(utils.UsersRoot).child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        UserM userM = snapshot.getValue(UserM.class);
                        frienList.add(userM);
                        Log.e("111111111", userM.getEmail() + "   " + userM.getId());
                        adapter = new RequestAdapter(frienList, getActivity());
                        binding.recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.recycle.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}


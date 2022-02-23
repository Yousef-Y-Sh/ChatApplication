package com.yousef.sh.chatapplication.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yousef.sh.chatapplication.MainActivity;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.SearchActivity;
import com.yousef.sh.chatapplication.Utils.OnRecycleView;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.adapter.ListFriendAdpater;
import com.yousef.sh.chatapplication.adapter.RequestAdapter;
import com.yousef.sh.chatapplication.adapter.StoryAdapter;
import com.yousef.sh.chatapplication.databinding.FragmentChatBinding;
import com.yousef.sh.chatapplication.moudle.Story;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    Utils utils;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<UserM> list;
    ArrayList<String> KeyList;
    ListFriendAdpater adapter;
    StoryAdapter storyAdapter;
    ArrayList<Story> storyArrayList;
    FirebaseStorage storage;
    StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        binding = FragmentChatBinding.inflate(inflater);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        utils = new Utils(getActivity());
        list = new ArrayList<>();
        storyArrayList = new ArrayList<>();
        KeyList = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        GetRequestList();
        GetListStories();
        OnClick();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    void GetListStories() {
        storyArrayList.add(new Story(null, null, null, null));
        storyAdapter = new StoryAdapter(getActivity(), storyArrayList);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycleStores.setLayoutManager(horizontalLayoutManagaer);
        binding.recycleStores.setAdapter(storyAdapter);

        FirebaseDatabase.getInstance().getReference(utils.StoryRoot)
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        storyArrayList.clear();
                        storyArrayList.add(new Story(null, null, null, null));
                        if (snapshot.exists()) {
                            snapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Story story = dataSnapshot.getValue(Story.class);
                                    storyArrayList.add(1, story);
                                }
                            });
                            storyAdapter = new StoryAdapter(getActivity(), storyArrayList);
                            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            binding.recycleStores.setLayoutManager(horizontalLayoutManagaer);
                            binding.recycleStores.setAdapter(storyAdapter);
                        } else {
                            storyAdapter = new StoryAdapter(getActivity(), storyArrayList);
                            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            binding.recycleStores.setLayoutManager(horizontalLayoutManagaer);
                            binding.recycleStores.setAdapter(storyAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void GetRequestList() {
        FirebaseDatabase.getInstance().getReference(utils.FriendRoot).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String x = dataSnapshot.child("isFriend").getValue() + "";
                                if (x.equals("2")) {
                                    KeyList.add(dataSnapshot.getKey());
                                }

                            }
                            LoopStringList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void LoopStringList() {
        for (String s : KeyList) {
            FirebaseDatabase.getInstance().getReference(utils.UsersRoot)
                    .child(s)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                UserM userM = snapshot.getValue(UserM.class);
                                list.add(userM);
                                adapter = new ListFriendAdpater(getActivity(), list);
                                binding.recycleFriend.setLayoutManager(new LinearLayoutManager(getActivity()));
                                binding.recycleFriend.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    void OnClick() {
        binding.tvSearch.setOnClickListener(view -> {
            utils.Intent(SearchActivity.class);
        });
    }


}
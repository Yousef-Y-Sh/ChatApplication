package com.yousef.sh.chatapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yousef.sh.chatapplication.MainActivity;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.adapter.ListFriendAdpater;
import com.yousef.sh.chatapplication.adapter.StoryAdapter;
import com.yousef.sh.chatapplication.databinding.FragmentChatBinding;
import com.yousef.sh.chatapplication.moudle.Story;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;

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
    ListFriendAdpater adapter;
    StoryAdapter storyAdapter;
    ArrayList<Story> storyArrayList;
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
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        utils = new Utils(getActivity());
        list = new ArrayList<>();
        storyArrayList = new ArrayList<>();
        GetFriendList();
        GetListStories();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void GetFriendList() {
        FirebaseDatabase.getInstance().getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserM new_User = dataSnapshot.getValue(UserM.class);
                            if (!new_User.getEmail().equals(auth.getCurrentUser().getEmail()))
                                list.add(new_User);
                        }
                        adapter = new ListFriendAdpater(getActivity(), list);
                        binding.recycleFriend.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.recycleFriend.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        utils.Toast(error.getMessage());
                    }
                });
    }

    void GetListStories() {

                FirebaseDatabase.getInstance().getReference(utils.StoryRoot).child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Story story = snapshot.getValue(Story.class);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                storyArrayList.add(story);
                            }
                            storyAdapter = new StoryAdapter(getActivity(), storyArrayList);
                            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            binding.recycleStores.setLayoutManager(horizontalLayoutManagaer);
                            binding.recycleStores.setAdapter(storyAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        utils.Toast(error.getMessage());
                    }
                });
            }
}
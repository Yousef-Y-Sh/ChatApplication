package com.yousef.sh.chatapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.ChatActivity;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestedFriendAdapter extends RecyclerView.Adapter<SuggestedFriendAdapter.MyViewHoder> {
    Activity activity;
    ArrayList<UserM> arrayList;
    Utils utils = new Utils();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public SuggestedFriendAdapter(Activity activity, ArrayList<UserM> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.suggested_people_item, parent, false);
        return new MyViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        Picasso.get().load(arrayList.get(position).getImgUri()).into(holder.imgUser);
        holder.tvName.setText(arrayList.get(position).getName());
        holder.btnAddFriend.setOnClickListener(view -> {
            SendRequestFriend(position, holder);
        });
        GetRequserFriend(position, holder);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {
        private CircleImageView imgUser;
        private TextView tvName;
        private ImageView btnAddFriend;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            imgUser = (CircleImageView) itemView.findViewById(R.id.img_user);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            btnAddFriend = (ImageView) itemView.findViewById(R.id.btn_add_friend);
        }
    }

    void SendRequestFriend(int position, MyViewHoder holder) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("isFriend", 1);
        database.getReference(utils.FriendRoot)
                .child(arrayList.get(position).getId())
                .child(user.getUid())
                .setValue(map).addOnSuccessListener(unused -> holder.btnAddFriend.setImageResource(R.drawable.ic_send));
    }

    void GetRequserFriend(int position, MyViewHoder holder) {
        database.getReference(utils.FriendRoot)
                .child(arrayList.get(position).getId())
                .child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String stateFriend = String.valueOf(snapshot.child("isFriend").getValue());
                            if (stateFriend.equals("1")) {
                                holder.btnAddFriend.setImageResource(R.drawable.ic_send);
                            } else if (stateFriend.equals("2")) {
                                holder.btnAddFriend.setImageResource(R.drawable.ic_baseline_check_24);
                            } else {
                                holder.btnAddFriend.setImageResource(R.drawable.ic_baseline_person_add_24);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }

}

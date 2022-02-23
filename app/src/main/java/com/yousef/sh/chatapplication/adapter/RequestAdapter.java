package com.yousef.sh.chatapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    ArrayList<UserM> list;
    Activity activity;
    Utils utils = new Utils();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public RequestAdapter(ArrayList<UserM> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View v = layoutInflater.inflate(R.layout.friend_request_accept, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.MyViewHolder holder, int position) {
        UserM user = list.get(position);
        Picasso.get().load(user.getImgUri()).into(holder.imgFriend);
        holder.tvName.setText(user.getName());
        holder.acceptBtn.setOnClickListener(view -> {
            AcceptFriendRequest(position);
        });
        holder.deleteBtn.setOnClickListener(view -> {
            DeleteFriendRequest(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgFriend;
        private TextView tvName;
        private MaterialButton acceptBtn;
        private MaterialButton deleteBtn;
        private TextView tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFriend = (CircleImageView) itemView.findViewById(R.id.imgFriend);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            acceptBtn = (MaterialButton) itemView.findViewById(R.id.acceptBtn);
            deleteBtn = (MaterialButton) itemView.findViewById(R.id.deleteBtn);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

        }
    }

    void AcceptFriendRequest(int position) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("isFriend", 2);
        database.getReference(utils.FriendRoot)
                .child(user.getUid())
                .child(list.get(position).getId())
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                database.getReference(utils.FriendRoot)
                        .child(list.get(position).getId())
                        .child(user.getUid())
                        .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });


    }

    void DeleteFriendRequest(int position) {
        database.getReference(utils.FriendRoot)
                .child(user.getUid())
                .child(list.get(position).getId())
                .removeValue().addOnSuccessListener(unused -> {
            list.remove(position);
            notifyDataSetChanged();
        });

    }
}

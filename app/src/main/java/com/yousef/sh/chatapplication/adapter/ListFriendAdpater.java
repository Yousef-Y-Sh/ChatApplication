package com.yousef.sh.chatapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.ChatActivity;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.moudle.Chat;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ListFriendAdpater extends RecyclerView.Adapter<ListFriendAdpater.MyViewHoder> {
    Activity activity;
    ArrayList<UserM> arrayList;
    Utils utils = new Utils();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public ListFriendAdpater(Activity activity, ArrayList<UserM> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        pref = activity.getSharedPreferences("DEVICE_TOKEN", activity.MODE_PRIVATE);
        editor = pref.edit();
        View v = LayoutInflater.from(activity).inflate(R.layout.friend_item, parent, false);
        return new MyViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        UserM userm = arrayList.get(position);
        Picasso.get().load(arrayList.get(position).getImgUri()).into(holder.imageView3);
        holder.tvname.setText(arrayList.get(position).getName());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra(utils.FriendObject, arrayList.get(position));
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {
        private final CircleImageView imageView3;
        private final TextView tvname;
        private final TextView tvmsg;


        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView3 = itemView.findViewById(R.id.imageView3);
            tvname = itemView.findViewById(R.id.tvname);
            tvmsg = itemView.findViewById(R.id.tvmsg);
        }
    }
    void getLastMsg(int position) {
        database.getReference(utils.ChatRoot)
                .child(user.getUid()).child(arrayList.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}

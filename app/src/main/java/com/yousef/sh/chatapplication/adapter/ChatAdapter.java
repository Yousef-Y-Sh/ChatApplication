package com.yousef.sh.chatapplication.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.Call;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.moudle.Chat;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter {
    Activity activity;
    ArrayList<Chat> list;
    int right = 0;
    int left = 1;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    UserM friend;
    Utils utils = new Utils();



    public ChatAdapter(Activity activity, ArrayList<Chat> list, UserM friend) {
        this.activity = activity;
        this.list = list;
        this.friend = friend;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == right) {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            View v = layoutInflater.inflate(R.layout.right_item, parent, false);
            return new RightViewHolder(v);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            View v = layoutInflater.inflate(R.layout.left_item, parent, false);
            return new LeftViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (list.get(position).getSender().equals(user.getUid())) {
            RightViewHolder right = (RightViewHolder) holder;
            right.tvRight.setText(list.get(position).getMsg() + "");
            right.tvdateRight.setText(list.get(position).getDate() + "");
//            int lastPosition = list.size() - 1;
//            if (list.get(lastPosition).getMsg().equals("aaa")) {
//                ShowDialog();
//            }
        } else {
            LeftViewHolder left = (LeftViewHolder) holder;
            left.tv_left.setText(list.get(position).getMsg() + "");
            left.tvDate.setText(list.get(position).getDate() + "");
            Picasso.get().load(friend.getImgUri()).into(left.imgFriend);
//            int lastPosition = list.size() - 1;
//            if (list.get(lastPosition).getMsg().equals("aaa")) {
//                ShowDialog();
//            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSender().equals(user.getUid()))
            return right;
        else
            return left;
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRight;
        private TextView tvdateRight;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRight = (TextView) itemView.findViewById(R.id.tv_right);
            tvdateRight = (TextView) itemView.findViewById(R.id.tvdateRight);
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_left;
        private TextView tvDate;
        private CircleImageView imgFriend;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_left = (TextView) itemView.findViewById(R.id.tv_left);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            imgFriend = (CircleImageView) itemView.findViewById(R.id.imgFriend);

        }
    }

    void ShowDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        View itemView = LayoutInflater.from(activity).inflate(R.layout.incoming_layout, null);
        builder.setView(itemView);
        TextView tvName = (TextView) itemView.findViewById(R.id.tv_name);
        ImageView accept = (ImageView) itemView.findViewById(R.id.accept);
        ImageView denied = (ImageView) itemView.findViewById(R.id.denied);
        tvName.setText("مكالمة واردة من " + friend.getName());
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        denied.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        accept.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Call.class);
            activity.startActivity(intent);
        });
        alertDialog.show();
    }
}

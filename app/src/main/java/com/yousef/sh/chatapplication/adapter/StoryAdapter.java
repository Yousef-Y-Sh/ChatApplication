package com.yousef.sh.chatapplication.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.ChoiceImageStory;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.Utils.OnRecycleView;
import com.yousef.sh.chatapplication.Utils.Utils;
import com.yousef.sh.chatapplication.moudle.Story;
import com.yousef.sh.chatapplication.moudle.UserM;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class StoryAdapter extends RecyclerView.Adapter {
    Activity activity;
    ArrayList<Story> list;
    Utils utils;
    int firstPosition = 0;
    int lastposition = 1;


    public StoryAdapter(Activity activity, ArrayList<Story> list) {
        this.activity = activity;
        this.list = list;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        utils = new Utils(activity);
        if (viewType == firstPosition) {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            View v = layoutInflater.inflate(R.layout.story_item, parent, false);
            return new FirstPosition(v);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            View v = layoutInflater.inflate(R.layout.friend_story_item, parent, false);
            return new LastPositionHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && list.get(position).getUserId() == null) {
            FirstPosition firstHolder = (FirstPosition) holder;
            Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(firstHolder.UserImg);
            firstHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utils.Intent(ChoiceImageStory.class);
                }
            });
        } else {
            LastPositionHolder lastHolder = (LastPositionHolder) holder;
            Picasso.get().load(list.get(position).getUserImg()).into(lastHolder.imgUser);
            Picasso.get().load(list.get(position).getImage()).into(lastHolder.imgStory);
            lastHolder.tvName.setText(list.get(position).getName());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == firstPosition) {
            return firstPosition;
        } else {
            return lastposition;
        }
    }

    public class FirstPosition extends RecyclerView.ViewHolder {

        private ImageView UserImg;


        public FirstPosition(@NonNull View itemView) {
            super(itemView);
            UserImg = (ImageView) itemView.findViewById(R.id.UserImg);
        }
    }

    public class LastPositionHolder extends RecyclerView.ViewHolder {

        private ImageView imgStory;
        private CircleImageView imgUser;
        private TextView tvName;

        public LastPositionHolder(@NonNull View itemView) {
            super(itemView);
            imgStory = (ImageView) itemView.findViewById(R.id.imgStory);
            imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }
}

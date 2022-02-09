package com.yousef.sh.chatapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yousef.sh.chatapplication.R;
import com.yousef.sh.chatapplication.moudle.Story;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class StoryAdapter extends RecyclerView.Adapter {
    Activity activity;
    ArrayList<Story> list;
    int startPostition = 1;
    int lastPosition = 0;

    public StoryAdapter(Activity activity, ArrayList<Story> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        if (viewType == startPostition) {
            View v = layoutInflater.inflate(R.layout.story_item, parent, false);
            return new FirstStoriesHolder(v);
        } else {
            View v = layoutInflater.inflate(R.layout.friend_story_item, parent, false);
            return new LastPositionHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            FirstStoriesHolder firstHolder = (FirstStoriesHolder) holder;
            firstHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "stories", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            LastPositionHolder lastHolder = (LastPositionHolder) holder;
            Picasso.get().load(list.get(position).getImage()).into(lastHolder.friendImg);
            lastHolder.tvName.setText(list.get(position).getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return startPostition;
        else
            return lastPosition;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LastPositionHolder extends RecyclerView.ViewHolder {
        private CircleImageView friendImg;
        private TextView tvName;

        public LastPositionHolder(@NonNull View itemView) {
            super(itemView);
            friendImg = (CircleImageView) itemView.findViewById(R.id.friend_img);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public class FirstStoriesHolder extends RecyclerView.ViewHolder {
        public FirstStoriesHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

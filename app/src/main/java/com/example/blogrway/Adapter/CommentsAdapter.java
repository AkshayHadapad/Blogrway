package com.example.blogrway.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogrway.Model.Comments;
import com.example.blogrway.Model.Users;
import com.example.blogrway.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Activity context;
    private List<Users> usersList;
    private List<Comments> commentsList;

    public CommentsAdapter(Activity context , List<Comments> commentsList, List<Users> usersList){
        this.context = context;
        this.commentsList = commentsList;
        this.usersList = usersList;
    }
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_comment , parent , false);
        return new CommentsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comments comments = commentsList.get(position);
        holder.setmComment(comments.getComment());

        Users users = usersList.get(position);
        holder.setmUserName(users.getName());
        holder.setCircleImageView(users.getImage());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView mComment , mUserName;
        CircleImageView circleImageView;
        View mView;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setmComment(String comment){
            mComment = mView.findViewById(R.id.comment_tv);
            mComment.setText(comment);
        }
        public void setmUserName(String userName){
            mUserName = mView.findViewById(R.id.comment_user);
            mUserName.setText(userName);
        }
        public void setCircleImageView(String profilePic){
            circleImageView = mView.findViewById(R.id.comment_Profile_pic);
            Glide.with(context).load(profilePic).into(circleImageView);
        }
    }
}

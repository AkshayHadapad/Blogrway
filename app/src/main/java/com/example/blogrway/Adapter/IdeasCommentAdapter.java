package com.example.blogrway.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogrway.IdeasComment;
import com.example.blogrway.Model.Comments;
import com.example.blogrway.Model.IdeasComments;
import com.example.blogrway.Model.Users;
import com.example.blogrway.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IdeasCommentAdapter extends RecyclerView.Adapter<IdeasCommentAdapter.CommentssViewHolder> {

    private Activity context;
    private List<Users> usersList;
    private List<IdeasComments> commentsList;

    public IdeasCommentAdapter(Activity context , List<IdeasComments> commentsList, List<Users> usersList){
        this.context = context;
        this.commentsList = commentsList;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public CommentssViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_commentideas , parent , false);
        return new CommentssViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentssViewHolder holder, int position) {
        IdeasComments icomments = commentsList.get(position);
        holder.setmComment(icomments.getComment());

        Users users = usersList.get(position);
        holder.setmUserName(users.getName());
        holder.setCircleImageView(users.getImage());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentssViewHolder extends RecyclerView.ViewHolder{
        TextView mComment , mUserName;
        CircleImageView circleImageView;
        View mView;
        public CommentssViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setmComment(String comment){
            mComment = mView.findViewById(R.id.comment_tv1);
            mComment.setText(comment);
        }
        public void setmUserName(String userName){
            mUserName = mView.findViewById(R.id.comment_user1);
            mUserName.setText(userName);
        }
        public void setCircleImageView(String profilePic){
            circleImageView = mView.findViewById(R.id.comment_Profile_pic1);
            Glide.with(context).load(profilePic).into(circleImageView);
        }
    }


}

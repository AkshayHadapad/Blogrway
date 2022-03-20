package com.example.blogrway.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogrway.CommentsActivity;
import com.example.blogrway.IdeasComment;
import com.example.blogrway.Model.Ideasmodel;
import com.example.blogrway.Model.Post;
import com.example.blogrway.Model.Users;
import com.example.blogrway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostIdeaAdapter extends RecyclerView.Adapter<PostIdeaAdapter.IdeaViewHolder> {

    private List<Ideasmodel> mList;
    private List<Users> usersList;
    private Activity context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public PostIdeaAdapter(Activity context , List<Ideasmodel> mList , List<Users> usersList){
        this.mList = mList;
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public IdeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_ideas , parent , false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new IdeaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeaViewHolder holder, int position) {
        Ideasmodel post = mList.get(position);
        holder.setPostTopic(post.getTopic());
        holder.setPostHeadline(post.getHeadline());
        holder.setPostStory(post.getStory());


        long milliseconds = post.getTime().getTime();
        String date  = DateFormat.format("MM/dd/yyyy" , new Date(milliseconds)).toString();
        holder.setPostDaate(date);

        String username = usersList.get(position).getName();
        String image = usersList.get(position).getImage();

        holder.setProfilePic(image);
        holder.setPostUsername(username);

        //likebtn
        String postId = post.IdeasId;
        String currentUserId = auth.getCurrentUser().getUid();
        holder.likePicc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Upvoted!! ", Toast.LENGTH_SHORT).show();
                firestore.collection("Ideasprojects/" + postId + "/Upvote").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){
                            Map<String , Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp" , FieldValue.serverTimestamp());
                            firestore.collection("Ideasprojects/" + postId + "/Upvote").document(currentUserId).set(likesMap);
                        }else{
                            firestore.collection("Ideasprojects/" + postId + "/Upvote").document(currentUserId).delete();
                        }
                    }
                });
            }
        });
        //like color change
        firestore.collection("Ideasprojects/" + postId + "/Upvote").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        holder.likePicc.setImageDrawable(context.getDrawable(R.drawable.ic_after_uparrow));
                    }else{
                        holder.likePicc.setImageDrawable(context.getDrawable(R.drawable.ic_uparrow));
                    }
                }
            }
        });
        //likes count
        firestore.collection("Ideasprojects/" + postId + "/Upvote").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (!value.isEmpty()){
                        int count = value.size();
                        holder.setideasPostLikes(count);
                    }else{
                        holder.setideasPostLikes(0);
                    }
                }
            }
        });

        //dislikebtn
        String post_Id = post.IdeasId;
        String currentUser_Id = auth.getCurrentUser().getUid();
        holder.dislikePicc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Downvoted!! ", Toast.LENGTH_SHORT).show();
                firestore.collection("Ideasprojects/" + postId + "/Downvote").document(currentUser_Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){
                            Map<String , Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp" , FieldValue.serverTimestamp());
                            firestore.collection("Ideasprojects/" + post_Id + "/Downvote").document(currentUser_Id).set(likesMap);
                        }else{
                            firestore.collection("Ideasprojects/" + post_Id + "/Downvote").document(currentUser_Id).delete();
                        }
                    }
                });
            }
        });
        //dislike color change
        firestore.collection("Ideasprojects/" + post_Id + "/Downvote").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        holder.dislikePicc.setImageDrawable(context.getDrawable(R.drawable.ic_after_downarrow));
                    }else{
                        holder.dislikePicc.setImageDrawable(context.getDrawable(R.drawable.ic_downarrow));
                    }
                }
            }
        });
        //dislikes count
        firestore.collection("Ideasprojects/" + post_Id + "/Downvote").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (!value.isEmpty()){
                        int count = value.size();
                        holder.setideasdisPostLikes(count);
                    }else{
                        holder.setideasdisPostLikes(0);
                    }
                }
            }
        });

        //comments implementation
        holder.commentsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context , IdeasComment.class);
                commentIntent.putExtra("postid" , postId);
                context.startActivity(commentIntent);
            }
        });

        if (currentUserId.equals(post.getUser())){
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setClickable(true);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Delete")
                            .setMessage("Are You Sure ?")
                            .setNegativeButton("No" , null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    firestore.collection("Ideasprojects/" + postId + "/Comments").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                                        firestore.collection("Ideasprojects/" + postId + "/Comments").document(snapshot.getId()).delete();
                                                    }
                                                }
                                            });

                                    firestore.collection("Ideasprojects/" + postId + "/Upvote").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                                        firestore.collection("Ideasprojects/" + postId + "/Upvote").document(snapshot.getId()).delete();
                                                    }
                                                }
                                            });

                                    firestore.collection("Ideasprojects/" + postId + "/Downvote").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                                        firestore.collection("Ideasprojects/" + postId + "/Downvote").document(snapshot.getId()).delete();
                                                    }
                                                }
                                            });
                                    firestore.collection("Ideasprojects").document(postId).delete();
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                    alert.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class IdeaViewHolder extends RecyclerView.ViewHolder{

        ImageView postPic , commentsPic , likePicc, dislikePicc;
        CircleImageView profilePic ;
        TextView postUsername , postDate , postTopic,postHeadline,postStory , postLikes,dispostLikes;
        ImageButton deleteBtn;
        View mView;

        public IdeaViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            likePicc = mView.findViewById(R.id.like_btn1);
            dislikePicc = mView.findViewById(R.id.dislike_btn1);
            commentsPic = mView.findViewById(R.id.comments_post1);
            deleteBtn= mView.findViewById(R.id.delete_btn1);
        }
        public void setideasPostLikes(int count){
            postLikes = mView.findViewById(R.id.like_count_tv1);
            postLikes.setText(count + " Upvote");
        }

        public void setideasdisPostLikes(int count){
            dispostLikes = mView.findViewById(R.id.dislike_count_tv1);
            dispostLikes.setText(count + " Downvote");
        }

        public void setProfilePic(String urlProfile){
            profilePic = mView.findViewById(R.id.profile_pic1);
            Glide.with(context).load(urlProfile).into(profilePic);
        }
        public void setPostUsername(String username){
            postUsername = mView.findViewById(R.id.username_tv1);
            postUsername.setText(username);
        }
        public void setPostDaate(String date){
            postDate = mView.findViewById(R.id.date_tv1);
            postDate.setText(date);
        }
        public void setPostTopic(String topic){
            postTopic = mView.findViewById(R.id.topic_tv);
            postTopic.setText(topic);
        }

        public void setPostHeadline(String headline){
            postHeadline= mView.findViewById(R.id.headline_tv);
            postHeadline.setText(headline);
        }

        public void setPostStory(String story){
            postStory = mView.findViewById(R.id.deepstory);
            postStory.setMovementMethod(new ScrollingMovementMethod());
            postStory.setText(story);
        }
    }
}

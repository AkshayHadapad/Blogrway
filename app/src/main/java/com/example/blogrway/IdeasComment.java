package com.example.blogrway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blogrway.Adapter.CommentsAdapter;
import com.example.blogrway.Adapter.IdeasCommentAdapter;
import com.example.blogrway.Model.Comments;
import com.example.blogrway.Model.IdeasComments;
import com.example.blogrway.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdeasComment extends AppCompatActivity {

    private EditText commentEdit;
    private Button mAddCommentBtn;
    private RecyclerView mCommentRecyclerView;
    private FirebaseFirestore firestore;
    private String post_id;
    private String currentUserId;
    private FirebaseAuth auth;
    private IdeasCommentAdapter adapter;
    private List<IdeasComments> mList;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas_comment);

        commentEdit = findViewById(R.id.comment_edittext1);
        mAddCommentBtn = findViewById(R.id.add_comment1);
        mCommentRecyclerView = findViewById(R.id.comment_recyclerView1);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        mList = new ArrayList<>();
        usersList = new ArrayList<>();
        adapter = new IdeasCommentAdapter(IdeasComment.this , mList , usersList);

        post_id = getIntent().getStringExtra("postid");
        mCommentRecyclerView.setHasFixedSize(true);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecyclerView.setAdapter(adapter);

        firestore.collection("Ideasprojects/" + post_id + "/Comments").addSnapshotListener(IdeasComment.this , new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        IdeasComments commentss = documentChange.getDocument().toObject(IdeasComments.class).withideasId(documentChange.getDocument().getId());
                        String userId = documentChange.getDocument().getString("user");

                        firestore.collection("Users").document(userId).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            Users users = task.getResult().toObject(Users.class);
                                            usersList.add(users);
                                            mList.add(commentss);
                                            adapter.notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(IdeasComment.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        mAddCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEdit.getText().toString();
                if (!comment.isEmpty()) {
                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("comment", comment);
                    commentsMap.put("time", FieldValue.serverTimestamp());
                    commentsMap.put("user", currentUserId);
                    firestore.collection("Ideasprojects/" + post_id + "/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(IdeasComment.this, "Comment Added", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(IdeasComment.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(IdeasComment.this, "Please write Comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
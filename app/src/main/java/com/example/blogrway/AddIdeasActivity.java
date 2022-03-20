package com.example.blogrway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class AddIdeasActivity extends AppCompatActivity {

    private Button mAddPostBtn;
    private EditText mStoryText,mTopictext,mHeadlinetext;
    private ProgressBar mProgressBar;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserId;
    private Toolbar postToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ideas);

        mAddPostBtn = findViewById(R.id.save_post_btn);
        mTopictext = findViewById(R.id.topic_text);
        mHeadlinetext = findViewById(R.id.headline_text);
        mStoryText = findViewById(R.id.idea_text);
        mProgressBar = findViewById(R.id.post_progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        postToolbar = findViewById(R.id.addpost_toolbar);
        setSupportActionBar(postToolbar);
        getSupportActionBar().setTitle("Add Post");

        mAddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String topic = mTopictext.getText().toString();
                String headline = mHeadlinetext.getText().toString();
                String story = mStoryText.getText().toString();

                if(!topic.isEmpty() && !headline.isEmpty()&& !story.isEmpty()){

                    HashMap< String, Object > postideasMap = new HashMap< >();
                    postideasMap.put("user", currentUserId);
                    postideasMap.put("topic", topic);
                    postideasMap.put("headline", headline);
                    postideasMap.put("story", story);
                    postideasMap.put("time", FieldValue.serverTimestamp());

                    firestore.collection("Ideasprojects").add(postideasMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(AddIdeasActivity.this, "Ideas Added Successfully !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddIdeasActivity.this, Projectsandideas.class));
                                finish();
                            } else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(AddIdeasActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddIdeasActivity.this, "Please fill all Details!! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
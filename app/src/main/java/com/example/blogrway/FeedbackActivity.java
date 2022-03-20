package com.example.blogrway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class FeedbackActivity extends AppCompatActivity {

    private Button feedbackbtn;
    private EditText name_text,feedback_text;
    private ProgressBar mProgressBar;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String currentUserId;
    private Toolbar postToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackbtn = findViewById(R.id.feeback_btn);
        name_text = findViewById(R.id.name_text);
        feedback_text = findViewById(R.id.feedback_text);
        mProgressBar = findViewById(R.id.post_progressBar1);
        mProgressBar.setVisibility(View.INVISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        postToolbar = findViewById(R.id.addpost_toolbar1);
        setSupportActionBar(postToolbar);
        getSupportActionBar().setTitle("Feedback");

        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String name = name_text.getText().toString();
                String feedbback = feedback_text.getText().toString();


                if(!name.isEmpty() && !feedbback.isEmpty()){

                    HashMap< String, Object > postideasMap = new HashMap< >();
                    postideasMap.put("user", currentUserId);
                    postideasMap.put("name", name);
                    postideasMap.put("feedback", feedbback);
                    postideasMap.put("time", FieldValue.serverTimestamp());

                    firestore.collection("Feedback").add(postideasMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(FeedbackActivity.this, "Feedback Submitted !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(FeedbackActivity.this, Projectsandideas.class));
                                finish();
                            } else {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(FeedbackActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(FeedbackActivity.this, "Please fill all Details!! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
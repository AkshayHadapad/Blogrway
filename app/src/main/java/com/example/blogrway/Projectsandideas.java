package com.example.blogrway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.blogrway.Adapter.PostAdapter;
import com.example.blogrway.Adapter.PostIdeaAdapter;
import com.example.blogrway.Model.IdeasId;
import com.example.blogrway.Model.Ideasmodel;
import com.example.blogrway.Model.Post;
import com.example.blogrway.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Projectsandideas extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private PostIdeaAdapter adapter;
    private List<Ideasmodel> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private List<Users> usersList;
    Button aboutusbtn,blogsbtn,feebback1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectsandideas);
        aboutusbtn=(Button)findViewById(R.id.aboutus1);
        feebback1=(Button)findViewById(R.id.feebback1);
        blogsbtn=(Button)findViewById(R.id.blogsbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.ideasrecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Projectsandideas.this));
        list = new ArrayList<>();
        usersList = new ArrayList<>();
        adapter = new PostIdeaAdapter(Projectsandideas.this , list, usersList);
        mRecyclerView.setAdapter(adapter);

        mainToolbar = findViewById(R.id.main_toolbar1);
        fab = findViewById(R.id.ideasbtn);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Projects & Ideas-BW");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Projectsandideas.this , AddIdeasActivity.class));
            }
        });
        aboutusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Projectsandideas.this , Aboutus.class));
            }
        });

        feebback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Projectsandideas.this , FeedbackActivity.class));
                Toast.makeText(Projectsandideas.this, "Get Creative!..", Toast.LENGTH_SHORT).show();
            }
        });

        blogsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Projectsandideas.this , MainActivity.class));
                Toast.makeText(Projectsandideas.this, "Blogging Time!..", Toast.LENGTH_SHORT).show();
            }
        });

        if (firebaseAuth.getCurrentUser() != null){

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !mRecyclerView.canScrollVertically(1);
                    if (isBottom)
                        Toast.makeText(Projectsandideas.this, "Reached Bottom", Toast.LENGTH_SHORT).show();
                }
            });
            query = firestore.collection("Ideasprojects").orderBy("time" , Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(Projectsandideas.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){
                            String ideasId = doc.getDocument().getId();
                            Ideasmodel post = doc.getDocument().toObject(Ideasmodel.class).withideasId(ideasId);
                            String postUserId = doc.getDocument().getString("user");
                            firestore.collection("Users").document(postUserId).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                Users users = task.getResult().toObject(Users.class);
                                                usersList.add(users);
                                                list.add(post);
                                                adapter.notifyDataSetChanged();
                                            }else{
                                                Toast.makeText(Projectsandideas.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else{
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_menu){
            startActivity(new Intent(Projectsandideas.this , SetUpActivity.class));
        }else if(item.getItemId() == R.id.sign_out_menu){
            firebaseAuth.signOut();
            startActivity(new Intent(Projectsandideas.this , SignInActivity.class));
            finish();
        }
        return true;
    }
}
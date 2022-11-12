package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.socialmediaapp.Adaptars.CommentsAdaptar;
import com.example.socialmediaapp.Fragments.Models.Comments;
import com.example.socialmediaapp.Fragments.Models.Notification;
import com.example.socialmediaapp.Fragments.Models.Post;
import com.example.socialmediaapp.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {


    ActivityCommentBinding binding;
    String postID;
    String postBy;

    Intent intent;


    FirebaseAuth auth;
    FirebaseDatabase database;


    ArrayList<Comments>  commentsArrayList = new ArrayList<>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());



        // set  back toolbar

        setSupportActionBar(binding.toolbar2);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        intent = getIntent();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        postBy = intent.getStringExtra("postBy");
        postID = intent.getStringExtra("postID");


       // Toast.makeText(this, "post id is"+ postID, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this, "post by is"+ postBy, Toast.LENGTH_SHORT).show();



        database.getReference().child("posts")
                .child(postID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Post post = snapshot.getValue(Post.class);


                        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.cover_place).into(binding.postimage);

                        binding.postdiscvriptionTv.setText(post.getPostDiscription());
                        binding.like.setText(post.getPostLike()+"");
                        binding.totalcommenttv.setText(post.getCommentsCount()+"");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        database.getReference().child("Users").child(postBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        Users users = snapshot.getValue(Users.class);

                        binding.name.setText(users.getName());

                        Picasso.get().load(users.getProfilephoto()).placeholder(R.drawable.cover_place).into(binding.profileImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        binding.commentpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Comments comments = new Comments();


                comments.setCommentBy(auth.getUid());
                comments.setCommentBody(binding.commentET.getText().toString());
                comments.setCommentedAt(new Date().getTime());


                database.getReference().child("posts")
                        .child(postID)
                        .child("comments")
                        .push()
                        .setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                                database.getReference().child("posts")
                                        .child(postID)
                                        .child("commentsCount")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                int commentsCount =0;

                                                if(snapshot.exists()){
                                                    commentsCount = snapshot.getValue(Integer.class);

                                                }

                                                database.getReference().child("posts")
                                                        .child(postID)
                                                        .child("commentsCount")
                                                        .setValue(commentsCount +1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                binding.commentET.setText("");

                                                                Toast.makeText(CommentActivity.this, "Commented", Toast.LENGTH_SHORT).show();


                                                                Notification notification = new Notification();
                                                                notification.setNotificationBy(auth.getUid());
                                                                notification.setNotificationAt(new Date().getTime());
                                                                notification.setPostID(postID);
                                                                notification.setPostBy(postBy);
                                                                notification.setType("comment");


                                                                database.getReference()
                                                                        .child("notification")
                                                                        .child(postBy)
                                                                        .push()
                                                                        .setValue(notification);
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }
                        });

            }
        });


        CommentsAdaptar commentsAdaptar = new CommentsAdaptar(commentsArrayList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


        binding.commentRv.setLayoutManager(layoutManager);
        binding.commentRv.setAdapter(commentsAdaptar);



        database.getReference().child("posts")
                .child(postID)
                .child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        commentsArrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                            Comments comments = dataSnapshot.getValue(Comments.class);

                            commentsArrayList.add(comments);


                        }
                        commentsAdaptar.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
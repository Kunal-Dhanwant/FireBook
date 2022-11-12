package com.example.socialmediaapp.Adaptars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.CommentActivity;
import com.example.socialmediaapp.Fragments.Models.Notification;
import com.example.socialmediaapp.Fragments.Models.Post;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.DashboardRvSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostAdaptar extends RecyclerView.Adapter<PostAdaptar.viewholder> {

    ArrayList<Post> list;
    Context context;

    public PostAdaptar(ArrayList<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Post post = list.get(position);


        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.cover_place).into(holder.binding.postImage);



        holder.binding.totalcommenttv.setText(post.getCommentsCount()+"");

        holder.binding.like.setText(post.getPostLike()+"");
        String discription = post.getPostDiscription();
        if(discription.isEmpty()){

            holder.binding.discriptiontv.setVisibility(View.GONE);
        }else{
            holder.binding.discriptiontv.setText(post.getPostDiscription());
        }


        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(post.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilephoto()).placeholder(R.drawable.cover_place).into(holder.binding.profileImage);
                        holder.binding.username.setText(users.getName());
                        holder.binding.about.setText(users.getProfessional());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        FirebaseDatabase.getInstance().getReference().child("posts")
                        .child(post.getPostId())
                                .child("likes")
                                        .child(FirebaseAuth.getInstance().getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        if(snapshot.exists()){

                                                            holder.binding.like.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_heart3,0,0,0);
                                                        }else{
                                                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {

                                                                    FirebaseDatabase.getInstance().getReference().child("posts")
                                                                            .child(post.getPostId()).child("likes")
                                                                            .child(FirebaseAuth.getInstance().getUid()).setValue(true)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {


                                                                                    FirebaseDatabase.getInstance().getReference()
                                                                                            .child("posts")
                                                                                            .child(post.getPostId())
                                                                                            .child("postLike")
                                                                                            .setValue(post.getPostLike()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {


                                                                                                    holder.binding.like.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_heart3,0,0,0);


                                                                                                    Notification notification = new Notification();
                                                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                                                    notification.setNotificationAt(new Date().getTime());
                                                                                                    notification.setPostID(post.getPostId());
                                                                                                    notification.setPostBy(post.getPostedBy());
                                                                                                    notification.setType("like");


                                                                                                    FirebaseDatabase.getInstance().getReference()
                                                                                                            .child("notification")
                                                                                                            .child(post.getPostedBy())
                                                                                                            .push()
                                                                                                            .setValue(notification);


                                                                                                }
                                                                                            });


                                                                                }
                                                                            });

                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });




        holder.binding.totalcommenttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postID",post.getPostId());
                intent.putExtra("postBy",post.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class  viewholder extends RecyclerView.ViewHolder{





        DashboardRvSampleBinding binding;


        public viewholder(@NonNull View itemView) {
            super(itemView);


            binding = DashboardRvSampleBinding.bind(itemView);

//
        }
    }
}

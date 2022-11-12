package com.example.socialmediaapp.Adaptars;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.Fragments.Models.Comments;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.CommentRvSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdaptar  extends  RecyclerView.Adapter<CommentsAdaptar.viewholder> {


    ArrayList<Comments> list;
    Context context;

    public CommentsAdaptar(ArrayList<Comments> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View  view = LayoutInflater.from(context).inflate(R.layout.comment__rv_sample,parent,false);

        return  new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {


        Comments comments = list.get(position);



        String time = TimeAgo.using(comments.getCommentedAt());

        holder.binding.timetv.setText(time);


        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(comments.getCommentBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        Users users = snapshot.getValue(Users.class);


                        Picasso.get().load(users.getProfilephoto()).placeholder(R.drawable.cover_place).into(holder.binding.profileImage);
                        holder.binding.commentbodyTv.setText(Html.fromHtml("<b>"+users.getName()+"</b>"+" "+ comments.getCommentBody()));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewholder extends RecyclerView.ViewHolder{


        CommentRvSampleBinding binding;

        public viewholder(@NonNull View itemView) {
            super(itemView);


            binding = CommentRvSampleBinding.bind(itemView);
        }
    }
}

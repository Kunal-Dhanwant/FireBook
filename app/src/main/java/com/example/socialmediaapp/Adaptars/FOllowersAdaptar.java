package com.example.socialmediaapp.Adaptars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.Fragments.Models.Follower;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.FriendsRvSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FOllowersAdaptar extends  RecyclerView.Adapter<FOllowersAdaptar.viewHolder> {

    ArrayList<Follower> list;
    Context context;

    public FOllowersAdaptar(ArrayList<Follower> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friends_rv_sample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Follower follower = list.get(position);


        FirebaseDatabase.getInstance().getReference().child("Users").child(follower.getFollowedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Users users = snapshot.getValue(Users.class);

                            Picasso.get().load(users.getProfilephoto()).placeholder(R.drawable.cover_place)
                                    .into(holder.binding.frindsProfile);

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

    public class viewHolder extends RecyclerView.ViewHolder{












        FriendsRvSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);


            binding =FriendsRvSampleBinding.bind(itemView);



        }
    }
}

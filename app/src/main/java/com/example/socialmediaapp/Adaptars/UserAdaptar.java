package com.example.socialmediaapp.Adaptars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.Fragments.Models.Follower;
import com.example.socialmediaapp.Fragments.Models.Notification;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.SearchSampleUserBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdaptar  extends  RecyclerView.Adapter<UserAdaptar.viewHolder>{

    ArrayList<Users> list;
    Context context;

    public UserAdaptar(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_sample_user,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Users user = list.get(position);


        Picasso.get().load(user.getProfilephoto()).placeholder(R.drawable.cover_place).into(holder.binding.profileImage);

        holder.binding.UserNameEt.setText(user.getName());
        holder.binding.professionalTV.setText(user.getProfessional());



        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId())
                .child("followers").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.binding.btnFollow.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_following_bg));
                            holder.binding.btnFollow.setText("FOLLOWING");
                            holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.grey));
                            holder.binding.btnFollow.setEnabled(false);
                        }else{
                            holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Follower follower = new Follower();
                                    follower.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    follower.setFollowedAt(new Date().getTime());
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId())
                                            .child("followers").child(FirebaseAuth.getInstance().getUid()).setValue(follower).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void avoid) {

                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserId()).
                                                            child("followerCount").setValue(user.getFollowerCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {

                                                                    Toast.makeText(context, "You Follow " + user.getName(), Toast.LENGTH_SHORT).show();

                                                                    holder.binding.btnFollow.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_following_bg));
                                                                    holder.binding.btnFollow.setText("FOLLOWING");
                                                                    holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.grey));
                                                                    holder.binding.btnFollow.setEnabled(false);


                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setType("follow");





                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(user.getUserId())
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











    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder{


        SearchSampleUserBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SearchSampleUserBinding.bind(itemView);
        }
    }
}

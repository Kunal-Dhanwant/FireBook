package com.example.socialmediaapp.Adaptars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.CommentActivity;
import com.example.socialmediaapp.Fragments.Models.Notification;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.Notification2RvSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdaptar extends  RecyclerView.Adapter<NotificationAdaptar.viewholder>{


    ArrayList<Notification> list;
    Context context;

    public NotificationAdaptar(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(context).inflate(R.layout.notification2_rv_sample,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Notification notification = list.get(position);


        String type = notification.getType();

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Users users = snapshot.getValue(Users.class);

                        Picasso.get().load(users.getProfilephoto())
                                .placeholder(R.drawable.cover_place)
                                .into(holder.binding.profileImage);


                        if(type.equals("like")){
                            holder.binding.notificationtv.setText(Html.fromHtml("<b>"+users.getName()+"</b>" + " Liked Your Post"));
                        }else if (type.equals("comment")){
                            holder.binding.notificationtv.setText(Html.fromHtml("<b>"+users.getName()+"</b>" + " Commented on  Your Post"));

                        }else{
                            holder.binding.notificationtv.setText(Html.fromHtml("<b>"+users.getName()+"</b>" + " Starting Followed You"));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        holder.binding.opennotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!type.equals("follow")){


                    FirebaseDatabase.getInstance().getReference()
                                    .child("notification")
                                            .child(notification.getPostBy()).child(notification.getNotificationID())
                                    .child("checkOpen")
                                            .setValue(true);





                    holder.binding.opennotification.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    Intent intent = new Intent(context, CommentActivity.class);

                    intent.putExtra("postID",notification.getPostID());
                    intent.putExtra("postBy",notification.getPostBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            }
        });

        Boolean checkOpen = notification.isCheckOpen();
        if(checkOpen==true){
            holder.binding.opennotification.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }else{}







    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{




        Notification2RvSampleBinding binding;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding = Notification2RvSampleBinding.bind(itemView);





        }
    }
}

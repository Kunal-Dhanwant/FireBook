package com.example.socialmediaapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaapp.Adaptars.NotificationAdaptar;
import com.example.socialmediaapp.Fragments.Models.Notification;
import com.example.socialmediaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Notification2Fragment extends Fragment {




    RecyclerView  notificationRv;
    ArrayList<Notification>  list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    NotificationAdaptar adaptar;
    LinearLayoutManager layoutManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notification2, container, false);

        notificationRv = view.findViewById(R.id.notification_rv);
        list = new ArrayList<>();


         adaptar = new NotificationAdaptar(list,getContext());
         layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        notificationRv.setLayoutManager(layoutManager);

        notificationRv.setAdapter(adaptar);







        database.getReference()
                .child("notification")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        list.clear();


                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                            Notification notification = dataSnapshot.getValue(Notification.class);

                            notification.setNotificationID(dataSnapshot.getKey());

                            list.add(notification);
                        }

                        adaptar.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







        return view;
    }



}
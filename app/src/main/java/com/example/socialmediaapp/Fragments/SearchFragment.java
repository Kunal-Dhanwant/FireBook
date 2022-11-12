package com.example.socialmediaapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaapp.Adaptars.UserAdaptar;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment {



    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    FragmentSearchBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false);


        UserAdaptar adaptar = new UserAdaptar(list,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.serachUserRv.setLayoutManager(layoutManager);
        binding.serachUserRv.setAdapter(adaptar);


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){


                    Users user = dataSnapshot.getValue(Users.class);


                    user.setUserId(dataSnapshot.getKey());

                    if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(user);
                    }


                }
                adaptar.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return  binding.getRoot();
    }
}
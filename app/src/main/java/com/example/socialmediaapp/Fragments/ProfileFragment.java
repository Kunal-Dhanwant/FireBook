package com.example.socialmediaapp.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.Adaptars.FOllowersAdaptar;
import com.example.socialmediaapp.Fragments.Models.Follower;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {


    ArrayList<Follower> list;
    RecyclerView friendsRv;
    ImageView addImage, verifiedIv,profileImage;
    ImageView coverphoto;


    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    TextView username,userprofessional,totalfollowers;




    ProgressDialog dialog;
    ProgressDialog profiledialoge;




    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(getContext());
        profiledialoge = new ProgressDialog(getContext());




    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please Wait!");
        dialog.setTitle("Uploading Cover Photo.......");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        profiledialoge.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        profiledialoge.setMessage("Please Wait!");
        profiledialoge.setTitle("Uploading Profile Photo.......");
        profiledialoge.setCancelable(false);
        profiledialoge.setCanceledOnTouchOutside(false);

        list = new ArrayList<>();
        friendsRv = view.findViewById(R.id.friendsRv);
        addImage = view.findViewById(R.id.add_image);
        coverphoto = view.findViewById(R.id.background);
        username = view.findViewById(R.id.usernameET);
        userprofessional = view.findViewById(R.id.userProfessionalET);
        verifiedIv = view.findViewById(R.id.verifiedIV);
        profileImage = view.findViewById(R.id.profile_image);
        totalfollowers = view.findViewById(R.id.TotalfollowerTv);






        FOllowersAdaptar adaptar = new FOllowersAdaptar(list,getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        friendsRv.setLayoutManager(layoutManager);
        friendsRv.setAdapter(adaptar);



        database.getReference().child("Users").child(auth.getUid()).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {






                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Follower follower = dataSnapshot.getValue(Follower.class);
                    list.add(follower);
                }

                adaptar.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);


                            Picasso.get().load(users.getCoverPhoto()).placeholder(R.drawable.cover_place).into(coverphoto);
                            Picasso.get().load(users.getProfilephoto()).placeholder(R.drawable.cover_place).into(profileImage);

                            username.setText(users.getName());
                            userprofessional.setText(users.getProfessional());
                            totalfollowers.setText(users.getFollowerCount()+"");




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,11);
            }
        });


        verifiedIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,12);
            }
        });






        return  view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==11){
            if(data.getData() !=null){
                Uri uri = data.getData();


                dialog.show();


                coverphoto.setImageURI(uri);



                final StorageReference reference = storage.getReference().child("cover_photo").child(auth.getUid());

                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();

                        Toast.makeText(getContext(), "Cover Photo Saved ", Toast.LENGTH_SHORT).show();


                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                database.getReference().child("Users").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());

                            }
                        });
                    }
                });

            }
        }else{
            if(data.getData() !=null){
                Uri uri = data.getData();
                profiledialoge.show();


                profileImage.setImageURI(uri);



                final StorageReference reference = storage.getReference().child("profilephoto").child(auth.getUid());

                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        profiledialoge.dismiss();

                        Toast.makeText(getContext(), "Profile Photo Saved ", Toast.LENGTH_SHORT).show();


                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                database.getReference().child("Users").child(auth.getUid()).child("profilephoto").setValue(uri.toString());

                            }
                        });
                    }
                });

            }
        }

    }



}
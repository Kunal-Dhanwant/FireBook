package com.example.socialmediaapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.socialmediaapp.Adaptars.UserAdaptar;
import com.example.socialmediaapp.Fragments.Models.Post;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.FragmentAddBinding;
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

import java.util.Date;


public class AddFragment extends Fragment {




    FragmentAddBinding binding;
    Uri uri;


    FirebaseAuth auth;
    FirebaseDatabase database;


    FirebaseStorage storage;

    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);



        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please Wait!");
        dialog.setTitle("Uploading Post.......");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);



        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Users user = snapshot.getValue(Users.class);


                    Picasso.get().load(user.getProfilephoto()).placeholder(R.drawable.cover_place).into(binding.profileImage);
                    binding.Name.setText(user.getName());
                    binding.professionalTV.setText(user.getProfessional());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.postdiscription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String discription = binding.postdiscription.getText().toString();
                if(!discription.isEmpty()){
                    binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.foolow_btn_bg));
                    binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.white));
                    binding.btnPost.setEnabled(true);
                }else{

                    binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.btn_following_bg));
                    binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.grey));
                    binding.btnPost.setEnabled(false);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);

            }
        });






        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

               final StorageReference reference = storage.getReference()
                       .child("posts").child(auth.getUid()).child(new Date().getTime()+"");

               reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                       reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {

                               Post post = new Post();

                               post.setPostAt(new Date().getTime());
                               post.setPostDiscription(binding.postdiscription.getText().toString());
                               post.setPostImage(uri.toString());
                               post.setPostedBy(auth.getUid());



                               database.getReference().child("posts")
                                       .push().setValue(post)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {


                                               dialog.dismiss();


                                               Toast.makeText(getContext(), "Posted Sucessfully", Toast.LENGTH_SHORT).show();
                                           }
                                       });

                           }
                       });

                   }
               });



            }
        });



        return  binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(data.getData()!=null){

           uri = data.getData();

           binding.postIv.setImageURI(uri);
           binding.postIv.setVisibility(View.VISIBLE);
            binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.foolow_btn_bg));
            binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.white));
            binding.btnPost.setEnabled(true);

        }



    }
}
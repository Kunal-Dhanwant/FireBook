package com.example.socialmediaapp.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.socialmediaapp.Adaptars.PostAdaptar;
import com.example.socialmediaapp.Adaptars.StoryAdaptar;
import com.example.socialmediaapp.Fragments.Models.Post;
import com.example.socialmediaapp.Fragments.Models.Story;
import com.example.socialmediaapp.Fragments.Models.UsersStories;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {



    ArrayList<Story> storyArrayList;
    RecyclerView storyRV;
    ShimmerRecyclerView dashboardRv;

    ArrayList<Post> postArrayList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ProgressDialog dialog;

    ImageView profileimage;


    ImageView addstoryImageview;
    RoundedImageView storyIMG;
    ActivityResultLauncher<String> gallaryLauncher;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        dashboardRv = view.findViewById(R.id.dashboardRV);
        dashboardRv.showShimmerAdapter();



         dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
         dialog.setTitle("Story Uploading");
         dialog.setMessage("Please Wait...");
         dialog.setCancelable(false);
         dialog.setCanceledOnTouchOutside(false);




        storyRV = view.findViewById(R.id.stroryRv);
        storyArrayList = new ArrayList<>();



        StoryAdaptar storyAdaptar = new StoryAdaptar(storyArrayList,getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
      //  storyRV.setNestedScrollingEnabled(true);

        storyRV.setLayoutManager(linearLayoutManager);

        storyRV.setAdapter(storyAdaptar);



        database.getReference().child("stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if(snapshot.exists()){
                            storyArrayList.clear();
                            for (DataSnapshot storysnapshot : snapshot.getChildren()){

                                Story story = new Story();
                                story.setStoryBy(storysnapshot.getKey());
                                story.setStoryAt(storysnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UsersStories> stories = new ArrayList<>();
                                for (DataSnapshot snapshot1 : storysnapshot.child("userStories").getChildren()){

                                    UsersStories usersStories = snapshot1.getValue(UsersStories.class);
                                    stories.add(usersStories);

                                }
                                story.setStories(stories);
                                storyArrayList.add(story);
                            }

                            storyAdaptar.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });








        postArrayList = new ArrayList<>();

        PostAdaptar postAdaptar = new PostAdaptar(postArrayList,getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
       // dashboardRv.setNestedScrollingEnabled(false);
        dashboardRv.setLayoutManager(layoutManager);







        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {




                postArrayList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        Post post = dataSnapshot.getValue(Post.class);
                        post.setPostId(dataSnapshot.getKey());
                        postArrayList.add(post);
                    }

                dashboardRv.setAdapter(postAdaptar);
                    dashboardRv.hideShimmerAdapter();
                    postAdaptar.notifyDataSetChanged();


                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        /// open gallary for adding story
        addstoryImageview = view.findViewById(R.id.addstoryIMG);
        storyIMG = view.findViewById(R.id.storyIMG);

        addstoryImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallaryLauncher.launch("image/*");
            }
        });


        gallaryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                storyIMG.setImageURI(result);
                dialog.show();




               //store story in fire base database storage


                final StorageReference reference = storage.getReference()
                        .child("stories")
                        .child(auth.getUid())
                        .child(new Date().getTime()+"");

                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                               Story story = new Story();
                               story.setStoryAt(new Date().getTime());

                               database.getReference()
                                       .child("stories")
                                       .child(auth.getUid())
                                       .child("postedBy")
                                       .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {


                                               UsersStories stories= new UsersStories(uri.toString(),story.getStoryAt());


                                               database.getReference()
                                                       .child("stories")
                                                       .child(auth.getUid())
                                                       .child("userStories")
                                                       .push()
                                                       .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                           @Override
                                                           public void onSuccess(Void unused) {

                                                               dialog.dismiss();
                                                           }
                                                       });

                                           }
                                       });

                            }
                        });



                    }
                });


            }
        });


        profileimage = view.findViewById(R.id.profile_image);
        database.getReference().child("Users")
                .child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        Users users = snapshot.getValue(Users.class);

                        Picasso.get().load(users.getProfilephoto()).placeholder(R.drawable.cover_place)
                                .into(profileimage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return  view;
    }
}
package com.example.socialmediaapp.Adaptars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.Fragments.Models.Story;
import com.example.socialmediaapp.Fragments.Models.UsersStories;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.Users;
import com.example.socialmediaapp.databinding.StoryRvDesignBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdaptar extends RecyclerView.Adapter<StoryAdaptar.viewHolder> {


    public StoryAdaptar(ArrayList<Story> list, Context context) {
        this.list = list;
        this.context = context;
    }

    ArrayList<Story> list;
    Context context;


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.story_rv_design, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Story story = list.get(position);


        if (story.getStories().size() > 0) {


            UsersStories lastStory = story.getStories().get(story.getStories().size() - 1);

            Picasso.get().load(lastStory.getImage()).into(holder.binding.postImage);

            holder.binding.circularStatusView.setPortionsCount(story.getStories().size());


            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(story.getStoryBy())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            Users users = snapshot.getValue(Users.class);
                            Picasso.get()
                                    .load(users.getProfilephoto())
                                    .placeholder(R.drawable.cover_place)
                                    .into(holder.binding.profile);
                            holder.binding.name.setText(users.getName());


                            // set on clicklistner on story items


                            holder.binding.postImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ArrayList<MyStory> myStories = new ArrayList<>();

                                    for (UsersStories stories : story.getStories()) {
                                        myStories.add(new MyStory(stories.getImage()));
                                    }


                                    new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                            .setStoriesList(myStories) // Required
                                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                            .setTitleText(users.getName()) // Default is Hidden
                                            .setSubtitleText("") // Default is Hidden
                                            .setTitleLogoUrl(users.getProfilephoto()) // Default is Hidden
                                            .setStoryClickListeners(new StoryClickListeners() {
                                                @Override
                                                public void onDescriptionClickListener(int position) {
                                                    //your action
                                                }

                                                @Override
                                                public void onTitleIconClickListener(int position) {
                                                    //your action
                                                }
                                            }) // Optional Listeners
                                            .build() // Must be called before calling show method
                                            .show();


                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {


        StoryRvDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StoryRvDesignBinding.bind(itemView);


        }
    }
}

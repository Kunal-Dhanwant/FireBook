package com.example.socialmediaapp.Fragments.Models;

public class Follower {
    String followedBy;
    long followedAt;

    public Follower() {
    }

    public Follower(String followedBy, long followedAt) {
        this.followedBy = followedBy;
        this.followedAt = followedAt;
    }

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public long getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(long followedAt) {
        this.followedAt = followedAt;
    }



}

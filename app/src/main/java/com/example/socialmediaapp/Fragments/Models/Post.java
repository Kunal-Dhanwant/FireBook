package com.example.socialmediaapp.Fragments.Models;

public class Post {


    String postId;
    String postedBy;
    String postDiscription;
    String postImage;
    long postAt;
    int postLike;
    int commentsCount;



    public Post() {
    }


    public Post(String postId, String postedBy, String postDiscription, String postImage, long postAt) {
        this.postId = postId;
        this.postedBy = postedBy;
        this.postDiscription = postDiscription;
        this.postImage = postImage;
        this.postAt = postAt;

    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDiscription() {
        return postDiscription;
    }

    public void setPostDiscription(String postDiscription) {
        this.postDiscription = postDiscription;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public long getPostAt() {
        return postAt;
    }

    public void setPostAt(long postAt) {
        this.postAt = postAt;
    }
}

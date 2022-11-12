package com.example.socialmediaapp.Fragments.Models;

public class Comments {


    private String commentBody,commentBy;
    private  long commentedAt;

    public Comments() {
    }

    public Comments(String commentBody, String commentBy, long commentedAt) {
        this.commentBody = commentBody;
        this.commentBy = commentBy;
        this.commentedAt = commentedAt;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public long getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(long commentedAt) {
        this.commentedAt = commentedAt;
    }
}

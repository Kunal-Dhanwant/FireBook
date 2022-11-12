package com.example.socialmediaapp.Fragments.Models;

public class Notification {



    private String notificationBy;
    private  long notificationAt;
    private  String type;
    private  String postID;
    private  String notificationID;

    private  String postBy;
    private  boolean checkOpen;

    public Notification() {
    }

    public Notification(String notificationBy, long notificationAt, String type, String postID, String postBy, boolean checkOpen) {
        this.notificationBy = notificationBy;
        this.notificationAt = notificationAt;
        this.type = type;
        this.postID = postID;
        this.postBy = postBy;
        this.checkOpen = checkOpen;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationBy() {
        return notificationBy;
    }

    public void setNotificationBy(String notificationBy) {
        this.notificationBy = notificationBy;
    }

    public long getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(long notificationAt) {
        this.notificationAt = notificationAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostBy() {
        return postBy;
    }

    public void setPostBy(String postBy) {
        this.postBy = postBy;
    }

    public boolean isCheckOpen() {
        return checkOpen;
    }

    public void setCheckOpen(boolean checkOpen) {
        this.checkOpen = checkOpen;
    }
}

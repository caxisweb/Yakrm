package com.codeclinic.yakrm.Models;

public class NotificationListModel {
    String notification, duration;

    public NotificationListModel(String notification, String duration) {
        this.notification = notification;
        this.duration = duration;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

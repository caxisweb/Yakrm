package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouritesListModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("arab_message")
    @Expose
    private String arab_message;
    @SerializedName("data")
    @Expose
    private List<FavouritesListItemModel> data = null;

    public String getArab_message() {
        return arab_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FavouritesListItemModel> getData() {
        return data;
    }

    public void setData(List<FavouritesListItemModel> data) {
        this.data = data;
    }
}

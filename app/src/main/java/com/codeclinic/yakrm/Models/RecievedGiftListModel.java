package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecievedGiftListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("arab_message")
    @Expose
    private String arab_message;

    public String getArab_message() {
        return arab_message;
    }
    @SerializedName("data")
    @Expose
    private List<RecievedGftListItemModel> data = null;

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

    public List<RecievedGftListItemModel> getData() {
        return data;
    }

    public void setData(List<RecievedGftListItemModel> data) {
        this.data = data;
    }
}

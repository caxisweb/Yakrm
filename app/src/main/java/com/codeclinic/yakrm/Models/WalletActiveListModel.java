package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletActiveListModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("admin_profit_dis")
    @Expose
    private String adminProfitDis;
    @SerializedName("data")
    @Expose
    private List<WalletActiveListItemModel> data = null;

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

    public String getAdminProfitDis() {
        return adminProfitDis;
    }

    public void setAdminProfitDis(String adminProfitDis) {
        this.adminProfitDis = adminProfitDis;
    }

    public List<WalletActiveListItemModel> getData() {
        return data;
    }

    public void setData(List<WalletActiveListItemModel> data) {
        this.data = data;
    }

}

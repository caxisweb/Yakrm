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
    @SerializedName("total_active_voucher")
    @Expose
    private String total_active_voucher;
    @SerializedName("total_favourites")
    @Expose
    private String total_favourites;
    @SerializedName("voucher_end_soon")
    @Expose
    private String voucher_end_soon;
    @SerializedName("voucher_ended")
    @Expose
    private String voucher_ended;
    @SerializedName("admin_profit_dis")
    @Expose
    private String adminProfitDis;
    @SerializedName("data")
    @Expose
    private List<WalletActiveListItemModel> data = null;
    @SerializedName("arab_message")
    @Expose
    private String arab_message;

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


    public String getTotal_active_voucher() {
        return total_active_voucher;
    }

    public void setTotal_active_voucher(String total_active_voucher) {
        this.total_active_voucher = total_active_voucher;
    }

    public String getTotal_favourites() {
        return total_favourites;
    }

    public void setTotal_favourites(String total_favourites) {
        this.total_favourites = total_favourites;
    }

    public String getVoucher_end_soon() {
        return voucher_end_soon;
    }

    public void setVoucher_end_soon(String voucher_end_soon) {
        this.voucher_end_soon = voucher_end_soon;
    }

    public String getVoucher_ended() {
        return voucher_ended;
    }

    public void setVoucher_ended(String voucher_ended) {
        this.voucher_ended = voucher_ended;
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

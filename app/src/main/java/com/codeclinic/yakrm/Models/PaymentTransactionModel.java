package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentTransactionModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("wallet")
    @Expose
    private String Wallet;

    public String getWallet() {
        return Wallet;
    }

    public void setWallet(String wallet) {
        Wallet = wallet;
    }

    @SerializedName("message")
    @Expose
    private String message;

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
}

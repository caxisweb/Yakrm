package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionsListItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("amount_from_wallet")
    @Expose
    private String amountFromWallet;
    @SerializedName("amount_from_bank")
    @Expose
    private String amountFromBank;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("scan_voucher_type")
    @Expose
    private String scanVoucherType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAmountFromWallet() {
        return amountFromWallet;
    }

    public void setAmountFromWallet(String amountFromWallet) {
        this.amountFromWallet = amountFromWallet;
    }

    public String getAmountFromBank() {
        return amountFromBank;
    }

    public void setAmountFromBank(String amountFromBank) {
        this.amountFromBank = amountFromBank;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getScanVoucherType() {
        return scanVoucherType;
    }

    public void setScanVoucherType(String scanVoucherType) {
        this.scanVoucherType = scanVoucherType;
    }
}

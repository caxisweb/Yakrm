package com.yakrm.codeclinic.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionsListItemModel {
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("voucher_price")
    @Expose
    private String voucherPrice;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

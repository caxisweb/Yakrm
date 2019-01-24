package com.yakrm.codeclinic.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouritesListItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_image")
    @Expose
    private String brandImage;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("voucher_id")
    @Expose
    private String voucherId;
    @SerializedName("voucher_is_active")
    @Expose
    private String voucherIsActive;
    @SerializedName("vendor_is_active")
    @Expose
    private String vendorIsActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherIsActive() {
        return voucherIsActive;
    }

    public void setVoucherIsActive(String voucherIsActive) {
        this.voucherIsActive = voucherIsActive;
    }

    public String getVendorIsActive() {
        return vendorIsActive;
    }

    public void setVendorIsActive(String vendorIsActive) {
        this.vendorIsActive = vendorIsActive;
    }
}

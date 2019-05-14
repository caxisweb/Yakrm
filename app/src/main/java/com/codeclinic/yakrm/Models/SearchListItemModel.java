package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchListItemModel {
    @SerializedName("voucher_id")
    @Expose
    private String voucherId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("voucher_price")
    @Expose
    private String voucherPrice;
    @SerializedName("voucher_type")
    @Expose
    private String voucherType;
    @SerializedName("expired_at")
    @Expose
    private String expiredAt;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_image")
    @Expose
    private String brandImage;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("gift_category_name")
    @Expose
    private String giftCategoryName;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGiftCategoryName() {
        return giftCategoryName;
    }

    public void setGiftCategoryName(String giftCategoryName) {
        this.giftCategoryName = giftCategoryName;
    }
}

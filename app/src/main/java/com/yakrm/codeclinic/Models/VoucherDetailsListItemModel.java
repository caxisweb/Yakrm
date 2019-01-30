package com.yakrm.codeclinic.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherDetailsListItemModel {
    @SerializedName("voucher_id")
    @Expose
    private String voucherId;
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

    @SerializedName("voucher_image")
    @Expose
    private String voucherImage;

    public String getVoucherImage() {
        return voucherImage;
    }

    public void setVoucherImage(String voucherImage) {
        this.voucherImage = voucherImage;
    }
    @SerializedName("gift_category_name")
    @Expose
    private String giftCategoryName;
    @SerializedName("expired_at")
    @Expose
    private String expiredAt;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
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

    public String getGiftCategoryName() {
        return giftCategoryName;
    }

    public void setGiftCategoryName(String giftCategoryName) {
        this.giftCategoryName = giftCategoryName;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

}

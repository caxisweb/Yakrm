package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecievedGftListItemModel {

    @SerializedName("voucher_gift_send_id")
    @Expose
    private String voucherGiftSendId;
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
    @SerializedName("expired_at")
    @Expose
    private String expiredAt;
    @SerializedName("voucher_image")
    @Expose
    private String voucherImage;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("scan_code")
    @Expose
    private String scanCode;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("attached_video_image")
    @Expose
    private String attachedVideoImage;
    @SerializedName("sent_user_id")
    @Expose
    private String sentUserId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("scan_voucher_type")
    @Expose
    private String scanVoucherType;

    public String getVoucherGiftSendId() {
        return voucherGiftSendId;
    }

    public void setVoucherGiftSendId(String voucherGiftSendId) {
        this.voucherGiftSendId = voucherGiftSendId;
    }

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

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getVoucherImage() {
        return voucherImage;
    }

    public void setVoucherImage(String voucherImage) {
        this.voucherImage = voucherImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachedVideoImage() {
        return attachedVideoImage;
    }

    public void setAttachedVideoImage(String attachedVideoImage) {
        this.attachedVideoImage = attachedVideoImage;
    }

    public String getSentUserId() {
        return sentUserId;
    }

    public void setSentUserId(String sentUserId) {
        this.sentUserId = sentUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScanVoucherType() {
        return scanVoucherType;
    }

    public void setScanVoucherType(String scanVoucherType) {
        this.scanVoucherType = scanVoucherType;
    }
}

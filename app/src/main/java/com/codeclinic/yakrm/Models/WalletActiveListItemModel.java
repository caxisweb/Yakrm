package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletActiveListItemModel {
    @SerializedName("voucher_gift_send_id")
    @Expose
    private String vouchergiftsendid;
    @SerializedName("voucher_payment_detail_id")
    @Expose
    private String voucherPaymentDetailId;
    @SerializedName("voucher_id")
    @Expose
    private String voucherId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_name_arab")
    @Expose
    private String brand_name_arab;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("voucher_price")
    @Expose
    private String voucherPrice;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("voucher_type")
    @Expose
    private String voucherType;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("expired_at")
    @Expose
    private String expired_at;
    @SerializedName("voucher_image")
    @Expose
    private String voucherImage;
    @SerializedName("scan_code")
    @Expose
    private String scanCode;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("scan_voucher_type")
    @Expose
    private String scanVoucherType;
    @SerializedName("replace_voucher_payment_id")
    @Expose
    private String replaceVoucherPaymentId;
    @SerializedName("new_voucher_id")
    @Expose
    private String newVoucherId;
    @SerializedName("voucher_description")
    @Expose
    private String voucher_description;

    public String getBrand_name_arab() {
        return brand_name_arab;
    }

    public String getVouchergiftsendid() {
        return vouchergiftsendid;
    }

    public void setVouchergiftsendid(String vouchergiftsendid) {
        this.vouchergiftsendid = vouchergiftsendid;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getVoucher_description() {
        return voucher_description;
    }

    public void setVoucher_description(String voucher_description) {
        this.voucher_description = voucher_description;
    }

    public String getVoucherPaymentDetailId() {
        return voucherPaymentDetailId;
    }

    public void setVoucherPaymentDetailId(String voucherPaymentDetailId) {
        this.voucherPaymentDetailId = voucherPaymentDetailId;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

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

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVoucherImage() {
        return voucherImage;
    }

    public void setVoucherImage(String voucherImage) {
        this.voucherImage = voucherImage;
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

    public String getScanVoucherType() {
        return scanVoucherType;
    }

    public void setScanVoucherType(String scanVoucherType) {
        this.scanVoucherType = scanVoucherType;
    }

    public String getReplaceVoucherPaymentId() {
        return replaceVoucherPaymentId;
    }

    public void setReplaceVoucherPaymentId(String replaceVoucherPaymentId) {
        this.replaceVoucherPaymentId = replaceVoucherPaymentId;
    }

    public String getNewVoucherId() {
        return newVoucherId;
    }

    public void setNewVoucherId(String newVoucherId) {
        this.newVoucherId = newVoucherId;
    }
}

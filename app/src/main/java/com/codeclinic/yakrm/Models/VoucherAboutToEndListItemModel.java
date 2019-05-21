package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherAboutToEndListItemModel {

    @SerializedName("voucher_payment_detail_id")
    @Expose
    private String voucherPaymentDetailId;
    @SerializedName("voucher_id")
    @Expose
    private String voucherId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
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
}

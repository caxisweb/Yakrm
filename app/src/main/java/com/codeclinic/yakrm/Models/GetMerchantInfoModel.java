package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMerchantInfoModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("merchant_logo")
    @Expose
    private String merchantLogo;
    @SerializedName("merchant_title")
    @Expose
    private String merchantTitle;
    @SerializedName("merchant_phone")
    @Expose
    private String merchantPhone;
    @SerializedName("merchant_currency")
    @Expose
    private String merchantCurrency;
    @SerializedName("transaction_currency")
    @Expose
    private String transactionCurrency;
    @SerializedName("transaction_amount")
    @Expose
    private String transactionAmount;
    @SerializedName("transaction_exchange_rate")
    @Expose
    private Integer transactionExchangeRate;
    @SerializedName("max_amount")
    @Expose
    private String maxAmount;
    @SerializedName("min_amount")
    @Expose
    private String minAmount;
    @SerializedName("profile_title")
    @Expose
    private String profileTitle;
    @SerializedName("device_fp_session_id")
    @Expose
    private String deviceFpSessionId;
    @SerializedName("device_fp_org_id")
    @Expose
    private String deviceFpOrgId;
    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("payment_timeout")
    @Expose
    private String paymentTimeout;
    @SerializedName("merchant_logo_file")
    @Expose
    private String merchantLogoFile;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public String getMerchantTitle() {
        return merchantTitle;
    }

    public void setMerchantTitle(String merchantTitle) {
        this.merchantTitle = merchantTitle;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getMerchantCurrency() {
        return merchantCurrency;
    }

    public void setMerchantCurrency(String merchantCurrency) {
        this.merchantCurrency = merchantCurrency;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Integer getTransactionExchangeRate() {
        return transactionExchangeRate;
    }

    public void setTransactionExchangeRate(Integer transactionExchangeRate) {
        this.transactionExchangeRate = transactionExchangeRate;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getProfileTitle() {
        return profileTitle;
    }

    public void setProfileTitle(String profileTitle) {
        this.profileTitle = profileTitle;
    }

    public String getDeviceFpSessionId() {
        return deviceFpSessionId;
    }

    public void setDeviceFpSessionId(String deviceFpSessionId) {
        this.deviceFpSessionId = deviceFpSessionId;
    }

    public String getDeviceFpOrgId() {
        return deviceFpOrgId;
    }

    public void setDeviceFpOrgId(String deviceFpOrgId) {
        this.deviceFpOrgId = deviceFpOrgId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getPaymentTimeout() {
        return paymentTimeout;
    }

    public void setPaymentTimeout(String paymentTimeout) {
        this.paymentTimeout = paymentTimeout;
    }

    public String getMerchantLogoFile() {
        return merchantLogoFile;
    }

    public void setMerchantLogoFile(String merchantLogoFile) {
        this.merchantLogoFile = merchantLogoFile;
    }
}

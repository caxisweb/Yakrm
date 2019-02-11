package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCardListItemModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("holder_name")
    @Expose
    private String holderName;
    @SerializedName("card_number")
    @Expose
    private String cardNumber;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("security_number")
    @Expose
    private String securityNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }
}

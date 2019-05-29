package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentStatusModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("paymentBrand")
    @Expose
    private String paymentBrand;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("descriptor")
    @Expose
    private String descriptor;
    @SerializedName("result")
    @Expose
    private PaymentStatusResultModel result;
    @SerializedName("resultDetails")
    @Expose
    private PaymentStatusResultDetail resultDetails;
    @SerializedName("card")
    @Expose
    private PaymentStatusCardModel card;
    @SerializedName("customer")
    @Expose
    private PaymentStatusCustomerModel customer;
    @SerializedName("customParameters")
    @Expose
    private PaymentStatusCustomerParameterModel customParameters;
    @SerializedName("risk")
    @Expose
    private PaymentStatusRiskModel risk;
    @SerializedName("buildNumber")
    @Expose
    private String buildNumber;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("ndc")
    @Expose
    private String ndc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentBrand() {
        return paymentBrand;
    }

    public void setPaymentBrand(String paymentBrand) {
        this.paymentBrand = paymentBrand;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public PaymentStatusResultModel getResult() {
        return result;
    }

    public void setResult(PaymentStatusResultModel result) {
        this.result = result;
    }

    public PaymentStatusResultDetail getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(PaymentStatusResultDetail resultDetails) {
        this.resultDetails = resultDetails;
    }

    public PaymentStatusCardModel getCard() {
        return card;
    }

    public void setCard(PaymentStatusCardModel card) {
        this.card = card;
    }

    public PaymentStatusCustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(PaymentStatusCustomerModel customer) {
        this.customer = customer;
    }

    public PaymentStatusCustomerParameterModel getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(PaymentStatusCustomerParameterModel customParameters) {
        this.customParameters = customParameters;
    }

    public PaymentStatusRiskModel getRisk() {
        return risk;
    }

    public void setRisk(PaymentStatusRiskModel risk) {
        this.risk = risk;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNdc() {
        return ndc;
    }

    public void setNdc(String ndc) {
        this.ndc = ndc;
    }
}

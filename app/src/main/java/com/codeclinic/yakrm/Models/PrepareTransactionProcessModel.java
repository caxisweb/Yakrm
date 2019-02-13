package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.paytabs.paytabs_sdk.http.PayerAuthEnrollReply;

public class PrepareTransactionProcessModel {
    @SerializedName("merchantReferenceCode")
    @Expose
    private String merchantReferenceCode;
    @SerializedName("requestID")
    @Expose
    private String requestID;
    @SerializedName("decision")
    @Expose
    private String decision;
    @SerializedName("reasonCode")
    @Expose
    private Integer reasonCode;
    @SerializedName("requestToken")
    @Expose
    private String requestToken;
    @SerializedName("payerAuthEnrollReply")
    @Expose
    private PayerAuthEnrollReply payerAuthEnrollReply;
    @SerializedName("local_transaction_id")
    @Expose
    private Integer localTransactionId;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("tokenized")
    @Expose
    private String tokenized;

    public String getMerchantReferenceCode() {
        return merchantReferenceCode;
    }

    public void setMerchantReferenceCode(String merchantReferenceCode) {
        this.merchantReferenceCode = merchantReferenceCode;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Integer getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(Integer reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public PayerAuthEnrollReply getPayerAuthEnrollReply() {
        return payerAuthEnrollReply;
    }

    public void setPayerAuthEnrollReply(PayerAuthEnrollReply payerAuthEnrollReply) {
        this.payerAuthEnrollReply = payerAuthEnrollReply;
    }

    public Integer getLocalTransactionId() {
        return localTransactionId;
    }

    public void setLocalTransactionId(Integer localTransactionId) {
        this.localTransactionId = localTransactionId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getTokenized() {
        return tokenized;
    }

    public void setTokenized(String tokenized) {
        this.tokenized = tokenized;
    }
}

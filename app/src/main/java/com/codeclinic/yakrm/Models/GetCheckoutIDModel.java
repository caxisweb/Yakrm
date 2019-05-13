package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCheckoutIDModel {
    @SerializedName("result")
    @Expose
    private CheckoutResultModel result;
    @SerializedName("buildNumber")
    @Expose
    private String buildNumber;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("ndc")
    @Expose
    private String ndc;
    @SerializedName("id")
    @Expose
    private String id;

    public CheckoutResultModel getResult() {
        return result;
    }

    public void setResult(CheckoutResultModel result) {
        this.result = result;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayerAuthEnrollReply {
    @SerializedName("reasonCode")
    @Expose
    private Integer reasonCode;
    @SerializedName("acsURL")
    @Expose
    private String acsURL;
    @SerializedName("paReq")
    @Expose
    private String paReq;
    @SerializedName("proxyPAN")
    @Expose
    private String proxyPAN;
    @SerializedName("xid")
    @Expose
    private String xid;
    @SerializedName("proofXML")
    @Expose
    private String proofXML;
    @SerializedName("veresEnrolled")
    @Expose
    private String veresEnrolled;
    @SerializedName("authenticationPath")
    @Expose
    private String authenticationPath;

    public Integer getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(Integer reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getAcsURL() {
        return acsURL;
    }

    public void setAcsURL(String acsURL) {
        this.acsURL = acsURL;
    }

    public String getPaReq() {
        return paReq;
    }

    public void setPaReq(String paReq) {
        this.paReq = paReq;
    }

    public String getProxyPAN() {
        return proxyPAN;
    }

    public void setProxyPAN(String proxyPAN) {
        this.proxyPAN = proxyPAN;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getProofXML() {
        return proofXML;
    }

    public void setProofXML(String proofXML) {
        this.proofXML = proofXML;
    }

    public String getVeresEnrolled() {
        return veresEnrolled;
    }

    public void setVeresEnrolled(String veresEnrolled) {
        this.veresEnrolled = veresEnrolled;
    }

    public String getAuthenticationPath() {
        return authenticationPath;
    }

    public void setAuthenticationPath(String authenticationPath) {
        this.authenticationPath = authenticationPath;
    }

}

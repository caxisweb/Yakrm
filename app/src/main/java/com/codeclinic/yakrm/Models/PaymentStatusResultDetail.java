package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentStatusResultDetail {
    @SerializedName("CscResultCode")
    @Expose
    private String cscResultCode;
    @SerializedName("TransactionIdentfier")
    @Expose
    private String transactionIdentfier;
    @SerializedName("ConnectorTxID1")
    @Expose
    private String connectorTxID1;
    @SerializedName("connectorId")
    @Expose
    private String connectorId;
    @SerializedName("VerStatus")
    @Expose
    private String verStatus;
    @SerializedName("BatchNo")
    @Expose
    private String batchNo;
    @SerializedName("endToEndId")
    @Expose
    private String endToEndId;
    @SerializedName("AuthorizeId")
    @Expose
    private String authorizeId;
    @SerializedName("AvsResultCode")
    @Expose
    private String avsResultCode;

    public String getCscResultCode() {
        return cscResultCode;
    }

    public void setCscResultCode(String cscResultCode) {
        this.cscResultCode = cscResultCode;
    }

    public String getTransactionIdentfier() {
        return transactionIdentfier;
    }

    public void setTransactionIdentfier(String transactionIdentfier) {
        this.transactionIdentfier = transactionIdentfier;
    }

    public String getConnectorTxID1() {
        return connectorTxID1;
    }

    public void setConnectorTxID1(String connectorTxID1) {
        this.connectorTxID1 = connectorTxID1;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    public String getVerStatus() {
        return verStatus;
    }

    public void setVerStatus(String verStatus) {
        this.verStatus = verStatus;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }

    public String getAuthorizeId() {
        return authorizeId;
    }

    public void setAuthorizeId(String authorizeId) {
        this.authorizeId = authorizeId;
    }

    public String getAvsResultCode() {
        return avsResultCode;
    }

    public void setAvsResultCode(String avsResultCode) {
        this.avsResultCode = avsResultCode;
    }
}

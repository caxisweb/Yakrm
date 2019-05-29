package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentStatusCustomerModel {
    @SerializedName("SHOPPER_MSDKIntegrationType")
    @Expose
    private String sHOPPERMSDKIntegrationType;
    @SerializedName("SHOPPER_device")
    @Expose
    private String sHOPPERDevice;
    @SerializedName("CTPE_DESCRIPTOR_TEMPLATE")
    @Expose
    private String cTPEDESCRIPTORTEMPLATE;
    @SerializedName("SHOPPER_OS")
    @Expose
    private String sHOPPEROS;
    @SerializedName("SHOPPER_MSDKVersion")
    @Expose
    private String sHOPPERMSDKVersion;

    public String getSHOPPERMSDKIntegrationType() {
        return sHOPPERMSDKIntegrationType;
    }

    public void setSHOPPERMSDKIntegrationType(String sHOPPERMSDKIntegrationType) {
        this.sHOPPERMSDKIntegrationType = sHOPPERMSDKIntegrationType;
    }

    public String getSHOPPERDevice() {
        return sHOPPERDevice;
    }

    public void setSHOPPERDevice(String sHOPPERDevice) {
        this.sHOPPERDevice = sHOPPERDevice;
    }

    public String getCTPEDESCRIPTORTEMPLATE() {
        return cTPEDESCRIPTORTEMPLATE;
    }

    public void setCTPEDESCRIPTORTEMPLATE(String cTPEDESCRIPTORTEMPLATE) {
        this.cTPEDESCRIPTORTEMPLATE = cTPEDESCRIPTORTEMPLATE;
    }

    public String getSHOPPEROS() {
        return sHOPPEROS;
    }

    public void setSHOPPEROS(String sHOPPEROS) {
        this.sHOPPEROS = sHOPPEROS;
    }

    public String getSHOPPERMSDKVersion() {
        return sHOPPERMSDKVersion;
    }

    public void setSHOPPERMSDKVersion(String sHOPPERMSDKVersion) {
        this.sHOPPERMSDKVersion = sHOPPERMSDKVersion;
    }

}

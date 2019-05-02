package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutApplicationModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("about_application_arab")
    @Expose
    private String aboutApplicationArab;
    @SerializedName("about_application_english")
    @Expose
    private String aboutApplicationEnglish;
    @SerializedName("terms_and_condition_arab")
    @Expose
    private String termsAndConditionArab;
    @SerializedName("terms_and_condition_english")
    @Expose
    private String termsAndConditionEnglish;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAboutApplicationArab() {
        return aboutApplicationArab;
    }

    public void setAboutApplicationArab(String aboutApplicationArab) {
        this.aboutApplicationArab = aboutApplicationArab;
    }

    public String getAboutApplicationEnglish() {
        return aboutApplicationEnglish;
    }

    public void setAboutApplicationEnglish(String aboutApplicationEnglish) {
        this.aboutApplicationEnglish = aboutApplicationEnglish;
    }

    public String getTermsAndConditionArab() {
        return termsAndConditionArab;
    }

    public void setTermsAndConditionArab(String termsAndConditionArab) {
        this.termsAndConditionArab = termsAndConditionArab;
    }

    public String getTermsAndConditionEnglish() {
        return termsAndConditionEnglish;
    }

    public void setTermsAndConditionEnglish(String termsAndConditionEnglish) {
        this.termsAndConditionEnglish = termsAndConditionEnglish;
    }
}
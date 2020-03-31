package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftCategoryBannersModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gift_category_id")
    @Expose
    private String giftCategoryId;
    @SerializedName("gift_category_banner")
    @Expose
    private String giftCategoryBanner;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGiftCategoryId() {
        return giftCategoryId;
    }

    public void setGiftCategoryId(String giftCategoryId) {
        this.giftCategoryId = giftCategoryId;
    }

    public String getGiftCategoryBanner() {
        return giftCategoryBanner;
    }

    public void setGiftCategoryBanner(String giftCategoryBanner) {
        this.giftCategoryBanner = giftCategoryBanner;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

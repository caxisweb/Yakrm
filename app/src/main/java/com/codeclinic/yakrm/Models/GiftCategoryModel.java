package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftCategoryModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gift_category_name")
    @Expose
    private String giftCategoryName;
    @SerializedName("arab_gift_category_name")
    @Expose
    private String arabGiftCategoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGiftCategoryName() {
        return giftCategoryName;
    }

    public void setGiftCategoryName(String giftCategoryName) {
        this.giftCategoryName = giftCategoryName;
    }

    public String getArabGiftCategoryName() {
        return arabGiftCategoryName;
    }

    public void setArabGiftCategoryName(String arabGiftCategoryName) {
        this.arabGiftCategoryName = arabGiftCategoryName;
    }
}

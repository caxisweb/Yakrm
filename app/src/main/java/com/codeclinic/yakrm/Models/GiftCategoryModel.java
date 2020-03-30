package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("gift_category_icon")
    @Expose
    private String gift_category_icon;
    @SerializedName("gift_category_banner")
    @Expose
    private String gift_category_banner;
    @SerializedName("gift_category_banners")
    @Expose
    private List<GiftCategoryBannersModel> giftCategoryBanners = null;

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

    public String getGift_category_icon() {
        return gift_category_icon;
    }

    public void setArabGiftCategoryName(String arabGiftCategoryName) {
        this.arabGiftCategoryName = arabGiftCategoryName;
    }

    public String getGift_category_banner() {
        return gift_category_banner;
    }

    public List<GiftCategoryBannersModel> getGiftCategoryBanners() {
        return giftCategoryBanners;
    }
}

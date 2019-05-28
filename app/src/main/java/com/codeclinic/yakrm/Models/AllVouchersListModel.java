package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllVouchersListModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_cart_item")
    @Expose
    private String total_cart_item;
    @SerializedName("data")
    @Expose
    private List<AllVoucherListItemModel> data = null;
    @SerializedName("gift_category")
    @Expose
    private List<GiftCategoryModel> giftCategory = null;

    public List<GiftCategoryModel> getGiftCategory() {
        return giftCategory;
    }

    public void setGiftCategory(List<GiftCategoryModel> giftCategory) {
        this.giftCategory = giftCategory;
    }

    public String getTotal_cart_item() {
        return total_cart_item;
    }

    public void setTotal_cart_item(String total_cart_item) {
        this.total_cart_item = total_cart_item;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AllVoucherListItemModel> getData() {
        return data;
    }

    public void setData(List<AllVoucherListItemModel> data) {
        this.data = data;
    }
}

package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoucherDetailsListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_name_arab")
    @Expose
    private String brand_name_arab;
    @SerializedName("brand_image")
    @Expose
    private String brandImage;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("brand_description_arab")
    @Expose
    private String brand_description_arab;
    @SerializedName("is_favourite")
    @Expose
    private String isFavourite;
    @SerializedName("data")
    @Expose
    private List<VoucherDetailsListItemModel> data = null;

    public String getBrand_name_arab() {
        return brand_name_arab;
    }

    public String getBrand_description_arab() {
        return brand_description_arab;
    }

    public void setBrand_description_arab(String brand_description_arab) {
        this.brand_description_arab = brand_description_arab;
    }

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

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public List<VoucherDetailsListItemModel> getData() {
        return data;
    }

    public void setData(List<VoucherDetailsListItemModel> data) {
        this.data = data;
    }

}

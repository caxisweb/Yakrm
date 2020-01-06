package com.codeclinic.yakrm.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandListItemModel {
    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("total_voucher")
    @Expose
    private String totalVoucher;

    public String getBrand_name_arab() {
        return brand_name_arab;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTotalVoucher() {
        return totalVoucher;
    }

    public void setTotalVoucher(String totalVoucher) {
        this.totalVoucher = totalVoucher;
    }
}

package com.codeclinic.yakrm.DeliveryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("arab_message")
    @Expose
    private String arab_message;
    @SerializedName("order_id")
    @Expose
    private String order_id;

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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getArab_message() {
        return arab_message;
    }

    public void setArab_message(String arab_message) {
        this.arab_message = arab_message;
    }
}

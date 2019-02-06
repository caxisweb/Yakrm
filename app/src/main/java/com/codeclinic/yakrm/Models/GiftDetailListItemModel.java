package com.codeclinic.yakrm.Models;

public class GiftDetailListItemModel {
    String valu, discount, pay;

    public GiftDetailListItemModel(String valu, String discount, String pay) {

        this.valu = valu;
        this.discount = discount;
        this.pay = pay;
    }

    public String getValu() {
        return valu;
    }

    public void setValu(String valu) {
        this.valu = valu;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}

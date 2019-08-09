package com.codeclinic.yakrm.Hyperpay_checkout;


public interface PaymentStatusRequestListener {

    void onErrorOccurred();

    void onPaymentStatusReceived(String paymentStatus);
}

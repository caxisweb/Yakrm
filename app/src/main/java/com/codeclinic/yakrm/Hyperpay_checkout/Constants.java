package com.codeclinic.yakrm.Hyperpay_checkout;

import java.util.LinkedHashSet;
import java.util.Set;


public class Constants {

    public static final int CONNECTION_TIMEOUT = 5000;
    //public static final String BASE_URL = "http://52.59.56.185";
    public static final String BASE_URL = "http://test.yakrm.com/api/";
    //public static final String MERCHANT_ID = "ff80808138516ef4013852936ec200f2";
    public static final String MERCHANT_ID = "8acda4cc6b8870a6016b8d8cabae2013";
    public static final String LOG_TAG = "msdk.demo";

    /* The configuration values to change across the app */
    public static class Config {

        /* The payment brands for Ready-to-Use UI and Payment Button */
        public static final Set<String> PAYMENT_BRANDS;
        /* The default payment brand for payment button */
        public static final String PAYMENT_BUTTON_BRAND = "GOOGLEPAY";
        /* The default amount and currency */
        public static final String AMOUNT = "10.00";
        public static final String CURRENCY = "SAR";
        public static final String CARD_BRAND = "VISA";

        /* The card info for SDK & Your Own UI*/
        /*public static final String CARD_BRAND = "VISA";
        public static final String CARD_HOLDER_NAME = "JOHN DOE";
        public static final String CARD_NUMBER = "4200000000000000";
        public static final String CARD_EXPIRY_MONTH = "07";
        public static final String CARD_EXPIRY_YEAR = "21";
        public static final String CARD_CVV = "123";*/
        public static final String CARD_HOLDER_NAME = "Ahmed Gasem";
        public static final String CARD_NUMBER = "4508249992511960";
        public static final String CARD_EXPIRY_MONTH = "02";
        public static final String CARD_EXPIRY_YEAR = "20";
        public static final String CARD_CVV = "698";

        static {
            PAYMENT_BRANDS = new LinkedHashSet<>();

            PAYMENT_BRANDS.add("VISA");
            PAYMENT_BRANDS.add("MASTER");
            PAYMENT_BRANDS.add("PAYPAL");
            PAYMENT_BRANDS.add("GOOGLEPAY");
        }
    }
}

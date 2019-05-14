package com.codeclinic.yakrm.Utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.codeclinic.yakrm.BuildConfig;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;

/**
 * Created by shardullavekar on 19/08/17.
 */

public class CheckoutBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



        ComponentName componentName = new ComponentName(BuildConfig.APPLICATION_ID, CheckoutBroadcastReceiver.class.getCanonicalName());

        String paymentBrand = intent.getStringExtra(CheckoutActivity.EXTRA_PAYMENT_BRAND);
        String checkoutId = intent.getStringExtra(CheckoutActivity.EXTRA_CHECKOUT_ID);


        intent = new Intent(context, CheckoutActivity.class);
        intent.setPackage(BuildConfig.APPLICATION_ID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(CheckoutActivity.ACTION_PAYMENT_METHOD_SELECTED);
        intent.putExtra(CheckoutActivity.EXTRA_CHECKOUT_ID, checkoutId);

        context.startActivity(intent);
    }

}

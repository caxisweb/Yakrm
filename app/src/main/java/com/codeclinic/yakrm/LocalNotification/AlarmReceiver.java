package com.codeclinic.yakrm.LocalNotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.codeclinic.yakrm.Activities.CartActivity;
import com.codeclinic.yakrm.R;

import java.util.Date;

import static android.app.Notification.GROUP_ALERT_SUMMARY;

/**
 * Created by ptyagi on 4/17/17.
 */

/**
 * AlarmReceiver handles the broadcast message and generates Notification
 */
public class AlarmReceiver extends BroadcastReceiver {

    String channelId = "channel-01";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_LOW;

    @Override
    public void onReceive(Context context, Intent intent) {
      /*  //Get notification manager to manage/send notifications
        //Intent to invoke app when click on notification.
        //In this sample, we want to start/launch this sample app when user clicks on notification
        Intent intentToRepeat = new Intent(context, CartActivity.class);
        //set flag to restart/relaunch the app
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Pending intent to handle launch of Activity in intent above
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);
        //Build notification
        Notification repeatedNotification = buildLocalNotification(context, pendingIntent).build();
        //Send local notification
        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);*/


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            mChannel.setSound(null, null);
            notificationManager.createNotificationChannel(mChannel);

        }

        Intent intentToRepeat = new Intent(context, CartActivity.class);
        //set flag to restart/relaunch the app
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Pending intent to handle launch of Activity in intent above
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build notification
        Notification repeatedNotification = buildLocalNotification(context, pendingIntent).setGroupAlertBehavior(GROUP_ALERT_SUMMARY).setGroup("My group").setGroupSummary(false).build();

        //Send local notification
        //NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationHelper.getNotificationManager(context).notify(0, repeatedNotification);
    }

    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.app_logo)
                .setLargeIcon(bitmap)
                .setContentTitle(context.getResources().getString(R.string.You_have_few_items_in_the_cart_to_purchase))
                //Vibration
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                //LED
                .setLights(Color.RED, 3000, 3000)
                .setContentText(context.getResources().getString(R.string.Purchase_Voucher))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(channelId);
        /* .setGroupAlertBehavior()*/

        return mBuilder;
    }
}

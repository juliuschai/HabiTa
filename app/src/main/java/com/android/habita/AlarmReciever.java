package com.android.habita;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReciever extends BroadcastReceiver {
    public int NOTIFICATION_ID = 0;
    public int PENDINGINTENT_ID = 0;

    public AlarmReciever() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String habitName = "";
        if (extras != null) {
            habitName = extras.getString("name");
        }

        Bundle newExtras = new Bundle();
        newExtras.putString("name", habitName);

        Intent successIntent = new Intent(context, SuccessReceiver.class);
        successIntent.putExtras(newExtras);
        PendingIntent successPendingIntent = PendingIntent.getBroadcast(
                context, PENDINGINTENT_ID + 1, successIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(context, CancelReceiver.class);
        cancelIntent.putExtras(newExtras);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(
                context, PENDINGINTENT_ID + 2, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_name))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText("Habit " + habitName + " reminder!")
//                .setContentIntent(successPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_launcher_foreground, context.getString(R.string.success),
                        successPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, context.getString(R.string.cancel),
                        cancelPendingIntent);
        builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

/*
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String habitName = "";
        if (extras != null)
            habitName = extras.getString("name");

        Bundle newExtras = new Bundle();
        newExtras.putString("name", habitName);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) { throw new RuntimeException("notificationManager is null"); }
        //Create the content intent for the notification, which launches this activity
//        Intent contentIntent = new Intent(context, MainActivity.class);
//        PendingIntent contentPendingIntent = PendingIntent.getActivity(
//                context, PENDINGINTENT_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Prepare success and cancel PendingIntent
        Intent successIntent = new Intent(context, SuccessReceiver.class);
        successIntent.putExtras(newExtras);
        PendingIntent successPendingIntent = PendingIntent.getBroadcast(
                context, PENDINGINTENT_ID + 1, successIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(context, CancelReceiver.class);
        successIntent.putExtras(newExtras);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(
                context, PENDINGINTENT_ID + 2, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stand_up)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText("Habit " + habitName + " reminder!")
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_launcher_foreground, context.getString(R.string.success),
                        successPendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, context.getString(R.string.cancel),
                        cancelPendingIntent);


        //Deliver the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
*/

}

package com.example.controlpanalamal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Second case when notification payload is
        // received.
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
        if(remoteMessage.getNotification().getTitle().equals("BuyShoes")) {
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }else{
            System.out.println("Type = " + remoteMessage.getNotification().getTitle());
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    // Method to display the notifications
    public void showNotification(String title,
                                 String message) {
        System.out.println("title = " + title + "Body = " + message);
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, Notification_recycle.class);
        // Assign channel ID
        String channel_id = MainActivity.Channel_ID;
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.circle_one)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.circle_one);
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}

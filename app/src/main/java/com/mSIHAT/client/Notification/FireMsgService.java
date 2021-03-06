package com.mSIHAT.client.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mSIHAT.client.MainActivity;
import com.mSIHAT.client.R;

import java.util.Date;

public class FireMsgService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
 
        Log.e("Msg", "Message received ["+remoteMessage+"]");
 
        // Create Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                        intent, PendingIntent.FLAG_ONE_SHOT);
 
        NotificationCompat.Builder notificationBuilder = new
                 NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.hcpicon2)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
 
        NotificationManager notificationManager =
                (NotificationManager) 
                getSystemService(Context.NOTIFICATION_SERVICE);

        int m = (int)((new Date().getTime()/ 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notificationBuilder.build());

        Intent intent2 = new Intent();
        intent2.setAction("UpdateLocation");
        intent2.putExtra("DATAPASSED", 3);
        sendBroadcast(intent2);
        Log.e("msg1","sent");
    }
}
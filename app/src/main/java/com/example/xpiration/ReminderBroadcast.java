package com.example.xpiration;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    //****************************** Clase para crear la notificacion **********************************
    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent;
        Intent intentB = new Intent(context,SegundaActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SegundaActivity.class);
        stackBuilder.addNextIntent(intentB);
        pendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"ChannelXpiration")
                .setSmallIcon(R.drawable.ic_apple_alt)
                .setContentTitle("Xpiration")
                .setContentText("Recuerda revisar la lista de cosas a expirar")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());

    }
}
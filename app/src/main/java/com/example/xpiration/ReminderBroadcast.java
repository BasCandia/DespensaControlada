package com.example.xpiration;

import android.app.Notification;
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
        PendingIntent pendingIntent;//Accion que se realizara en el futuro
        Intent intentB = new Intent(context,VistaNotificacionActivity.class);//Intent para llevar a vista de notificacion
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(VistaNotificacionActivity.class);
        stackBuilder.addNextIntent(intentB);//Se le agrega a la notificacion la accion de llevar a la vista
        pendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"ChannelXpiration") //Contenido de la notificacion
                .setSmallIcon(R.drawable.ic_apple_alt)
                .setContentTitle("DespensaControlada")
                .setContentText("Recuerda revisar la lista de Lotes a expirar")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent); //Se le setea el contenido a la notificacion

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());//Se muestra la notificacion

    }
}
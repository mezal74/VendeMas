package com.vendemas.vendemas.utilities.mensajeria;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vendemas.vendemas.R;
import com.vendemas.vendemas.activities.Chat;
import com.vendemas.vendemas.activities.MainLogin;
import com.vendemas.vendemas.utilities.mensajeria.moldes.Notification;

import java.util.List;

public class mFirebaseMesagingService extends FirebaseMessagingService
{
    private static final String TAG = "FMessagingService";

    public mFirebaseMesagingService()
    {

    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage)
    {

        isRunning(this);

        if ( Chat.isRunning )
        {

            Chat.h.post(new Runnable() {
                @Override
                public void run() {
                    Chat.cargarMensaje(remoteMessage.getNotification().getBody(), Chat.RECIBIDO, getApplicationContext());
                }
            });
        }
        else
        {
            handleNow(remoteMessage);
        }


    }

    public boolean isRunning(Context ctx)
    {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> tasks = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo task : tasks)
        {
            Log.e("TASK", task.processName);
            //if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
              //  return true;
        }

        return false;
    }

    private void scheduleJob(RemoteMessage remoteMessage)
    {

    }

    private void handleNow(RemoteMessage remoteMessage)
    {
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Intent intent = new Intent(this, MainLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(0, builder.build());

    }
}

package com.example.h2601864.myjournal.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.h2601864.myjournal.R;
import com.example.h2601864.myjournal.act.ActEdit;

/**
 * Created by H2601864 on 2015/11/12.
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentNotification=new Intent(context, ActEdit.class);
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent=PendingIntent.getActivity(context,0,intentNotification,0);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("To write a few words.")
                .setContentText("A new day, a note of good mood !")
                .setSmallIcon(R.drawable.icon)
                .build();
        /*Notification notification=new Notification();
        notification.flags=Notification.FLAG_AUTO_CANCEL;
        notification.defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
        notification.icon= R.drawable.icon;
        notification.tickerText="To write a few words.";
        notification.setLatestEventInfo(context,"hi,","A new day, a note of good mood !",pIntent);*/
        manager.notify(0,noti);
    }
}

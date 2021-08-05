package com.rsin.alarmly;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.rsin.alarmly.Room.Alarm;
import com.rsin.alarmly.Room.MyDatabase;

import javax.xml.transform.Result;


public class AlaramReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        MyDatabase myDatabase = Room.databaseBuilder(context, MyDatabase.class, "AlarmDB")
                .allowMainThreadQueries()
                .build();
        Bundle args = intent.getBundleExtra("DATA");
        Alarm alarm = (Alarm) args.getSerializable("alarm");
        Toast.makeText(context, alarm.getTitle(), Toast.LENGTH_SHORT).show();
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(alarm.getTitle());
        notificationHelper.getManager().notify(1, nb.build());
        Uri alarmuri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmuri == null) {
            alarmuri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmuri);
        ringtone.play();

    }

}

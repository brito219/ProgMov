package com.example.ToDo.util;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.ToDo.receiver.AlarmReceiver;

public class NotificationUtils {
    public static void schedule(Context ctx, long timeInMillis, long taskId, String title) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("title", title);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, (int)taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
    }

    public static void cancel(Context ctx, long taskId) {
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, (int)taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }
}

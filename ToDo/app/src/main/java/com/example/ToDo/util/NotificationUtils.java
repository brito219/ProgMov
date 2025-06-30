package com.example.ToDo.util;

import android.app.*;
import android.content.Context;
import android.content.Intent;

import com.example.ToDo.entity.Task;
import com.example.ToDo.receiver.AlarmReceiver;

public class NotificationUtils {

    public static void scheduleMultiple(Context context, Task task) {
        long[] offsets = {
                5 * 60 * 60 * 1000,   // 5 horas
                1 * 60 * 60 * 1000,   // 1 hora
                10 * 60 * 1000,       // 10 minutos
                0                     // no horário
        };

        String[] mensagens = {
                "Faltam 5 horas para: " + task.getTitle(),
                "Falta 1 hora para: " + task.getTitle(),
                "Faltam 10 minutos para: " + task.getTitle(),
                "Agora é hora de: " + task.getTitle()
        };

        for (int i = 0; i < offsets.length; i++) {
            long triggerAt = task.getDateTime() - offsets[i];
            if (triggerAt < System.currentTimeMillis()) continue;

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("taskId", task.getId() * 10 + i);
            intent.putExtra("title", task.getTitle());
            intent.putExtra("mensagem", mensagens[i]);

            PendingIntent pi = PendingIntent.getBroadcast(
                    context,
                    (int) (task.getId() * 10 + i),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager am = (AlarmManager)
                    context.getSystemService(Context.ALARM_SERVICE);

            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
        }
    }

    public static void cancel(Context context, long taskId) {
        AlarmManager am = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        for (int i = 0; i < 4; i++) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(
                    context,
                    (int) (taskId * 10 + i),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            am.cancel(pi);
        }
    }
}
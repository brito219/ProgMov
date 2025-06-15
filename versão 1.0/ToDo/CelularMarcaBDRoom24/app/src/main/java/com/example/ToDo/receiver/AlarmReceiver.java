package com.example.ToDo.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import com.example.ToDo.R;
import com.example.ToDo.view.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "todo_channel";

    @Override
    public void onReceive(Context ctx, Intent intent) {
        long taskId = intent.getLongExtra("taskId", -1);
        String title = intent.getStringExtra("title");

        NotificationManager nm = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Cria canal (Android O+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "ToDo Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Canal de notificações do ToDo");
            nm.createNotificationChannel(channel);
        }

        // Intent que será disparada ao clicar na notificação
        Intent main = new Intent(ctx, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                ctx,
                0,
                main,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)   // seu ícone de notificação
                .setContentTitle("Lembrete: " + title)
                .setContentText("É hora de concluir sua tarefa")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pi)
                .setAutoCancel(true);

        nm.notify((int) taskId, builder.build());
    }
}

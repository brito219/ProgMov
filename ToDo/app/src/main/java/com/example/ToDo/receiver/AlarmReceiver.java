package com.example.ToDo.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.ToDo.R;
import com.example.ToDo.view.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "todo_channel";

    @Override
    public void onReceive(Context ctx, Intent intent) {
        long taskId = intent.getLongExtra("taskId", -1);
        String title = intent.getStringExtra("title");
        String mensagem = intent.getStringExtra("mensagem");

        NotificationManager nm = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "ToDo Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Canal de notificações do ToDo");
            nm.createNotificationChannel(channel);
        }

        Intent main = new Intent(ctx, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                ctx,
                0,
                main,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Lembrete: " + title)
                .setContentText(mensagem)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pi)
                .setAutoCancel(true);

        nm.notify((int) taskId, builder.build());
    }
}
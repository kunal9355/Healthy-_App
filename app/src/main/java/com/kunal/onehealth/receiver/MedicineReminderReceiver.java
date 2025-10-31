package com.kunal.onehealth.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.kunal.onehealth.R;
import com.kunal.onehealth.ui.medicine.MedicineActivity;

public class MedicineReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicineName = intent.getStringExtra("medicineName");

        // ✅ 1. Play custom alarm sound (alarm.mp3 in res/raw)
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.start();

        // ✅ 2. Notification channel ID and name
        String CHANNEL_ID = "med_channel";
        String CHANNEL_NAME = "Medicine Reminders";

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // ✅ 3. Create notification channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminders for your scheduled medicines");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 400, 400, 400});
            manager.createNotificationChannel(channel);
        }

        // ✅ 4. Open MedicineActivity when clicked
        Intent openIntent = new Intent(context, MedicineActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // ✅ 5. Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pill)  // use a 24dp vector icon
                .setContentTitle("💊 Medicine Reminder")
                .setContentText("Time to take: " + medicineName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0, 400, 400, 400})
                .setSound(null); // sound handled by MediaPlayer

        // ✅ 6. Show notification
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}

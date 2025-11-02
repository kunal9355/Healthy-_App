package com.kunal.onehealth.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.kunal.onehealth.R;
import com.kunal.onehealth.ui.medicine.MedicineActivity;

public class MedicineReminderReceiver extends BroadcastReceiver {

    // 🔸 Keep global reference to stop sound later
    private static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicineName = intent.getStringExtra("medicineName");

        // ---------------------------
        // 1️⃣ Delete old channel to refresh sound
        // ---------------------------
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel("med_channel");
        }

        // ---------------------------
        // 2️⃣ Play custom MP3 alarm sound
        // ---------------------------
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm); // alarm.mp3 in res/raw/
        mediaPlayer.setLooping(true); // repeat until stopped
        mediaPlayer.start();

        // ---------------------------
        // 3️⃣ Create Notification Channel (Android 8+)
        // ---------------------------
        String CHANNEL_ID = "med_channel";
        String CHANNEL_NAME = "Medicine Reminders";

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminders for your scheduled medicines");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setSound(soundUri, attributes);
            manager.createNotificationChannel(channel);
        }

        // ---------------------------
        // 4️⃣ When user taps notification → open MedicineActivity
        // ---------------------------
        Intent openIntent = new Intent(context, MedicineActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // ---------------------------
        // 5️⃣ Build and show notification
        // ---------------------------
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pill) // use 24dp vector icon
                .setContentTitle("💊 Medicine Reminder")
                .setContentText("Time to take: " + medicineName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    // ---------------------------
    // 6️⃣ Public method to stop sound when user opens app
    // ---------------------------
    public static void stopAlarmSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

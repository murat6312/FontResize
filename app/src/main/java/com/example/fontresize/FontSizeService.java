package com.example.fontresize;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class FontSizeService extends Service {
    private static final String CHANNEL_ID = "FontResizeServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    public static final String ACTION_INCREASE_FONT = "com.example.fontresize.INCREASE_FONT";
    public static final String ACTION_DECREASE_FONT = "com.example.fontresize.DECREASE_FONT";
    public static final String ACTION_RESET_FONT = "com.example.fontresize.RESET_FONT";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, com.example.fontresize.MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent increaseIntent = new Intent(this, com.example.fontresize.FontSizeReceiver.class);
        increaseIntent.setAction(ACTION_INCREASE_FONT);
        PendingIntent increasePendingIntent = PendingIntent.getBroadcast(this, 0, increaseIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent decreaseIntent = new Intent(this, com.example.fontresize.FontSizeReceiver.class);
        decreaseIntent.setAction(ACTION_DECREASE_FONT);
        PendingIntent decreasePendingIntent = PendingIntent.getBroadcast(this, 1, decreaseIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent resetIntent = new Intent(this, com.example.fontresize.FontSizeReceiver.class);
        resetIntent.setAction(ACTION_RESET_FONT);
        PendingIntent resetPendingIntent = PendingIntent.getBroadcast(this, 2, resetIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher, "Increase Font", increasePendingIntent)  // replace with appropriate drawable
                .addAction(R.mipmap.ic_launcher, "Decrease Font", decreasePendingIntent)  // replace with appropriate drawable
                .addAction(R.mipmap.ic_launcher, "Reset Font", resetPendingIntent)  // replace with appropriate drawable
                .build();

        startForeground(NOTIFICATION_ID, notification);

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

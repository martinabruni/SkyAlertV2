package com.project.skyalert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.project.skyalert.activities.Screen2Activity;

/**
 * The NotificationHelper class provides utility methods for creating and managing notifications
 * for MQTT messages. It ensures compatibility with Android notification channels for API 26+.
 */
public class NotificationHelper {

    private static final String CHANNEL_ID = "mqtt_notifications_channel"; // ID for the notification channel
    private final Context context; // Application context for managing notifications

    /**
     * Constructs a NotificationHelper instance and creates the notification channel.
     *
     * @param context The application context used to create and manage notifications.
     */
    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    /**
     * Creates a notification channel for devices running API 26 (Android O) and above.
     * The channel is used for displaying MQTT message notifications.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MQTT Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel for MQTT message notifications");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Sends a notification with the specified title and message.
     *
     * @param title   The title of the notification.
     * @param message The content/message of the notification.
     */
    public void sendNotification(String title, String message) {
        Intent intent = new Intent(context, Screen2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Resumes if already running

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // Ensure flags are compatible with modern Android
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}

package com.project.skyalert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MqttHandler {
    private static final String CHANNEL_ID = "mqtt_notifications_channel";
    private MqttClient client;
    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();
    private Context context;

    public interface MessageListener {
        void onMessageReceived(String topic, String message);
    }

    public void setContext(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public void addObserver(MessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeObserver(MessageListener listener) {
        listeners.remove(listener);
    }

    protected void notifyObservers(String topic, String message) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(topic, message);
        }
        sendNotification("Sky Alert", "Topic: " + topic + "\nMessage: " + message);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "MQTT Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for MQTT message notifications");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    public void connect(String brokerUrl, String clientId, Context context) {
        this.context = context;
        createNotificationChannel();
        try {
            client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
            MqttConnectionOptions options = new MqttConnectionOptions();
            options.setCleanStart(true);
            options.setSessionExpiryInterval(0L);

            client.setCallback(new MqttCallback() {
                @Override
                public void disconnected(MqttDisconnectResponse disconnectResponse) {
                    // Handle disconnection logic here
                }

                @Override
                public void mqttErrorOccurred(MqttException exception) {
                    exception.printStackTrace();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String payload = new String(message.getPayload());
                    notifyObservers(topic, payload);
                }

                @Override
                public void deliveryComplete(IMqttToken token) {
                    // Optional: Handle message delivery completion
                }

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    // Optional: Handle logic when connection is complete
                }

                @Override
                public void authPacketArrived(int reasonCode, MqttProperties properties) {
                    // Optional: Handle auth packet arrival
                }
            });

            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            MqttSubscription subscription = new MqttSubscription(topic);
            client.subscribe(new MqttSubscription[]{subscription});
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

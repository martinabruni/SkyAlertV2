package com.project.skyalert;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.UUID;

public class MqttHandlerFacade extends MqttHandler {
    private static MqttHandlerFacade instance;
    private String clientId;

    // Private constructor that passes the NotificationHelper to the superclass
    private MqttHandlerFacade(NotificationHelper notificationHelper) {
        super(notificationHelper);
        clientId = "android-" + UUID.randomUUID().toString();
    }

    // Public method to provide access to the single instance with the dependency passed
    public static MqttHandlerFacade getInstance(NotificationHelper notificationHelper) {
        if (instance == null) {
            synchronized (MqttHandlerFacade.class) {
                if (instance == null) {
                    instance = new MqttHandlerFacade(notificationHelper);
                }
            }
        }
        return instance;
    }

    public void validateConnection(TextView component, String ipAddress, Context context){
        if (!isValidIpAddress(ipAddress)) {
            throw new RuntimeException("IP address not valid");
        } else {
            try {
                String brokerUrl = "tcp://" + ipAddress + ":1883";
                connectAndSubscribe(brokerUrl, clientId, context);
            } catch (Exception e){
                throw new RuntimeException("Connection to the broker failed");
            }
        }
    }
    private void connectAndSubscribe(String brokerUrl, String clientId, Context context) {
        connect(brokerUrl, clientId, context);
        subscribe("nina");
    }

    public void connectAndSubscribe(String brokerUrl, String clientId, Context context, String topic) {
        connect(brokerUrl, clientId, context);
        subscribe(topic);
    }

    private boolean isValidIpAddress(String ipAddress) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ipAddress != null && ipAddress.matches(ipPattern);
    }
}

package com.project.skyalert;

import android.content.Context;
import android.widget.TextView;

import com.project.skyalert.ui.layouts.TopicItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MqttHandlerFacade extends MqttHandler {
    private static MqttHandlerFacade instance;
    private final List<TopicItem> topics;
    private final String clientId;

    // Private constructor that passes the NotificationHelper to the superclass
    private MqttHandlerFacade(NotificationHelper notificationHelper) {
        super(notificationHelper);
        clientId = "android-" + UUID.randomUUID().toString();
        topics = new ArrayList<>();
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

    public static MqttHandlerFacade getInstance() {
        return instance;
    }

    public List<TopicItem> getTopics() {
        return topics;
    }

    public void validateAndConnect(TextView component, String ipAddress, String port, Context context) {
        if (!isValidIpAddress(ipAddress)) {
            throw new RuntimeException("IP address not valid");
        } else {
            try {
                String brokerUrl = "tcp://" + ipAddress + ":" + port;
                connect(brokerUrl, clientId, context);
            } catch (Exception e) {
                throw new RuntimeException("Connection to the broker failed");
            }
        }
    }

    public void subscribe(String topic, TopicItem topicItem) {
        super.subscribe(topic);
        this.topics.add(topicItem);
    }

    private boolean isValidIpAddress(String ipAddress) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ipAddress != null && ipAddress.matches(ipPattern);
    }

}

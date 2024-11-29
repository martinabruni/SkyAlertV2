package com.project.skyalert;

import android.content.Context;
import android.widget.TextView;

import com.project.skyalert.ui.layouts.TopicItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The MqttHandlerFacade class is a singleton that extends {@link MqttHandler} to provide
 * additional functionality for managing MQTT topics and connections.
 */
public class MqttHandlerFacade extends MqttHandler {

    private static MqttHandlerFacade instance; // Singleton instance
    private List<TopicItem> topics; // List of subscribed topics
    private final String clientId; // Unique client identifier

    /**
     * Private constructor for initializing the MqttHandlerFacade.
     *
     * @param notificationHelper The NotificationHelper instance used for managing notifications.
     */
    private MqttHandlerFacade(NotificationHelper notificationHelper) {
        super(notificationHelper);
        clientId = "android-" + UUID.randomUUID().toString();
        topics = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of MqttHandlerFacade, initializing it if necessary.
     *
     * @param notificationHelper The NotificationHelper instance used to initialize the facade.
     * @return The singleton instance of MqttHandlerFacade.
     */
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

    /**
     * Returns the singleton instance of MqttHandlerFacade if it has already been initialized.
     *
     * @return The singleton instance of MqttHandlerFacade.
     */
    public static MqttHandlerFacade getInstance() {
        return instance;
    }

    /**
     * Retrieves the list of topics currently subscribed to.
     *
     * @return A list of {@link TopicItem} objects representing the subscribed topics.
     */
    public List<TopicItem> getTopics() {
        return topics;
    }

    /**
     * Updates the list of subscribed topics.
     *
     * @param l The new list of {@link TopicItem} objects.
     */
    public void setTopics(List<TopicItem> l) {
        this.topics = l;
    }

    /**
     * Validates the provided IP address and port, then attempts to connect to the MQTT broker.
     *
     * @param component The UI component to update with connection status messages.
     * @param ipAddress The IP address of the MQTT broker.
     * @param port      The port of the MQTT broker.
     * @param context   The context for managing connection lifecycle.
     * @throws RuntimeException If the IP address is invalid or the connection fails.
     */
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

    /**
     * Subscribes to a given topic and adds it to the list of subscribed topics.
     *
     * @param topic     The MQTT topic to subscribe to.
     * @param topicItem A {@link TopicItem} representing the subscribed topic.
     */
    public void subscribe(String topic, TopicItem topicItem) {
        super.subscribe(topic);
        this.topics.add(topicItem);
    }

    /**
     * Validates the provided IP address.
     *
     * @param ipAddress The IP address to validate.
     * @return {@code true} if the IP address is valid, {@code false} otherwise.
     */
    private boolean isValidIpAddress(String ipAddress) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ipAddress != null && ipAddress.matches(ipPattern);
    }
}

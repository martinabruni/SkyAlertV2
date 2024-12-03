package com.project.skyalert;

import com.project.skyalert.ui.layouts.TopicItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MqttHandlerFacade is a singleton class that extends the functionality of {@link MqttHandler},
 * providing additional utilities for managing MQTT topics and validating connections.
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
     * Validates an IP address and port, then establishes a connection to the MQTT broker.
     *
     * @param ipAddress IP address of the broker.
     * @param port      Port number of the broker.
     * @throws RuntimeException If validation fails or connection cannot be established.
     */

    public void validateAndConnect(String ipAddress, String port) {
        if (!isValidIpAddress(ipAddress) || !isValidPort(port)) {
            throw new RuntimeException("IP address or port not valid");
        } else {
            try {
                String brokerUrl = "tcp://" + ipAddress + ":" + port;
                connect(brokerUrl, clientId);
            } catch (Exception e) {
                throw new RuntimeException("Connection to the broker failed");
            }
        }
    }

    /**
     * Subscribes to the given MQTT topic and updates the topic list.
     *
     * @param topic     The topic to subscribe to.
     * @param topicItem The UI representation of the topic.
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

    /**
     * Validates the provided port number.
     *
     * @param port The port number to validate.
     * @return {@code true} if the port number is valid, {@code false} otherwise.
     */
    private boolean isValidPort(String port) {
        String portPattern = "^([0-9]{1,5})$";
        if (port != null && port.matches(portPattern)) {
            int portNumber = Integer.parseInt(port);
            return portNumber >= 0 && portNumber <= 65535;
        }
        return false;
    }

    @Override
    public void disconnect() {
        super.disconnect();
        instance = null;
    }
}

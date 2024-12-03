package com.project.skyalert;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The MqttHandler class provides core functionality for connecting to an MQTT broker,
 * managing subscriptions, handling incoming messages, and notifying observers about received data.
 */

public class MqttHandler {

    private MqttClient client; // The MQTT client instance
    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>(); // List of observers
    private final NotificationHelper notificationHelper; // Helper for sending notifications

    /**
     * Interface for observing MQTT message events.
     */
    public interface MessageListener {

        /**
         * Called when a new MQTT message is received.
         *
         * @param topic   The topic of the received message.
         * @param message The message payload.
         * @param isError Indicates whether the received message represents an error (true if it does, false otherwise).
         */
        void onMessageReceived(String topic, String message, Boolean isError);
    }


    /**
     * Constructor for MqttHandler.
     *
     * @param notificationHelper A helper for managing notifications.
     */
    public MqttHandler(NotificationHelper notificationHelper) {
        this.notificationHelper = notificationHelper;
    }

    /**
     * Adds a listener to observe MQTT message events.
     *
     * @param listener The listener to be added to the observer list.
     */

    public void addObserver(MessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener from observing MQTT message events.
     *
     * @param listener The listener to be removed from the observer list.
     */

    public void removeObserver(MessageListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all observers of a received message.
     *
     * @param topic   The topic of the message.
     * @param message The message payload.
     * @param isError Indicates if the message is an error.
     */
    protected void notifyObservers(String topic, String message, Boolean isError) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(topic, message, isError);
        }
        if (isError) {
            notificationHelper.sendNotification("Sky Alert", message);
        }
    }

    /**
     * Connects to the specified MQTT broker with the provided client ID and context.
     *
     * @param brokerUrl The URL of the MQTT broker.
     * @param clientId  The unique identifier for the MQTT client.
     * @throws MqttException If there is an issue during the connection process.
     */

    public void connect(String brokerUrl, String clientId) throws MqttException {
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
                    try {
                        boolean isError = hasErrorList(payload);

                        if (isError) {
                            String formattedMessage = handleIncomingMessage(payload);
                            notifyObservers(topic, formattedMessage, true);
                        } else {
                            notifyObservers(topic, payload, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void deliveryComplete(IMqttToken token) {
                }

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                }

                @Override
                public void authPacketArrived(int reasonCode, MqttProperties properties) {
                }
            });

            client.connect(options);
        } catch (MqttException e) {
            throw new MqttException(e);
        }
    }

    /**
     * Subscribes the client to the specified MQTT topic.
     *
     * @param topic The topic to subscribe to.
     */

    public void subscribe(String topic) throws MqttException {
        try {
            MqttSubscription subscription = new MqttSubscription(topic);
            client.subscribe(new MqttSubscription[]{subscription});
        } catch (MqttException e) {
            throw new MqttException(e);
        }
    }

    /**
     * Disconnects the client from the MQTT broker.
     */

    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the payload contains an error list.
     *
     * @param payload The message payload.
     * @return True if the payload contains an error list, false otherwise.
     */
    public boolean hasErrorList(String payload) {
        try {
            JSONObject message = new JSONObject(payload);
            return message.has("error_list") && message.getJSONArray("error_list").length() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Handles incoming MQTT messages and formats error messages if present.
     *
     * @param payload The message payload.
     * @return A formatted string representation of the message.
     */
    public String handleIncomingMessage(String payload) {
        try {
            JSONObject message = new JSONObject(payload);
            String name = message.optString("name", "N/A");
            String description = message.optString("description", "N/A");
            String dateLocal = message.optString("date_local", "");

            String formattedDate = "N/A";
            if (!dateLocal.isEmpty()) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                Date date = inputFormat.parse(dateLocal);
                formattedDate = outputFormat.format(date);
            }

            boolean isError = hasErrorList(payload);

            StringBuilder formattedPayload = new StringBuilder();

            if (isError) {
                String reason = "N/A";
                if (message.has("error_list")) {
                    JSONArray errorList = message.getJSONArray("error_list");
                    if (errorList.length() > 0) {
                        reason = errorList.getJSONObject(0).optString("Reason", "No reason provided");
                    }
                }
                formattedPayload.append("ERROR\n").append("Name: ").append(name).append("\n").append("Description: ").append(description).append("\n").append("Time: ").append(formattedDate).append("\n").append("Reason: ").append(reason);
            }
            return formattedPayload.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid message format.";
        }
    }
}

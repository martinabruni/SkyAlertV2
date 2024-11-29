package com.project.skyalert;

import android.content.Context;

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
 * The MqttHandler class provides functionality for connecting to an MQTT broker,
 * subscribing to topics, and processing incoming messages.
 */
public class MqttHandler {

    private MqttClient client; // The MQTT client instance
    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>(); // List of observers
    private final NotificationHelper notificationHelper; // Helper for sending notifications
    private JSONObject lastMessage; // Stores the last received message

    /**
     * Interface for observing MQTT message events.
     */
    public interface MessageListener {
        /**
         * Called when a new MQTT message is received.
         *
         * @param topic   The topic of the received message.
         * @param message The message payload.
         */
        void onMessageReceived(String topic, String message);
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
     * Adds a new observer to listen for incoming MQTT messages.
     *
     * @param listener The listener to add.
     */
    public void addObserver(MessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes an existing observer from the list.
     *
     * @param listener The listener to remove.
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
            listener.onMessageReceived(topic, message);
        }
        if (isError) {
            notificationHelper.sendNotification("Sky Alert", message);
        }
    }

    /**
     * Connects to an MQTT broker.
     *
     * @param brokerUrl The URL of the MQTT broker.
     * @param clientId  A unique client identifier.
     * @param context   The context for managing lifecycle events.
     */
    public void connect(String brokerUrl, String clientId, Context context) {
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
                            String formattedMessage = handleIncomingMessage(topic, payload);
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
            e.printStackTrace();
        }
    }

    /**
     * Subscribes to an MQTT topic.
     *
     * @param topic The topic to subscribe to.
     */
    public void subscribe(String topic) {
        try {
            MqttSubscription subscription = new MqttSubscription(topic);
            client.subscribe(new MqttSubscription[]{subscription});
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects from the MQTT broker.
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
     * @param topic   The topic of the message.
     * @param payload The message payload.
     * @return A formatted string representation of the message.
     */
    public String handleIncomingMessage(String topic, String payload) {
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

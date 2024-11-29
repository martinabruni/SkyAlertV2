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

public class MqttHandler {
    private MqttClient client;
    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();
    private final NotificationHelper notificationHelper;
    private JSONObject lastMessage;

    public interface MessageListener {
        void onMessageReceived(String topic, String message);
    }

    public MqttHandler(NotificationHelper notificationHelper) {
        this.notificationHelper = notificationHelper;
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
        notificationHelper.sendNotification("Sky Alert", message);
    }

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
                            notifyObservers(topic, formattedMessage);
                        } else {

                            notifyObservers(topic, payload);
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

    public boolean hasErrorList(String payload) {
        try {
            JSONObject message = new JSONObject(payload);
            // Verifica se il messaggio contiene "error_list" e se ha elementi
            return message.has("error_list") && message.getJSONArray("error_list").length() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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
                formattedPayload.append("ERROR\n")
                        .append("Name: ").append(name).append("\n")
                        .append("Description: ").append(description).append("\n")
                        .append("Time: ").append(formattedDate).append("\n")
                        .append("Reason: ").append(reason);
            }
            return formattedPayload.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid message format.";
        }
    }
}

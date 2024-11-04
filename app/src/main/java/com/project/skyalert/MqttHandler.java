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

import java.util.ArrayList;
import java.util.List;

public class MqttHandler {
    private MqttClient client;
    private List<MessageListener> listeners = new ArrayList<>();

    public interface MessageListener {
        void onMessageReceived(String topic, String message);
    }

    public void addObserver(MessageListener listener) {
        listeners.add(listener);
    }

    public void removeObserver(MessageListener listener) {
        listeners.remove(listener);
    }

    protected void notifyObservers(String topic, String message) {
        for (MessageListener listener : listeners) {
            listener.onMessageReceived(topic, message);
        }
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

                }

                @Override
                public void mqttErrorOccurred(MqttException exception) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    notifyObservers(topic, new String(message.getPayload()));
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
}

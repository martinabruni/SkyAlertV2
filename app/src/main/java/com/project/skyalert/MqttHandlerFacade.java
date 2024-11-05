package com.project.skyalert;

import android.content.Context;

public class MqttHandlerFacade extends MqttHandler {
    private static final String DEFAULT_TOPIC = "nina";
    private static MqttHandlerFacade instance;

    // Private constructor to prevent instantiation from outside
    private MqttHandlerFacade() {}

    // Public method to provide access to the single instance
    public static MqttHandlerFacade getInstance() {
        if (instance == null) {
            synchronized (MqttHandlerFacade.class) {
                if (instance == null) {
                    instance = new MqttHandlerFacade();
                }
            }
        }
        return instance;
    }

    public void connectAndSubscribe(String brokerUrl, String clientId, Context context) {
        connect(brokerUrl, clientId, context);
        subscribe(DEFAULT_TOPIC);
    }

    public void connectAndSubscribe(String brokerUrl, String clientId, Context context, String topic) {
        connect(brokerUrl, clientId, context);
        subscribe(topic);
    }

    public void disconnect() {
        super.disconnect();
    }
}

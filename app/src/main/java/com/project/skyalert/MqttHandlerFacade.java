package com.project.skyalert;

public class MqttHandlerFacade extends MqttHandler {
    public void connect(String brokerUrl, String clientId) {
        super.connect(brokerUrl, clientId);
        System.out.println("Connected to broker: " + brokerUrl);
    }

    public void subscribe(String topic) {
        super.subscribe(topic);
        System.out.println("Subscribed to topic: " + topic);
    }
}

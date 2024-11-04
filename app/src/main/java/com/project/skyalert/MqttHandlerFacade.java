package com.project.skyalert;

import android.widget.TextView;

public class MqttHandlerFacade extends MqttHandler {
    public void connect(String brokerUrl, String clientId) {
        super.connect(brokerUrl, clientId);
        super.subscribe("nina");
        //add to observer
        //call the ui manager to handle ui update
    }
}

package com.project.skyalert;

import android.content.Context;
import android.widget.TextView;

public class MqttHandlerFacade extends MqttHandler {
    public void connect(String brokerUrl, String clientId, Context context) {
        super.connect(brokerUrl, clientId, context);
        super.subscribe("nina");
    }
}

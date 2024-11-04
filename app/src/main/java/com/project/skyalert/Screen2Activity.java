package com.project.skyalert;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Screen2Activity extends AppCompatActivity {
    private TextView feedbackTextbox;
    private TextView alertsTextbox;
    private MqttHandler mqttHandlerFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        // Initialize UI components
        feedbackTextbox = findViewById(R.id.feedbackTextView);
        alertsTextbox = findViewById(R.id.alertsTextView);

        // Get the IP_ADDRESS passed from MainActivity
        String ipAddress = getIntent().getStringExtra("IP_ADDRESS");
        String brokerUrl = "tcp://" + ipAddress + ":1883";

        feedbackTextbox.setText("Connecting to broker " + brokerUrl);

        // Use MqttHandlerFacade to manage MQTT connections
        mqttHandlerFacade = new MqttHandlerFacade();
        mqttHandlerFacade.connect(brokerUrl, "ANDROID_CLIENT");

        mqttHandlerFacade.subscribe("nina");

        mqttHandlerFacade.addObserver((topic, message) -> {
            runOnUiThread(() -> alertsTextbox.setText("Topic: " + topic + "\nMessage: " + message));
        });

        feedbackTextbox.setText("Connected");
    }
}

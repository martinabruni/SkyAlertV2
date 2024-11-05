package com.project.skyalert;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Screen2Activity extends AppCompatActivity {
    private LinearLayout alertsScrollView;
    private MqttHandlerFacade mqttHandlerFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        // Initialize UI components
        alertsScrollView = findViewById(R.id.layout);

        // Use MqttHandlerFacade to manage MQTT connections
        mqttHandlerFacade = MqttHandlerFacade.getInstance();
        mqttHandlerFacade.setContext(this); // Set context for notifications
        mqttHandlerFacade.addObserver((topic, message) -> {
            TextView newAlert = UIManager.newTextView(this, "Topic: " + topic + "\nMessage: " + message);
            runOnUiThread(() -> alertsScrollView.addView(newAlert, 0));
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttHandlerFacade != null) {
            mqttHandlerFacade.disconnect();
        }
    }
}

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

        alertsScrollView = findViewById(R.id.layout);
        NotificationHelper notificationHelper = new NotificationHelper(this);
        mqttHandlerFacade = MqttHandlerFacade.getInstance(notificationHelper); // Pass the dependency

        // Add an observer for handling incoming MQTT messages
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

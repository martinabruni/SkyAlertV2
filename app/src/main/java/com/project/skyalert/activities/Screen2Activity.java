package com.project.skyalert.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.MqttHandlerFacade;
import com.project.skyalert.NotificationHelper;
import com.project.skyalert.R;
import com.project.skyalert.UIManager;

import java.util.List;

public class Screen2Activity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout alertsScrollView;
    private ImageButton clearButton;
    private Button disconnectButton;
    private ImageButton subscribePage;
    private MqttHandlerFacade mqttHandlerFacade;
    private final AppCompatActivity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        alertsScrollView = findViewById(R.id.layout);
        clearButton = findViewById(R.id.clearButton);
        disconnectButton = findViewById(R.id.disconnectButton);
        subscribePage = findViewById(R.id.subscribePage);

        clearButton.setOnClickListener(this);
        disconnectButton.setOnClickListener(this);
        subscribePage.setOnClickListener(this);

        NotificationHelper notificationHelper = new NotificationHelper(this);
        mqttHandlerFacade = MqttHandlerFacade.getInstance(notificationHelper); // Pass the dependency

        // Add an observer for handling incoming MQTT messages
        mqttHandlerFacade.addObserver((topic, message) -> {
            runOnUiThread(() ->
                    UIManager.displayElements(List.of("Topic: " + topic + "\nMessage: " + message), alertsScrollView, this));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttHandlerFacade != null) {
            mqttHandlerFacade.disconnect();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.disconnectButton) {
            finish();
        } else if (id == R.id.clearButton) {
            alertsScrollView.removeAllViewsInLayout();
        } else if (id == R.id.subscribePage) {
            UIManager.loadNextActivity(currentActivity, SubscribeActivity.class);
        }
    }
}

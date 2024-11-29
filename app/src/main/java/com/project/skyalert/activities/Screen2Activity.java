package com.project.skyalert.activities;

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
import com.project.skyalert.ui.UIManager;
import com.project.skyalert.ui.ViewBinder;
import com.project.skyalert.ui.layouts.AlertItem;

import java.util.List;

public class Screen2Activity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout alertsScrollView;
    private MqttHandlerFacade mqttHandlerFacade;
    private final AppCompatActivity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        alertsScrollView = findViewById(R.id.layout);
        ImageButton clearButton = findViewById(R.id.clearButton);
        Button disconnectButton = findViewById(R.id.disconnectButton);
        ImageButton subscribePage = findViewById(R.id.subscribePage);

        clearButton.setOnClickListener(this);
        disconnectButton.setOnClickListener(this);
        subscribePage.setOnClickListener(this);

        NotificationHelper notificationHelper = new NotificationHelper(this);
        mqttHandlerFacade = MqttHandlerFacade.getInstance(notificationHelper); // Pass the dependency

        // Add an observer for handling incoming MQTT messages
        mqttHandlerFacade.addObserver((topic, message) -> {
            ViewBinder<AlertItem> alertBinder = (view, elementData) -> {
                TextView dataTextView = view.findViewById(R.id.dataTextView);
                TextView messageTextView = view.findViewById(R.id.messageTextView);
                TextView topicTextView = view.findViewById(R.id.topicTextView);

                dataTextView.setText(elementData.getData());
                messageTextView.setText(elementData.getMessage());
                topicTextView.setText(elementData.getTopic());
            };
            AlertItem newAlert = new AlertItem("", message, topic);

            runOnUiThread(() -> UIManager.displayElements(List.of(newAlert), alertsScrollView, this, R.layout.alert_item, alertBinder));
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

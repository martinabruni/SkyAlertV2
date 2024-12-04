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

import java.util.ArrayList;
import java.util.List;

/**
 * The Screen2Activity class manages the alerts displayed from MQTT messages
 * and provides options for clearing alerts, navigating to subscription pages, and disconnecting.
 */
public class Screen2Activity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout alertsScrollView; // Layout for displaying alerts
    private MqttHandlerFacade mqttHandlerFacade; // MQTT handler for managing broker connections

    /**
     * Initializes the activity and sets up the UI components and MQTT observers.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        // Initialize UI components
        alertsScrollView = findViewById(R.id.layout);
        ImageButton clearButton = findViewById(R.id.clearButton);
        Button disconnectButton = findViewById(R.id.disconnectButton);
        ImageButton subscribePage = findViewById(R.id.subscribePage);
        ImageButton settingsButton = findViewById(R.id.settingsIcon);
        ImageButton filtersButton = findViewById(R.id.filterIcon);

        // Set click listeners for buttons
        clearButton.setOnClickListener(this);
        disconnectButton.setOnClickListener(this);
        subscribePage.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        filtersButton.setOnClickListener(this);

        // Initialize MQTT handler
        NotificationHelper notificationHelper = new NotificationHelper(this);
        mqttHandlerFacade = MqttHandlerFacade.getInstance(notificationHelper);

        // Add an observer to handle incoming MQTT messages
        mqttHandlerFacade.addObserver((topic, message, isError) -> {
            // Define how the AlertItem data is bound to views
            ViewBinder<AlertItem> alertBinder = (view, elementData) -> {
                TextView dataTextView = view.findViewById(R.id.dataTextView);
                TextView messageTextView = view.findViewById(R.id.messageTextView);
                TextView topicTextView = view.findViewById(R.id.topicTextView);

                dataTextView.setText(elementData.getData());
                messageTextView.setText(elementData.getMessage());
                topicTextView.setText(elementData.getTopic());
            };

            // Create a new AlertItem for the received message
            AlertItem newAlert = new AlertItem("", message, topic);
            mqttHandlerFacade.getAlerts().add(newAlert);
            if (isError)
                // Update the UI on the main thread
                runOnUiThread(() -> UIManager.displayElements(List.of(newAlert), alertsScrollView, this, R.layout.alert_item, alertBinder));
            else
                runOnUiThread(() -> UIManager.displayElements(List.of(newAlert), alertsScrollView, this, R.layout.orange_alert_item, alertBinder));
        });
    }

    /**
     * Disconnects the MQTT handler and clears topics when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttHandlerFacade != null) {
            mqttHandlerFacade.disconnect();
            mqttHandlerFacade.setTopics(new ArrayList<>());
            mqttHandlerFacade.setAlerts(new ArrayList<>());
        }
    }

    /**
     * Handles button click events.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.disconnectButton) {
            // Disconnect and close the activity
            finish();
        } else if (id == R.id.clearButton) {
            // Clear all alerts
            alertsScrollView.removeAllViewsInLayout();
        } else if (id == R.id.subscribePage) {
            // Navigate to the subscription activity
            UIManager.loadNextActivity(this, SubscribeActivity.class);
        } else if (id == R.id.settingsIcon) {
            //Navigate to the settings activity
            UIManager.loadNextActivity(this, SettingsActivity.class);
        } else if (id == R.id.filterIcon) {
            //Navigate to the filters activity
            UIManager.loadNextActivity(this, FiltersActivity.class);
        }
    }
}

package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.MqttHandlerFacade;
import com.project.skyalert.R;
import com.project.skyalert.ui.UIManager;

/**
 * The SettingsActivity class provides the settings screen for the application.
 * It allows users to navigate to the "About App" section.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Called when the activity is first created.
     * Initializes the UI components and sets up click listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI components
        ImageButton changeBrokerButton = findViewById(R.id.changeBrokerButton);
        ImageButton addTopicButton = findViewById(R.id.addTopicButton);
        ImageButton notificationButton = findViewById(R.id.notificationButton);
        ImageButton aboutAppButton = findViewById(R.id.aboutAppButton);
        Button exitButton = findViewById(R.id.exitButton);

        // Set click listeners for buttons
        changeBrokerButton.setOnClickListener(this);
        addTopicButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        aboutAppButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    /**
     * Handles click events for the UI components.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.changeBrokerButton) {
            //Disconnect from the broker
            MqttHandlerFacade.getInstance().disconnect();
            //Navigate to the "Screen1" activity and clear the stack of activities
            UIManager.loadNextActivityAndClearStack(this, Screen1Activity.class);
        } else if (id == R.id.addTopicButton) {
            // Navigate to the "Subscribe" activity and destroy the currentActivity
            UIManager.loadNextActivityAndClear(this, SubscribeActivity.class);
        } else if (id == R.id.notificationButton) {
            // Navigate to the "Notification" activity
            UIManager.loadNextActivity(this, NotificationActivity.class);
        } else if (id == R.id.aboutAppButton) {
            // Navigate to the "About App" activity
            UIManager.loadNextActivity(this, AboutAppActivity.class);
        } else if (id == R.id.exitButton) {
            // Navigate to the "Screen2" activity and destroy the currentActivity
            UIManager.loadNextActivityAndClear(this, Screen2Activity.class);
        }
    }
}

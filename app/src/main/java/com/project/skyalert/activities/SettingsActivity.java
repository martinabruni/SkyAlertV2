package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize and set click listener for the "About App" button
        ImageButton aboutAppButton = findViewById(R.id.aboutAppButton);
        aboutAppButton.setOnClickListener(this);
    }

    /**
     * Handles click events for the UI components.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.aboutAppButton) {
            // Navigate to the "About App" activity
            UIManager.loadNextActivity(this, AboutAppActivity.class);
        }
    }
}

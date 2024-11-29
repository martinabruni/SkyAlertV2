package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;
import com.project.skyalert.ui.UIManager;

/**
 * The MainActivity class serves as the entry point for the SkyAlert app.
 * It initializes the main UI and handles button interactions.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable Edge-to-Edge UI mode
        setContentView(R.layout.activity_main);

        // Set up the "Connect" button
        Button button = findViewById(R.id.connectButton);
        button.setOnClickListener(this);
    }

    /**
     * Handles button click events.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        // Load the next activity
        UIManager.loadNextActivity(this, Screen1Activity.class);
    }
}

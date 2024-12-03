package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;

/**
 * The FiltersActivity class provides a screen for users to configure filter settings
 * for alerts and notifications within the application.
 */
public class FiltersActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Called when the activity is first created.
     * Sets up the layout for the filters screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        // Initialize UI components
        Button exitButton = findViewById(R.id.exitFilters);

        // Set click listeners for buttons
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.exitFilters) {
            // Go back to previous screen
            finish();
        }
    }
}

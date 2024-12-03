package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;

/**
 * The AboutAppActivity class provides a screen to display information about the application.
 * This typically includes details such as version, developers, and license information.
 */
public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Called when the activity is first created.
     * Sets up the layout for the "About App" screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutapp);

        // Initialize UI components
        Button exitButton = findViewById(R.id.exitAboutApp);

        // Set click listeners for buttons
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.exitAboutApp) {
            // Go back to previous screen
            finish();
        }
    }
}

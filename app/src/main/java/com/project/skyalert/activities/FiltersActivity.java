package com.project.skyalert.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;

/**
 * The FiltersActivity class provides a screen for users to configure filter settings
 * for alerts and notifications within the application.
 */
public class FiltersActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Sets up the layout for the filters screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the saved data. Otherwise, it is null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
    }
}

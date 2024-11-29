package com.project.skyalert.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;

/**
 * The AboutAppActivity class provides a screen to display information about the application.
 * This typically includes details such as version, developers, and license information.
 */
public class AboutAppActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Sets up the layout for the "About App" screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the saved data. Otherwise, it is null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutapp);
    }
}

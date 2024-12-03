package com.project.skyalert.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;

/**
 * The NotificationActivity class provides a screen for displaying notifications
 * related to the application's alerts and updates.
 */
public class NotificationActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * Sets up the layout for the notifications screen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }
}

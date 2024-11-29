package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;
import com.project.skyalert.ui.UIManager;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageButton aboutAppButton = findViewById(R.id.aboutAppButton);
        aboutAppButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.aboutAppButton) {
            UIManager.loadNextActivity(this, AboutAppActivity.class);
        }
    }
}

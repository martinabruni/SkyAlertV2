package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.R;
import com.project.skyalert.ui.UIManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.connectButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UIManager.loadNextActivity(this, Screen1Activity.class);
    }
}
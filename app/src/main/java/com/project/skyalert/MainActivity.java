package com.project.skyalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipAddress = findViewById(R.id.ipAddressInput);
        Button connectButton = findViewById(R.id.button);
        connectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String ipAddress = this.ipAddress.getText().toString();
        Intent intent = new Intent(this, Screen2Activity.class);
        intent.putExtra("IP_ADDRESS", ipAddress);
        startActivity(intent);
    }
}
package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.MqttHandlerFacade;
import com.project.skyalert.NotificationHelper;
import com.project.skyalert.R;
import com.project.skyalert.ui.UIManager;

public class Screen1Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText ipAddress;
    private EditText port;
    private TextView connectionResult;
    private MqttHandlerFacade mqttHandlerFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        ipAddress = findViewById(R.id.ipAddressInput);
        port = findViewById(R.id.portInput);
        connectionResult = findViewById(R.id.connectionResult);
        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mqttHandlerFacade != null) {
            mqttHandlerFacade.disconnect();
            setConnectionResultMessage("Disconnected");
        }
    }

    @Override
    public void onClick(View v) {
        String ipAddress = this.ipAddress.getText().toString();
        String port = this.port.getText().toString();
        try {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            mqttHandlerFacade = MqttHandlerFacade.getInstance(notificationHelper); // Pass the dependency
            mqttHandlerFacade.validateAndConnect(connectionResult, ipAddress, port, this);
            setConnectionResultMessage("Connected");
            UIManager.loadNextActivity(this, Screen2Activity.class);
        } catch (Exception e) {
            setConnectionResultMessage(e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttHandlerFacade != null) {
            mqttHandlerFacade.disconnect();
        }
    }

    private void setConnectionResultMessage(String msg) {
        this.connectionResult.setText(msg);
    }
}

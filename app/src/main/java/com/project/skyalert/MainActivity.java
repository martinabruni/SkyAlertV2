package com.project.skyalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ipAddress;
    private TextView connectionResult;
    private MqttHandlerFacade mqttHandlerFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddress = findViewById(R.id.ipAddressInput);
        connectionResult = findViewById(R.id.connectionResult);
        Button connectButton = findViewById(R.id.button);
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
        try {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            mqttHandlerFacade = MqttHandlerFacade.getInstance(notificationHelper); // Pass the dependency
            mqttHandlerFacade.validateConnection(connectionResult, ipAddress, this);
            setConnectionResultMessage("Connected");
            Intent intent = new Intent(this, Screen2Activity.class);
            intent.putExtra("IP_ADDRESS", ipAddress);
            startActivity(intent);
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

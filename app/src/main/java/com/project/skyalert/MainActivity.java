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
    MqttHandlerFacade mqttHandlerFacade = MqttHandlerFacade.getInstance();

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
    public void onClick(View v) {
        String ipAddress = this.ipAddress.getText().toString();
        if (!isValidIpAddress(ipAddress)) {
            setConnectionResultMessage("Ip address not valid");
        } else {
            try {
                String brokerUrl = "tcp://" + ipAddress + ":1883";
                mqttHandlerFacade.connectAndSubscribe(brokerUrl, "ANDROID_CLIENT", this);
                setConnectionResultMessage("Connected");
                Intent intent = new Intent(this, Screen2Activity.class);
                intent.putExtra("IP_ADDRESS", ipAddress);
                startActivity(intent);
            } catch (Exception e) {
                setConnectionResultMessage("Connection to the broker failed");
            }
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

    private boolean isValidIpAddress(String ipAddress) {
        // Regular expression for matching an IPv4 address
        String ipPattern =
                "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";

        // Check if the input string matches the pattern
        return ipAddress != null && ipAddress.matches(ipPattern);
    }
}
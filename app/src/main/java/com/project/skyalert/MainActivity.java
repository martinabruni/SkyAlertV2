package com.project.skyalert;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText IP_ADDRESS;
    private TextView FEEDBACK_TEXTBOX;
    private TextView ALERT_TEXTBOX;
    private String BROKER_URL;
    private static final String CLIENT_ID = java.util.UUID.randomUUID().toString();
    private MqttHandler mqttHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Il programma sta iniziando");
        super.onCreate(savedInstanceState);
        System.out.println("Programma iniziato");
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        IP_ADDRESS = findViewById(R.id.ipAddressInput);
        button.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        mqttHandler.disconnect();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        setContentView(R.layout.activity_screen2);
        FEEDBACK_TEXTBOX = findViewById(R.id.feedbackTextView);
        ALERT_TEXTBOX = findViewById(R.id.alertsTextView);
        BROKER_URL = "tcp://" + IP_ADDRESS.getText().toString() + ":1883";
        FEEDBACK_TEXTBOX.setText("Connecting to broker " + BROKER_URL);
        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL,CLIENT_ID);
        FEEDBACK_TEXTBOX.setText("Connected");
        mqttHandler.subscribe("nina");
        mqttHandler.setMessageListener(new MqttHandler.MessageListener() {
            @Override
            public void onMessageReceived(String topic, String message) {
                // Run on the UI thread to update the EditText
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ALERT_TEXTBOX.setText("Topic: " + topic + "\nMessage: " + message);
                    }
                });
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
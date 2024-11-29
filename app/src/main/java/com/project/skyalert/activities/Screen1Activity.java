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

/**
 * The Screen1Activity class allows the user to input MQTT connection details and
 * establish a connection to the MQTT broker.
 */
public class Screen1Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText ipAddress; // Input field for the broker IP address
    private EditText port; // Input field for the broker port
    private TextView connectionResult; // Displays connection status messages
    private MqttHandlerFacade mqttHandlerFacade; // Handles MQTT operations

    /**
     * Initializes the activity and sets up the UI components.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        // Initialize UI components
        ipAddress = findViewById(R.id.ipAddressInput);
        port = findViewById(R.id.portInput);
        connectionResult = findViewById(R.id.connectionResult);
        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(this);
    }

    /**
     * Ensures the MQTT connection is disconnected when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (mqttHandlerFacade != null) {
            mqttHandlerFacade.disconnect();
            setConnectionResultMessage("Disconnected");
        }
    }

    /**
     * Handles the Connect button click. Validates the input and establishes a connection to the MQTT broker.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        String ipAddress = this.ipAddress.getText().toString();
        String port = this.port.getText().toString();
        try {
            // Create a NotificationHelper and initialize MQTT connection
            NotificationHelper notificationHelper = new NotificationHelper(this);
            mqttHandlerFacade = MqttHandlerFacade.getInstance(notificationHelper); // Pass the dependency
            mqttHandlerFacade.validateAndConnect(connectionResult, ipAddress, port, this);
            setConnectionResultMessage("Connected");

            // Navigate to the next screen
            UIManager.loadNextActivity(this, Screen2Activity.class);
        } catch (Exception e) {
            // Display error message if connection fails
            setConnectionResultMessage(e.getMessage());
        }
    }

    /**
     * Cleans up resources and disconnects the MQTT connection when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttHandlerFacade != null) {
            mqttHandlerFacade.disconnect();
        }
    }

    /**
     * Updates the connection result message displayed to the user.
     *
     * @param msg The message to display.
     */
    private void setConnectionResultMessage(String msg) {
        this.connectionResult.setText(msg);
    }
}

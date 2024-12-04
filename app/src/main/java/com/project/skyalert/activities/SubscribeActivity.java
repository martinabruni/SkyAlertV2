package com.project.skyalert.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.MqttHandlerFacade;
import com.project.skyalert.R;
import com.project.skyalert.ui.UIManager;
import com.project.skyalert.ui.ViewBinder;
import com.project.skyalert.ui.layouts.TopicItem;

import org.eclipse.paho.mqttv5.common.MqttException;

import java.util.List;

/**
 * The SubscribeActivity class allows users to subscribe to MQTT topics
 * and view the list of subscribed topics.
 */
public class SubscribeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout topicLayout; // Layout for displaying subscribed topics
    private EditText topic; // Input field for the topic name
    private ViewBinder<TopicItem> topicBinder; // Binder to link TopicItem data with views
    private final MqttHandlerFacade mqttHandlerFacade = MqttHandlerFacade.getInstance(); // MQTT handler for subscriptions

    TextView subscriptionResult; // Text view to display subscription result

    /**
     * Initializes the activity and sets up the UI components for topic subscription.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this contains the saved data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        // Initialize UI components
        ImageButton dashboardButton = findViewById(R.id.dashboardButton);
        ImageButton subscribeButton = findViewById(R.id.subscribeButton);
        topicLayout = findViewById(R.id.layout);
        topic = findViewById(R.id.topicInput);
        subscriptionResult = findViewById(R.id.subscriptionResult);

        // Set click listeners for buttons
        dashboardButton.setOnClickListener(this);
        subscribeButton.setOnClickListener(this);

        // Define how TopicItem data is bound to views
        topicBinder = (view, elementData) -> {
            TextView nameTextView = view.findViewById(R.id.topicTextView);
            ImageButton deleteButton = view.findViewById(R.id.deleteButton); // Match the button ID

            nameTextView.setText(elementData.getName());

            // Set up a click listener for the delete button
            deleteButton.setOnClickListener(v -> {
                topicLayout.removeView(view); // Remove this specific view from the layout
                try {
                    mqttHandlerFacade.unsubscribe(elementData); // Remove the topic from the subscribed list
                } catch (MqttException e) {
                    UIManager.setResultMessage(subscriptionResult, e.toString());
                }
            });
        };


        // Display the list of already subscribed topics
        UIManager.displayElements(mqttHandlerFacade.getTopics(), topicLayout, this, R.layout.topic_item, topicBinder);
    }

    /**
     * Handles button click events.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.dashboardButton) {
            // Return to the previous screen
            finish();
        } else if (id == R.id.subscribeButton) {
            // Subscribe to the topic entered by the user
            String topicString = topic.getText().toString();

            //check if the topic input is empty otherwise create a new topic item
            if (topicString.isEmpty()) {
                UIManager.setResultMessage(subscriptionResult, "Insert a topic");
                return;
            }
            TopicItem newTopic = new TopicItem(topicString);

            if (mqttHandlerFacade.topicListContains(newTopic)) {
                UIManager.setResultMessage(subscriptionResult, "Already subscribed to that topic");
                return;
            }
            UIManager.clearResultMessage(subscriptionResult);

            // Add the new topic to the UI and subscribe via the MQTT handler
            UIManager.displayElements(List.of(newTopic), topicLayout, this, R.layout.topic_item, topicBinder);
            try {
                mqttHandlerFacade.subscribe(topicString, newTopic);
            } catch (MqttException e) {
                subscriptionResult.setText(e.toString());
            }
            UIManager.clearResultMessage(topic);
        }
    }
}

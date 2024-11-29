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

import java.util.List;

public class SubscribeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout topicLayout;
    private EditText topic;
    ViewBinder<TopicItem> topicBinder;
    private final MqttHandlerFacade mqttHandlerFacade = MqttHandlerFacade.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        ImageButton dashboardButton = findViewById(R.id.dashboardButton);
        ImageButton subscribeButton = findViewById(R.id.subscribeButton);
        topicLayout = findViewById(R.id.topicLayout);
        topic = findViewById(R.id.topicInput);

        dashboardButton.setOnClickListener(this);
        subscribeButton.setOnClickListener(this);

        topicBinder = (view, elementData) -> {
            TextView nameTextView = view.findViewById(R.id.topicTextView);
            nameTextView.setText(elementData.getName());
        };
        UIManager.displayElements(mqttHandlerFacade.getTopics(), topicLayout, this, R.layout.topic_item, topicBinder);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dashboardButton) {
            finish();
        } else if (id == R.id.subscribeButton) {
            String topicString = topic.getText().toString();
            TopicItem newTopic = new TopicItem(topicString);
            UIManager.displayElements(List.of(newTopic), topicLayout, this, R.layout.topic_item, topicBinder);
            mqttHandlerFacade.subscribe(topicString, newTopic);
        }
    }
}

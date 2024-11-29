package com.project.skyalert.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.skyalert.MqttHandlerFacade;
import com.project.skyalert.R;
import com.project.skyalert.UIManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubscribeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton subscribeButton;
    private LinearLayout topicLayout;
    private EditText topic;
    private final MqttHandlerFacade mqttHandlerFacade = MqttHandlerFacade.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        ImageButton dashboardButton = findViewById(R.id.dashboardButton);
        subscribeButton = findViewById(R.id.subscribeButton);
        topicLayout = findViewById(R.id.topicLayout);
        topic = findViewById(R.id.topicInput);

        dashboardButton.setOnClickListener(this);
        subscribeButton.setOnClickListener(this);

        UIManager.displayElements(mqttHandlerFacade.getTopics(), topicLayout, this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.dashboardButton){
            finish();
        } else if (id == R.id.subscribeButton) {
            String topicString = topic.getText().toString();
            UIManager.displayElements(List.of(topicString), topicLayout, this);
            mqttHandlerFacade.subscribe(topicString);
        }
    }
}

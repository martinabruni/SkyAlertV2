package com.project.skyalert;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UIManager {
    public static TextView newTextView(Context context, String content){
        TextView newAlert = new TextView(context);
        newAlert.setText(content);
        return newAlert;
    }
    public static void loadNextActivity(AppCompatActivity currentActivity, Class<?> nextActivity) {
        Intent intent = new Intent(currentActivity, nextActivity);
        currentActivity.startActivity(intent);
    }
}

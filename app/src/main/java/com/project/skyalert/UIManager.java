package com.project.skyalert;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UIManager {
    private static TextView newTextView(Context context, String content){
        TextView newAlert = new TextView(context);
        newAlert.setText(content);
        return newAlert;
    }
    public static void loadNextActivity(AppCompatActivity currentActivity, Class<?> nextActivity) {
        Intent intent = new Intent(currentActivity, nextActivity);
        currentActivity.startActivity(intent);
    }

    public static void displayElements(List<String> elementList, LinearLayout scroll, Context context){
        for (String e:
                elementList) {
            showElement(scroll, context, e);
        }
    }

    private static void showElement(LinearLayout scroll, Context context, String e) {
        TextView newElement = newTextView(context, e);
        scroll.addView(newElement);
    }

}

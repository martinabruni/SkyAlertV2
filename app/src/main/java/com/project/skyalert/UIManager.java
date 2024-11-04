package com.project.skyalert;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

public class UIManager {

    public static TextView newTextView(Context context, String content){
        TextView newAlert = new TextView(context);
        newAlert.setText(content);
        return newAlert;
    }
}

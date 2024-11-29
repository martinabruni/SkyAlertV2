package com.project.skyalert.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UIManager {
    public static void loadNextActivity(AppCompatActivity currentActivity, Class<?> nextActivity) {
        Intent intent = new Intent(currentActivity, nextActivity);
        currentActivity.startActivity(intent);
    }

    private static <T> void showElement(LayoutInflater inflater, int layoutResourceId, ViewBinder<T> binder, T element, LinearLayout scroll) {
        View elementView = inflater.inflate(layoutResourceId, scroll, false);
        binder.bindView(elementView, element);
        scroll.addView(elementView);
    }

    /**
     * Display a list of elements in the provided LinearLayout by inflating the specified layout.
     */
    public static <T> void displayElements(List<T> elementList, LinearLayout scroll, Context context, int layoutResourceId, ViewBinder<T> binder) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (T element : elementList) {
            showElement(inflater, layoutResourceId, binder, element, scroll);
        }
    }
}

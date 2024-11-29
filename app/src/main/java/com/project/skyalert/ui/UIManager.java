package com.project.skyalert.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * The UIManager class provides utility methods for managing UI components
 * and navigation between activities in the application.
 */
public class UIManager {

    /**
     * Navigates from the current activity to the specified next activity.
     *
     * @param currentActivity The current activity from which the navigation is initiated.
     * @param nextActivity    The target activity class to navigate to.
     */
    public static void loadNextActivity(AppCompatActivity currentActivity, Class<?> nextActivity) {
        Intent intent = new Intent(currentActivity, nextActivity);
        currentActivity.startActivity(intent);
    }

    /**
     * Inflates a layout for a single element, binds data to the layout using the provided binder,
     * and adds the resulting view to the specified LinearLayout.
     *
     * @param inflater         The LayoutInflater used to inflate the layout.
     * @param layoutResourceId The resource ID of the layout to be inflated.
     * @param binder           A ViewBinder for binding data to the layout.
     * @param element          The data element to be displayed.
     * @param scroll           The LinearLayout where the view will be added.
     * @param <T>              The type of data element.
     */
    private static <T> void showElement(LayoutInflater inflater, int layoutResourceId, ViewBinder<T> binder, T element, LinearLayout scroll) {
        View elementView = inflater.inflate(layoutResourceId, scroll, false);
        binder.bindView(elementView, element);
        scroll.addView(elementView);
    }

    /**
     * Displays a list of elements in a LinearLayout by inflating the specified layout for each element.
     *
     * @param elementList      The list of data elements to be displayed.
     * @param scroll           The LinearLayout where the elements will be displayed.
     * @param context          The context used for inflating the layouts.
     * @param layoutResourceId The resource ID of the layout to be inflated for each element.
     * @param binder           A ViewBinder for binding data to each layout.
     * @param <T>              The type of data elements in the list.
     */
    public static <T> void displayElements(List<T> elementList, LinearLayout scroll, Context context, int layoutResourceId, ViewBinder<T> binder) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (T element : elementList) {
            showElement(inflater, layoutResourceId, binder, element, scroll);
        }
    }
}

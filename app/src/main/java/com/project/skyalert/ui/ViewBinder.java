package com.project.skyalert.ui;

import android.view.View;

/**
 * A functional interface for binding data to views dynamically.
 */
public interface ViewBinder<T> {
    void bindView(View view, T data);
}

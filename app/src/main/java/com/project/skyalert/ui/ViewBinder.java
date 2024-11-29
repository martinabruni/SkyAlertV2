package com.project.skyalert.ui;

import android.view.View;

/**
 * The ViewBinder interface defines a contract for binding data to a view.
 * It is used in scenarios where custom binding logic is required for displaying
 * data in dynamically generated layouts.
 *
 * @param <T> The type of the data that will be bound to the view.
 */
public interface ViewBinder<T> {

    /**
     * Binds data of type {@code T} to the specified view.
     *
     * @param view The view to which the data should be bound.
     * @param data The data to bind to the view.
     */
    void bindView(View view, T data);
}

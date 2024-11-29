package com.project.skyalert.ui.layouts;

/**
 * The AlertItem class represents an alert with associated data, message, and topic.
 * It is used to display alert information in the application's UI.
 */
public class AlertItem {
    private final String data; // Additional data associated with the alert
    private final String message; // The message content of the alert
    private final String topic; // The topic associated with the alert

    /**
     * Constructs an AlertItem with the specified data, message, and topic.
     *
     * @param data    Additional data associated with the alert.
     * @param message The message content of the alert.
     * @param topic   The topic associated with the alert.
     */
    public AlertItem(String data, String message, String topic) {
        this.data = data;
        this.message = message;
        this.topic = topic;
    }

    /**
     * Returns the additional data associated with the alert.
     *
     * @return The alert data.
     */
    public String getData() {
        return data;
    }

    /**
     * Returns the message content of the alert.
     *
     * @return The alert message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the topic associated with the alert.
     *
     * @return The alert topic.
     */
    public String getTopic() {
        return topic;
    }
}

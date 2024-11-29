package com.project.skyalert.ui.layouts;

public class AlertItem {
    private final String data;
    private final String message;
    private final String topic;

    public AlertItem(String data, String message, String topic) {
        this.data = data;
        this.message = message;
        this.topic = topic;
    }

    public String getData() { return data; }
    public String getMessage() { return message; }
    public String getTopic() { return topic; }
}

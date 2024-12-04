package com.project.skyalert.ui.layouts;

import java.util.Objects;

/**
 * The TopicItem class represents an MQTT topic.
 * It is used to store and display information about a topic in the application's UI.
 */
public class TopicItem {

    private final String name; // The name of the MQTT topic

    /**
     * Constructs a TopicItem with the specified topic name.
     *
     * @param name The name of the topic.
     */
    public TopicItem(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the topic.
     *
     * @return The topic name.
     */
    public String getName() {
        return name;
    }

    /**
     * Compares this TopicItem's name with the name of another TopicItem.
     *
     * @param t the TopicItem to compare with
     * @return {@code true} if the names are equal, {@code false} otherwise
     */
    public boolean isEqualTo(TopicItem t){
        return Objects.equals(this.name, t.getName());
    }
}

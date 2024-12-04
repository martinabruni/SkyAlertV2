# SkyAlert Application

SkyAlert is an Android application that leverages MQTT protocol to deliver real-time alerts and notifications. The application allows users to subscribe to specific MQTT topics, manage subscriptions, and receive notifications on their mobile devices. The app is designed to be user-friendly, efficient, and highly configurable.

## Features

- **Real-Time Alerts**: Receive instant notifications from subscribed MQTT topics.
- **Customizable Subscriptions**: Add/Remove MQTT topic subscriptions.
- **Error Handling**: Detect and display errors in MQTT messages with detailed descriptions.
- **Notification Support**: Send notifications for important alerts via the Android Notification system.
- **Intuitive UI**: Simple and responsive UI to manage alerts and subscriptions.

---

## Project Structure

### Core Components

1. **`MqttHandler`**

   - Handles MQTT broker connections, subscriptions, and message processing.
   - Supports observer pattern for notifying listeners about incoming messages.
   - Features error detection in MQTT payloads.

2. **`MqttHandlerFacade`**

   - A singleton extending `MqttHandler` to add functionality like topic management and IP/Port validation.
   - Provides utilities for managing multiple topics and seamless connection handling.

3. **`NotificationHelper`**
   - Manages Android notifications for received MQTT messages.
   - Implements support for Notification Channels for API 26+.

---

## Activities

### 1. **`MainActivity`**

- **Description**: The entry point of the application.
- **Functionality**:
  - Provides the "Connect" button to navigate users to `Screen1Activity`.
  - Implements edge-to-edge UI for modern Android design.

### 2. **`Screen1Activity`**

- **Description**: Handles the MQTT broker connection setup.
- **Functionality**:
  - Allows input of MQTT broker details such as IP and port.
  - Validates connection details and establishes a connection to the broker.
  - Displays connection status (success or failure).
  - Navigates to `Screen2Activity` on successful connection.

### 3. **`Screen2Activity`**

- **Description**: Displays alerts and provides options for further navigation.
- **Functionality**:
  - Displays real-time MQTT messages or alerts from subscribed topics.
  - Allows users to:
    - Clear all displayed alerts.
    - Navigate to the `SubscribeActivity` for managing topic subscriptions.
    - Navigate to `SettingsActivity` or `FiltersActivity`.
    - Disconnect from the MQTT broker.
  - Provides local notifications for messages containing error_list.

### 4. **`SettingsActivity`**

- **Description**: Manages application settings.
- **Functionality**:
  - Provides options for:
    - Changing the MQTT broker settings.
    - Navigating to `SubscribeActivity` to add new topics.
    - Configuring notifications in `NotificationActivity`.
    - Viewing application details in `AboutAppActivity`.
  - Implements a back-navigation button to exit to the previous screen.

### 5. **`SubscribeActivity`**

- **Description**: Manages MQTT topic subscriptions.
- **Functionality**:
  - Displays a list of subscribed topics.
  - Allows users to subscribe to new topics by entering the topic name.
  - Allows users to unsubscribe from a topic clicking on the delete button(X) of a topic item.
  - Updates the displayed topic list dynamically upon subscription.

### 6. **`AboutAppActivity`**

- **Description**: Displays information about the application.
- **Functionality**:
  - Includes an "Exit" button to navigate back to the previous screen.

### 7. **`FiltersActivity`**

- **Description**: Configures alert filters.
- **Functionality**:
  - Allows users to define and adjust filters for received MQTT messages or alerts.

### 8. **`NotificationActivity`**

- **Description**: Manages notifications for alerts.
- **Functionality**:
  - Allows user to customize the notifications sound.
  - Allows user to upload an mp3 file from the their smartphone.
  - Displays default or uploaded notifications sounds.

---

### UI Components

1. **`UIManager`**

   - Utility class for inflating and binding UI components dynamically.
   - Handles navigation between activities.

2. **Layouts**

   - **`AlertItem`**: Represents an individual alert message with associated data.
   - **`TopicItem`**: Represents an MQTT topic for display in the UI.

3. **`ViewBinder`**
   - Interface for binding data elements to their corresponding UI views.

---

## Usage

### Setting Up MQTT Connection

1. Launch the app.
2. Input the broker IP and port on the connection screen (`Screen1Activity`).
3. Press "Connect" to establish a connection.
4. For hosting your own MQTT broker, consider using the [MQTTBroker repository](https://github.com/DevidCimarelli/MQTTBroker) as a starting point for configuring a reliable broker environment.

### Managing Subscriptions

1. Navigate to the subscription page (`SubscribeActivity`).
2. Add topics by entering their names and clicking "Subscribe."
3. View all subscribed topics in the displayed list.
4. Remove a topic by clicking on its X button.

### Viewing Alerts

1. After connecting, proceed to the alerts screen (`Screen2Activity`).
2. View real-time messages from subscribed topics.
3. Use the provided buttons to clear alerts or disconnect.

### Notifications

- Alerts containing errors are sent as Android notifications for better visibility.

---

## Dependencies

- **Paho MQTT Library**: For MQTT communication.
- **AndroidX**: For compatibility and modern Android features.
- **JSON**: For parsing MQTT message payloads.

---

## Installation

1. Clone the repository.
2. Checkout dev branch.
3. Import the project into Android Studio.
4. Configure the MQTT broker settings in the app.
5. Build the app.
6. Allow notification on the app.
7. Run the app on an Android device.

---

## Contributors

- **Project Lead**: [Fileni Francesco]
- **Developers**: [Bruni Martina, Fileni Francesco, Cimarelli Devid]
- **UI Design**: [Eftimie Daniela, Mabilia Eleonora, Mariotti Elisa]
- **QA Testing**: [Cimarelli Devid, Mabilia Eleonora]

---

For more information, feel free to contact us at [allievo_fileni.f@itsumbria.it](mailto:allievo_fileni.f@itsumbria.it).

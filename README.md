# SkyAlert Application

SkyAlert is an Android application that leverages MQTT protocol to deliver real-time alerts and notifications. The application allows users to subscribe to specific MQTT topics, manage subscriptions, and receive notifications on their mobile devices. The app is designed to be user-friendly, efficient, and highly configurable.

## Features

- **Real-Time Alerts**: Receive instant notifications from subscribed MQTT topics.
- **Customizable Subscriptions**: Add MQTT topic subscriptions.
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

   - A singleton extending `MqttHandler` to add functionality like topic management and IP validation.
   - Provides utilities for managing multiple topics and seamless connection handling.

3. **`NotificationHelper`**
   - Manages Android notifications for received MQTT messages.
   - Implements support for Notification Channels for API 26+.

---

### Activities

1. **`MainActivity`**

   - The entry point of the app.
   - Provides the "Connect" button for users to navigate further into the app.

2. **`Screen1Activity`**

   - Allows users to input MQTT connection details (broker IP and port) and establish a connection.
   - Displays connection status messages.

3. **`Screen2Activity`**

   - Displays real-time alerts received from MQTT topics.
   - Provides options to clear alerts, navigate to the subscription page, or disconnect.

4. **`SettingsActivity`**

   - Offers application settings and a button to navigate to the "About App" screen.

5. **`SubscribeActivity`**

   - Enables users to manage their MQTT subscriptions.
   - Displays a list of subscribed topics and allows adding new topics.

6. **`AboutAppActivity`**
   - Displays information about the application, such as version and developers.

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

### Viewing Alerts

1. After connecting, proceed to the alerts screen (`Screen2Activity`).
2. View real-time messages from subscribed topics.
3. Use the provided buttons to clear alerts or disconnect.

### Notifications

- Alerts flagged as important or containing errors are sent as Android notifications for better visibility.

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
5. Build and run the app on an Android device.
6. Allow notification on the app

---

## Contributors

- **Project Lead**: [Fileni Francesco]
- **Developers**: [Bruni Martina, Fileni Francesco, Cimarelli Devid]
- **UI Design**: [Eftimie Daniela, Mabilia Eleonora, Mariotti Elisa]
- **QA Testing**: [Cimarelli Devid]

---

For more information, feel free to contact us at [allievo_fileni.f@itsumbria.it](mailto:allievo_fileni.f@itsumbria.it).

# Daily

Daily is an Android application designed to help users plan and manage their daily tasks and events. The app is written entirely in Kotlin using Android Studio and follows Clean Architecture principles.

## Key Features

- **Task Management**: Easily add, edit, and delete tasks.
- **Reminders**: Set reminders for important tasks and events.
- **Simple Interface**: Intuitive and user-friendly interface for efficient task and reminder management.

## Technologies and Libraries Used

- **Room**:
  - `implementation "androidx.room:room-ktx:2.5.1"`
  - `kapt "androidx.room:room-compiler:2.5.1"`
- **Coroutines**:
  - `implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"`
- **Navigation**:
  - `implementation "androidx.navigation:navigation-fragment-ktx:2.5.3"`
  - `implementation "androidx.navigation:navigation-ui-ktx:2.5.3"`
- **Gson**:
  - `implementation "com.google.code.gson:gson:2.9.0"`
- **Hilt**:
  - `implementation "com.google.dagger:hilt-android:2.44"`
  - `kapt "com.google.dagger:hilt-compiler:2.44"`
- **Preferences DataStore**:
  - `implementation "androidx.datastore:datastore-preferences:1.0.0-alpha04"`

## Installation

1. **Clone the repository**
    ```bash
    git clone https://github.com/ArsTaroyan/Daily.git
    cd Daily
    ```

2. **Open the project**
    Open the project in [Android Studio](https://developer.android.com/studio).

3. **Sync Gradle**
    The project uses Gradle to manage dependencies. Sync Gradle to download all required dependencies.

4. **Run the application**
    Connect your Android device or use an emulator to run the application.

## Project Structure

The project follows Clean Architecture principles, ensuring modularity and testability of the code. The main modules include:

- **data**: Contains data sources (Room database, SharedPreferences).
- **domain**: Contains business logic (use cases, models).
- **presentation**: Contains the user interface (Activity, Fragment, ViewModel).

## Project Description

The application contains 2 screens: one for the task list and another for reminders. The design is built using XML layout.

![Daily photo](https://github.com/ArsTaroyan/Daily/assets/96776103/ad363adf-7ab5-4e77-9ea9-e1eb93e4a432)

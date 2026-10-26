# WeatherApp

A weather application for Android built with **Kotlin** and **Jetpack Compose**. WeatherApp allows users to explore weather information for Turkish cities through a simple and user-friendly interface.

## Features

- **City Weather Overview:** View weather information for a selection of major cities in Türkiye.
- **City Search:** Search for cities by name.
- **City Suggestions:** Search for cities using location suggestions from the geocoding service.
- **Weather Details:** View additional weather information for a selected city.
- **Modern UI:** A clean interface built with Jetpack Compose.
- **Asynchronous Network Requests:** Retrieve weather data using Kotlin coroutines.

## Technologies Used

- **Kotlin** — Main programming language
- **Jetpack Compose** — Declarative UI toolkit for Android
- **Android SDK** — Android application development
- **ViewModel** — UI-related state management
- **Kotlin Coroutines** — Asynchronous operations
- **StateFlow** — Reactive UI state handling
- **Retrofit** — HTTP API communication
- **Gson** — JSON serialization and deserialization
- **Open-Meteo API** — Weather forecasts and city geocoding

## APIs

WeatherApp uses the Open-Meteo services to retrieve weather and city information.

- **Weather Forecast API:** https://open-meteo.com/en/docs
- **Geocoding API:** https://open-meteo.com/en/docs/geocoding-api

Weather information is retrieved using the selected city's geographic coordinates.



## Getting Started

### Prerequisites

- Android Studio
- Android SDK
- Internet connection

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/busraturcan/WeatherApp.git
   ```

2. Open the project in Android Studio.

3. Allow Gradle to sync and download the required dependencies.

4. Run the application on an Android emulator or a connected Android device.

## Architecture

The project separates data retrieval, business logic, and UI components to keep the code organized and maintainable.

- **Data Models:** Represent city and weather data.
- **Remote Data Source:** Defines API endpoints using Retrofit.
- **Repository:** Handles data retrieval and provides weather information to the application.
- **ViewModel:** Manages UI state and coordinates application operations.
- **Jetpack Compose UI:** Displays weather information and handles user interactions.

## Project Goal

This project was developed to practice Android application development with Kotlin, Jetpack Compose, API integration, asynchronous programming, and state management.

## 👩‍💻 Author

**Büşra Turcan**

- GitHub: [@busraturcan](https://github.com/busraturcan)

---

*WeatherApp — Simple weather information, wherever you need it.*

# Weather App

## Overview
This is a weather app that provides real-time weather information based on the user's location or manually entered city. The app is built using the MVVM (Model-View-ViewModel) architecture to ensure a clean and maintainable codebase.

## Features
- **Current Weather:** Get detailed information about the current weather conditions.
- **Search:** Enter the name of any city to fetch its weather details.
- **History:** View a history of previously searched cities.
- **Location-based:** Automatically fetch weather information based on the user's current location.

## Architecture
The app follows the MVVM architectural pattern, separating the application logic into three main components:

### ViewModel
- Acts as a bridge between the Model and the View, handling UI-related logic.
- LiveData is used to observe changes in the underlying data and update the UI accordingly.
- Coroutine is used for asynchronous operations, ensuring a responsive user interface.

## Libraries and Technologies
- **Android Architecture Components:** LiveData, ViewModel, Room Database.
- **Coroutines:** For asynchronous programming and background tasks.
- **Retrofit:** For making network requests to fetch weather data.
- **Google Play Services:** Fused Location Provider for fetching the user's location.

## How to Build
To build and run the app locally, follow these steps:
1. Clone the repository: `git clone https://github.com/your-username/weather-app.git`
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

Feel free to contribute, report issues, or suggest improvements! Your feedback is highly appreciated.

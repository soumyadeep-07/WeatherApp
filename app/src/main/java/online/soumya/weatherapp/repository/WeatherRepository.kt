package online.soumya.weatherapp.repository

import android.util.Log
import online.soumya.weatherapp.util.WeatherApiService
import online.soumya.weatherapp.model.WeatherResponse

class WeatherRepository {

    suspend fun getWeather(location: String, apiKey: String): WeatherResponse? {
        val url = "weather?q=$location&appid=$apiKey"
        val fullUrl = "https://api.openweathermap.org/data/2.5/$url"

        return try {
            val response = WeatherApiService.create().getWeather(fullUrl)
            if (response.isSuccessful) {
                response.body()
            } else {
                // Log the error response
                val errorBody = response.errorBody()?.string()
                Log.e("WeatherRepository", "Error response: $errorBody")
                null
            }
        } catch (e: Exception) {
            // Log the exception
            Log.e("WeatherRepository", "Exception: ${e.message}")
            null
        }
    }
}


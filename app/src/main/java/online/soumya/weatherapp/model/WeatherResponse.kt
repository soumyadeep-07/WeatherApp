package online.soumya.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("weather") val weather: List<WeatherInfo>,
    @SerializedName("visibility") val visibility: Double?,
    @SerializedName("pressure") val pressure: Double?,
    @SerializedName("aqi") val aqi: Int?,
    @SerializedName("dew_point") val dewPoint: Double?
)

data class Main(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val lowTemperature: Double,
    @SerializedName("humidity") val humidity: Int
)

data class Wind(
    @SerializedName("speed") val windSpeed: Double
)

data class WeatherInfo(
    @SerializedName("main") val weatherMain: String,
    @SerializedName("description") val weatherDescription: String
)


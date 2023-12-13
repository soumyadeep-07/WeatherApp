package online.soumya.weatherapp.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.soumya.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface WeatherApiService {

    @GET
    suspend fun getWeather(@Url url: String): Response<WeatherResponse>

    companion object {
        fun create(): WeatherApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build()

            return retrofit.create(WeatherApiService::class.java)
        }

        private fun createOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }
    }
}

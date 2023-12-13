package online.soumya.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import online.soumya.weatherapp.repository.WeatherRepository
import online.soumya.weatherapp.model.WeatherResponse

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> get() = _weather

    fun getWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            val result = repository.getWeather(city, apiKey)
            _weather.value = result
        }
    }
}

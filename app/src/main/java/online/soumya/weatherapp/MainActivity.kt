//package online.soumya.weatherapp
//
//import android.annotation.SuppressLint
//import android.content.pm.PackageManager
//import android.location.Location
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.Manifest
//import android.content.Context
//import android.location.Address
//import android.location.Geocoder
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.PopupMenu
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import online.soumya.weatherapp.databinding.ActivityMainBinding
//import java.io.IOException
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import java.util.Locale
//import kotlin.math.roundToInt
//
//class MainActivity : AppCompatActivity() {
//    private val binding by lazy{
//        ActivityMainBinding.inflate(layoutInflater)
//    }
//    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var viewModel: WeatherViewModel
//    private lateinit var cityDao: CityDao
//
//    @SuppressLint("SetTextI18n")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        // Initialize Room database and DAO
//        val cityDatabase = CityDatabase.getDatabase(applicationContext)
//        // Check for location permission before requesting location updates
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            // Permission has been granted, proceed with location-related operations
//            fetchCurrentCity()
//        }
//        cityDao = cityDatabase.cityDao()
//
//        // Get the current date
//        val currentLocalDate = LocalDate.now()
//
//        binding.imgbtnClose.setOnClickListener {
//            binding.txtLocation.visibility = View.VISIBLE
//            binding.imgDrawableLayout.visibility = View.VISIBLE
//            binding.imgbtnThreeDot.visibility = View.VISIBLE
//            binding.edtCityName.visibility = View.GONE
//            binding.imgbtnClose.visibility = View.GONE
//            binding.edtCityName.text.clear()
//        }
//        binding.edtCityName.setOnEditorActionListener { _, _, _ ->
//            // Fetch weather data based on the entered city name
//            val cityName = binding.edtCityName.text.toString().trim()
//            if (cityName.isNotEmpty()) {
//                // Insert the city into the Room database
//                GlobalScope.launch {
//                    cityDao.insertCity(City(cityName = cityName))
//                }
//                // Trigger the API call
//                viewModel.getWeather(cityName, "78c916fb4b980d8dfa0869a7bb47044e")
//                binding.txtLocation.text = cityName
//            }
//            true // Return true to indicate that the action has been handled
//        }
//        binding.imgbtnThreeDot.setOnClickListener {
//            val popupMenu = PopupMenu(this, it)
//            popupMenu.menuInflater.inflate(R.menu.menu_drawer, popupMenu.menu)
//
//            // Set up the menu item click listener
//            popupMenu.setOnMenuItemClickListener { menuItem ->
//                when (menuItem.itemId) {
//                    R.id.action_search -> {
//                        // Handle item 1 click
//                        binding.txtLocation.visibility = View.GONE
//                        binding.imgDrawableLayout.visibility = View.GONE
//                        binding.imgbtnThreeDot.visibility = View.GONE
//                        binding.edtCityName.visibility = View.VISIBLE
//                        binding.imgbtnClose.visibility = View.VISIBLE
//                        true
//                    }
//                    R.id.action_history -> {
//                        // Handle item 2 click
//                        showCityHistoryDialog()
//                        true
//                    }
//                    else -> false
//                }
//            }
//
//            // Show the popup menu
//            popupMenu.show()
//        }
//
//        // Format the date as "Today, 12 September"
//        val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale.getDefault())
//        val formattedDate = currentLocalDate.format(formatter)
////        val location = "Kolkata"
//        binding.txtDate.text = formattedDate
////        binding.txtLocation.text = location
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        viewModel = ViewModelProvider(this, WeatherViewModelFactory(WeatherRepository()))
//            .get(WeatherViewModel::class.java)
//
//        viewModel.weather.observe(this, Observer { weatherResponse ->
//            // Handle the weather response
//            if (weatherResponse != null) {
//                // Handle the weather response
//                val temperatureKelvin = weatherResponse.main.temperature
//                val feelsLikeFahrenheit = weatherResponse.main.feelsLike
//                val lowTemperatureFahrenheit = weatherResponse.main.lowTemperature
//                val weatherMain = weatherResponse.weather.firstOrNull()?.weatherMain
//                val weatherDescription = weatherResponse.weather.firstOrNull()?.weatherDescription
//                val aqi = weatherResponse.aqi
//                val windSpeed = weatherResponse.wind.windSpeed
//                val humidity = weatherResponse.main.humidity
//                val visibility = weatherResponse.visibility
//                val pressure = weatherResponse.pressure
//                val dewPoint = weatherResponse.dewPoint
//                val temperatureCelsius = temperatureKelvin - 273.15
//                // Convert Fahrenheit to Celsius
//                // Log or handle the values
//                binding.txtTemperature.text = "${temperatureCelsius.roundToInt()} °C"
//                binding.txtWind.text = "$windSpeed m/s"
//                binding.txtHum.text =  "$humidity%"
//                binding.txtWeather.text = weatherMain
//                Log.d("WeatherApp", "Temperature: ${temperatureCelsius.roundToInt()} °C")
////                Log.d("WeatherApp", "Feels Like: $feelsLikeCelsius °C")
////                Log.d("WeatherApp", "Low Temperature: $lowTemperatureCelsius °C")
//                Log.d("WeatherApp", "Weather: $weatherMain - $weatherDescription")
//                Log.d("WeatherApp", "Air Quality Index: $aqi")
//                Log.d("WeatherApp", "Wind Speed: $windSpeed mph")
//                Log.d("WeatherApp", "Humidity: $humidity%")
//                Log.d("WeatherApp", "Visibility: $visibility mi")
//                Log.d("WeatherApp", "Pressure: $pressure in")
//                Log.d("WeatherApp", "Dew Point: $dewPoint")
//            } else {
//                // Handle error
//                Log.d("WeatherApp", "Temperature:Error")
//                Toast.makeText(this@MainActivity,"City weather data not found. Please try again later.",Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        // Trigger the API call
////        viewModel.getWeather("Kolkata", "78c916fb4b980d8dfa0869a7bb47044e")
//    }
//    private fun showCityHistoryDialog() {
//        // Retrieve the city names from the Room database
//        cityDao.getAllCities().observe(this, Observer { cities ->
//            val cityArray = cities.map { it.cityName }.toTypedArray()
//
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("City History")
//            builder.setItems(cityArray) { _, index ->
//                val selectedCity = cities[index]
//                // 'selectedCity' now contains the City object corresponding to the selected item
//                // Trigger the API call
//                viewModel.getWeather(selectedCity.cityName, "78c916fb4b980d8dfa0869a7bb47044e")
//                binding.txtLocation.text = selectedCity.cityName
//            }
//
//            builder.setPositiveButton("OK") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        })
//    }
//    // Handle the permission request result
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission granted, proceed with location-related operations
//                    fetchCurrentCity()
//                } else {
//                    // Permission denied, handle accordingly (show rationale, disable location features, etc.)
//                    Log.d("Permission", "Location permission denied")
//                }
//            }
//        }
//    }
//    private fun fetchCurrentCity() {
//        val locationRequest = LocationRequest.create()
//            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//            .setInterval(10000)  // 10 seconds
//
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val lastLocation = locationResult.lastLocation
//                val cityName = getCityNameFromLocation(lastLocation!!.latitude, lastLocation.longitude, this@MainActivity)
//                Log.d("CurrentCity", "Current city: $cityName")
//                GlobalScope.launch {
//                    cityDao.insertCity(City(cityName = cityName))
//                }
//                viewModel.getWeather(cityName, "78c916fb4b980d8dfa0869a7bb47044e")
//                binding.txtLocation.text = cityName  // Update UI with the current city
//                fusedLocationClient.removeLocationUpdates(this)
//            }
//        }
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//    }
//
//
//
//    private fun getCityNameFromLocation(latitude: Double, longitude: Double, context: Context): String {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        try {
//            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
//            if (addresses!!.isNotEmpty()) {
//                val cityName = addresses[0].locality
//                return cityName ?: "Unknown City"
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return "Unknown City"
//    }
//
//}
package online.soumya.weatherapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import online.soumya.weatherapp.databse.CityDao
import online.soumya.weatherapp.databinding.ActivityMainBinding
import online.soumya.weatherapp.databse.CityDatabase
import online.soumya.weatherapp.databse.City
import online.soumya.weatherapp.model.WeatherResponse
import online.soumya.weatherapp.repository.WeatherRepository
import online.soumya.weatherapp.viewModel.WeatherViewModel
import online.soumya.weatherapp.viewModel.WeatherViewModelFactory
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: WeatherViewModel
    private lateinit var cityDao: CityDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val cityDatabase = CityDatabase.getDatabase(applicationContext)

        // Check for location permission before requesting location updates
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission has been granted, proceed with location-related operations
            fetchCurrentCity()
        }

        cityDao = cityDatabase.cityDao()

        // Get the current date
        val currentLocalDate = LocalDate.now()

        // UI setup and listeners
        setupUI()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel = ViewModelProvider(this, WeatherViewModelFactory(WeatherRepository()))
            .get(WeatherViewModel::class.java)

        // Observe weather data
        observeWeatherData()

        // Trigger the API call
        fetchWeatherForCity("Kolkata")
    }

    private fun setupUI() {
        // Set up UI components, listeners, etc.
        val currentLocalDate = LocalDate.now()

        binding.imgbtnClose.setOnClickListener {
            toggleViewsVisibility(true)
            binding.edtCityName.text.clear()
        }

        binding.edtCityName.setOnEditorActionListener { _, _, _ ->
            fetchWeatherForCity(binding.edtCityName.text.toString().trim())
            true
        }

        binding.imgbtnThreeDot.setOnClickListener {
            showPopupMenu()
        }

        val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale.getDefault())
        val formattedDate = currentLocalDate.format(formatter)

        binding.txtDate.text = formattedDate
    }

    private fun toggleViewsVisibility(showMainViews: Boolean) {
        with(binding) {
            txtLocation.visibility = if (showMainViews) View.VISIBLE else View.GONE
            imgDrawableLayout.visibility = if (showMainViews) View.VISIBLE else View.GONE
            imgbtnThreeDot.visibility = if (showMainViews) View.VISIBLE else View.GONE
            edtCityName.visibility = if (!showMainViews) View.VISIBLE else View.GONE
            imgbtnClose.visibility = if (!showMainViews) View.VISIBLE else View.GONE
        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this, binding.imgbtnThreeDot)
        popupMenu.menuInflater.inflate(R.menu.menu_drawer, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    toggleViewsVisibility(false)
                    true
                }
                R.id.action_history -> {
                    showCityHistoryDialog()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun observeWeatherData() {
        viewModel.weather.observe(this, Observer { weatherResponse ->
            if (weatherResponse != null) {
                handleWeatherResponse(weatherResponse)
            } else {
                handleWeatherError()
            }
        })
    }

    private fun handleWeatherResponse(weatherResponse: WeatherResponse) {
        // Handle the weather response
        val temperatureKelvin = weatherResponse.main.temperature
        val windSpeed = weatherResponse.wind.windSpeed
        val humidity = weatherResponse.main.humidity
        val weatherMain = weatherResponse.weather.firstOrNull()?.weatherMain

        // Log or handle the values
        with(binding) {
            txtTemperature.text = "${(temperatureKelvin - 273.15).roundToInt()} °C"
            txtWind.text = "$windSpeed m/s"
            txtHum.text = "$humidity%"
            txtWeather.text = weatherMain
        }

        Log.d("WeatherApp", "Temperature: ${(temperatureKelvin - 273.15).roundToInt()} °C")
        Log.d("WeatherApp", "Wind Speed: $windSpeed m/s")
        Log.d("WeatherApp", "Humidity: $humidity%")
        Log.d("WeatherApp", "Weather: $weatherMain")
    }

    private fun handleWeatherError() {
        // Handle error and show a Toast message
        Log.d("WeatherApp", "Temperature: Error")
        Toast.makeText(this@MainActivity, "City weather data not found. Please try again later.", Toast.LENGTH_LONG).show()
        binding.txtLocation.text = "Kolkata"
    }

    private fun fetchWeatherForCity(cityName: String) {
        if (cityName.isNotEmpty()) {
            GlobalScope.launch {
                cityDao.insertCity(City(cityName = cityName))
            }
            viewModel.getWeather(cityName, "78c916fb4b980d8dfa0869a7bb47044e")
            binding.txtLocation.text = cityName
        }
    }

    private fun showCityHistoryDialog() {
        cityDao.getAllCities().observe(this, Observer { cities ->
            val cityArray = cities.map { it.cityName }.toTypedArray()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("City History")
            builder.setItems(cityArray) { _, index ->
                fetchWeatherForCity(cities[index].cityName)
            }

            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with location-related operations
                    fetchCurrentCity()
                } else {
                    // Permission denied, handle accordingly (show rationale, disable location features, etc.)
                    Log.d("Permission", "Location permission denied")
                }
            }
        }
    }

    private fun fetchCurrentCity() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)  // 10 seconds

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                val cityName = getCityNameFromLocation(lastLocation!!.latitude, lastLocation.longitude, this@MainActivity)
                Log.d("CurrentCity", "Current city: $cityName")
                GlobalScope.launch {
                    cityDao.insertCity(City(cityName = cityName))
                }
                viewModel.getWeather(cityName, "78c916fb4b980d8dfa0869a7bb47044e")
                binding.txtLocation.text = cityName  // Update UI with the current city
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun getCityNameFromLocation(latitude: Double, longitude: Double, context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val cityName = addresses[0].locality
                return cityName ?: "Unknown City"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "Unknown City"
    }
}

package com.lokhate.meteo.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.lokhate.meteo.R
import com.lokhate.meteo.data.model.Forecast
import com.lokhate.meteo.data.model.ForecastApi
import com.lokhate.meteo.ui.viewmodel.ForecastViewModel
import com.lokhate.meteo.utils.ForecastHelper
import com.lokhate.meteo.utils.LocationHelper
import com.lokhate.meteo.utils.SharedPreferencesHelper
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity(), LocationListener {
    private val forecastViewModel: ForecastViewModel by viewModels()
    private lateinit var locationManager: LocationManager
//    private lateinit var forecastAdapter: ForecastAdapter
    private lateinit var sharedPrefs: SharedPreferencesHelper
    private var forecastList: List<Forecast>? = emptyList()
    private var city: String = "Unknown"
    private var country: String = "Unknown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate")

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        sharedPrefs = SharedPreferencesHelper.getInstance(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "request permission")
            requestLocationPermission()
        } else {
            Log.d("MainActivity", "get location")
            getUserLocation()
        }

        forecastViewModel.city.observe(this, Observer { city: String ->
            this.city = city
        })
        forecastViewModel.country.observe(this, Observer { country: String ->
            this.country = country
        })
        forecastViewModel.forecast.observe(this, Observer { forecast: List<Forecast> ->
            sharedPrefs.putForecastList(forecast)
            forecastList = forecast
        })

        if (sharedPrefs.getForecastList().isEmpty()) {
            Log.d("MainActivity", "list empty")
            var lat = sharedPrefs.getDouble("latitude")
            var lon = sharedPrefs.getDouble("longitude")
            val timezone = sharedPrefs.getString("timezone") ?: "Europe/Monaco"
            Log.d("MainActivity", "lat: $lat lon: $lon")
            if (lat == 0.0) lat = 43.7323492
            if (lon == 0.0) lon = 7.4276832
            forecastViewModel.fetchForecast(lat, lon, timezone, city, country)
        } else {
            forecastList = sharedPrefs.getForecastList()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getUserLocation()
            } else {
                Toast.makeText(this, "You need to grant location permission to use the app", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Get user location")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        val timezone = getUserTimezone()
        val (city, country) = LocationHelper.getAddress(this, latitude, longitude)
        Log.d("MainActivity", "latitude: $latitude, longitude: $longitude, timezone: $timezone, city: $city, country: $country")
        saveUserValues(latitude, longitude, timezone, city, country)
        forecastViewModel.fetchForecast(latitude, longitude, timezone, city, country)
        locationManager.removeUpdates(this)
    }

    private fun saveUserValues(latitude: Double, longitude: Double, timezone: String, city: String, country: String) {
        sharedPrefs.putString("city", city)
        sharedPrefs.putString("country", country)
        sharedPrefs.putDouble("longitude", longitude)
        sharedPrefs.putDouble("latitude", latitude)
        sharedPrefs.putString("timezone", timezone)
    }

    private fun getUserTimezone(): String {
        val timezone: TimeZone = TimeZone.getDefault()
        return timezone.id
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}
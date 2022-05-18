package com.example.weatherandmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherandmap.databinding.ActivityMapsBinding
import com.example.weatherandmap.view_model.WeatherViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var weatherViewModel: WeatherViewModel

    private var latitude = 41.2995
    private var longitude = 69.2401
    private val TAG = "MapsActivity"

    private fun mapClick() {
        weatherViewModel = ViewModelProvider(this@MapsActivity).get(WeatherViewModel::class.java)
        mMap.setOnMapClickListener { it1 ->
            weatherViewModel.setLocationLatitude(it1.latitude)
            weatherViewModel.setLocationLongitude(it1.longitude)
            mMap.clear()
            val place = LatLng(latitude, longitude)
            latitude = it1.latitude
            Log.d(TAG, "mapClick: $latitude")
            Log.d(TAG, "mapClick: $longitude")
            longitude = it1.longitude
            val camera = CameraUpdateFactory.newLatLngZoom(place, 5f)
            mMap.addMarker(MarkerOptions().position(place).title("Choose place"))
            mMap.animateCamera(camera)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        weatherViewModel = ViewModelProvider(this@MapsActivity).get(WeatherViewModel::class.java)
        weatherViewModel.getLatitude().observe(this@MapsActivity, Observer {
            latitude = it
        })
        weatherViewModel.getLongitude().observe(this@MapsActivity, Observer {
            longitude = it
        })
        mMap = googleMap
        val place = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(place).title("Choose place"))
        val tashkent = LatLng(latitude, longitude)
        val camera = CameraUpdateFactory.newLatLngZoom(tashkent, 5f)
        mMap.moveCamera(camera)
        mMap.animateCamera(camera)
        mapClick()
        getWeatherClick()
    }

    private fun getWeatherClick() {
        weatherViewModel = ViewModelProvider(this@MapsActivity).get(WeatherViewModel::class.java)
        weatherViewModel.getLatitude().observe(this@MapsActivity, Observer {
            latitude = it
        })
        weatherViewModel.getLongitude().observe(this@MapsActivity, Observer {
            longitude = it
        })
        binding.getWeather.setOnClickListener {
            if (latitude != 0.0 && longitude != 0.0) {
                val bundle = Bundle()
                Log.d(TAG, "getWeatherClick: $latitude")
                Log.d(TAG, "getWeatherClick: $longitude")
                bundle.putDouble("latitude", latitude)
                bundle.putDouble("longitude", longitude)
                val intent = Intent(this, WeatherActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}
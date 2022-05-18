package com.example.weatherandmap

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.weatherandmap.databinding.ActivityWeatherBinding
import com.example.weatherandmap.view_model.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWeatherBinding
    lateinit var weatherViewModel: WeatherViewModel
    private var latitude = 0.0
    private var longitude = 0.0
    private var API_KEY = "ca8bd2acf1601d58d317eddd152004b5"
    private val TAG = "WeatherActivity"

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        if (bundle != null) {
            latitude = bundle.getDouble("latitude")
            Log.d(TAG, "onCreate: $latitude")
            longitude = bundle.getDouble("longitude")
            Log.d(TAG, "onCreate: $longitude")
        } else Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel.getWeather(latitude,longitude,API_KEY).observe(this) {
            Log.d(TAG, "onCreate: $it")
            binding.apply {
                progress.visibility = View.INVISIBLE
                address.text = it.name

                when (it.weather!![0].main) {
                    "Clouds" -> imageView.setImageResource(R.drawable.ic_cloud)
                    "Clear" -> imageView.setImageResource(R.drawable.ic_sun)
                    "Rain" -> imageView.setImageResource(R.drawable.ic_rainy)
                    "Snow" -> imageView.setImageResource(R.drawable.ic_snow)
                    "Smoke" -> imageView.setImageResource(R.drawable.ic_smoke)
                }

                temp.text = "${(it.main?.temp?.minus(273.15)?.toInt())}\u00B0"

                val date1 = Date((it.sys?.sunset?.plus(it.timezone!!))!!.toLong())

                val format1 = SimpleDateFormat("HH:mm").format(date1)

                Log.d(TAG, "onCreate: time $format1")

                backBtn.setOnClickListener { finish() }
            }
        }
    }
}
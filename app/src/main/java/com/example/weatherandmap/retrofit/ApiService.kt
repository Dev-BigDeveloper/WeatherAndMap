package com.example.weatherandmap.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.weatherandmap.models.Weather

interface ApiService {

    @GET("weather")
    fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") API_KEY: String,
    ): Call<Weather>
}
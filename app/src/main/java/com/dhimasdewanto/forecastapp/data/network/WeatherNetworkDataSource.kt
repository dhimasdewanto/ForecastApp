package com.dhimasdewanto.forecastapp.data.network

import androidx.lifecycle.LiveData
import com.dhimasdewanto.forecastapp.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather : LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        units: Char
    )
}
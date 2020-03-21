package com.dhimasdewanto.forecastapp.data.provider

import com.dhimasdewanto.forecastapp.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}
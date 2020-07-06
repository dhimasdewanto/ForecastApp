package com.dhimasdewanto.forecastapp.data.network.response

import com.dhimasdewanto.forecastapp.data.db.entity.CurrentWeatherEntry
import com.dhimasdewanto.forecastapp.data.db.entity.Request
import com.dhimasdewanto.forecastapp.data.db.entity.WeatherLocation

data class FutureWeatherResponse (
    val futureWeatherEntry: List<CurrentWeatherEntry>,
    val location: WeatherLocation,
    val request: Request
)
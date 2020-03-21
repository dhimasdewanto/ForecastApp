package com.dhimasdewanto.forecastapp.data.provider

import com.dhimasdewanto.forecastapp.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "New York"
    }
}
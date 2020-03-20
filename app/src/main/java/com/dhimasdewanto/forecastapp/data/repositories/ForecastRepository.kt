package com.dhimasdewanto.forecastapp.data.repositories

import androidx.lifecycle.LiveData
import com.dhimasdewanto.forecastapp.data.db.entity.CurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<CurrentWeatherEntry>
}
package com.dhimasdewanto.forecastapp.ui.weather.current

import androidx.lifecycle.ViewModel
import com.dhimasdewanto.forecastapp.internal.UnitSystems
import com.dhimasdewanto.forecastapp.data.repositories.ForecastRepository
import com.dhimasdewanto.forecastapp.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    // Get from settings later.
    private val unitSystems = UnitSystems.METRIC

    val isMetric: Boolean
        get() = unitSystems == UnitSystems.METRIC

    /**
     * Create weather only after it needed.
     */
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}

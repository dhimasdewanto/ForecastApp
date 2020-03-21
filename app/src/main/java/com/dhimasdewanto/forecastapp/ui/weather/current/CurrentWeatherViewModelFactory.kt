package com.dhimasdewanto.forecastapp.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhimasdewanto.forecastapp.data.provider.UnitProvider
import com.dhimasdewanto.forecastapp.data.repositories.ForecastRepository

class CurrentWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel?> create(modelClass: Class<T>) : T {
        return CurrentWeatherViewModel(forecastRepository, unitProvider) as T
    }
}
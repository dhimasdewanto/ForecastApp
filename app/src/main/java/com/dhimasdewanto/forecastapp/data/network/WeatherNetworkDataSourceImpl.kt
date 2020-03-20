package com.dhimasdewanto.forecastapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhimasdewanto.forecastapp.data.network.response.CurrentWeatherResponse
import com.dhimasdewanto.forecastapp.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try {
            val fetchCurrentWeather = weatherApiService
                .getCurrentWeather(location)
                .await()
            _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No Internet Connection.", e)
        }
    }
}
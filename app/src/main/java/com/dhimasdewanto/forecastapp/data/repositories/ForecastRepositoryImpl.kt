package com.dhimasdewanto.forecastapp.data.repositories

import androidx.lifecycle.LiveData
import com.dhimasdewanto.forecastapp.data.db.CurrentWeatherDao
import com.dhimasdewanto.forecastapp.data.db.entity.CurrentWeatherEntry
import com.dhimasdewanto.forecastapp.internal.UnitSystems
import com.dhimasdewanto.forecastapp.data.network.WeatherNetworkDataSource
import com.dhimasdewanto.forecastapp.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

/***
 * Repository doesn't have lifecycle.
 */
class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever {newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(isMetric: Boolean): LiveData<CurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData(isMetric)
            when (isMetric) {
                true -> return@withContext currentWeatherDao.getWeatherMetric()
                false -> return@withContext currentWeatherDao.getWeatherImperial()
            }
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData(isMetric: Boolean) {
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1))) {
            fetchCurrentWeather(isMetric)
        }
    }

    private suspend fun fetchCurrentWeather(isMetric: Boolean) {
        var units = UnitSystems.IMPERIAL.units
        if (isMetric) {
            units = UnitSystems.METRIC.units
        }

        weatherNetworkDataSource.fetchCurrentWeather("New York", units)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}
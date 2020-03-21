package com.dhimasdewanto.forecastapp.data.repositories

import androidx.lifecycle.LiveData
import com.dhimasdewanto.forecastapp.data.db.CurrentWeatherDao
import com.dhimasdewanto.forecastapp.data.db.WeatherLocationDao
import com.dhimasdewanto.forecastapp.data.db.entity.CurrentWeatherEntry
import com.dhimasdewanto.forecastapp.data.db.entity.WeatherLocation
import com.dhimasdewanto.forecastapp.internal.UnitSystems
import com.dhimasdewanto.forecastapp.data.network.WeatherNetworkDataSource
import com.dhimasdewanto.forecastapp.data.network.response.CurrentWeatherResponse
import com.dhimasdewanto.forecastapp.data.provider.LocationProvider
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
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
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

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData(isMetric: Boolean) {
        val lastLocation = weatherLocationDao.getLocation().value

        if (lastLocation == null || locationProvider.hasLocationChanged(lastLocation)) {
            fetchCurrentWeather(isMetric)
            return
        }

        if (isFetchCurrentNeeded(lastLocation.zonedDateTime)) {
            fetchCurrentWeather(isMetric)
        }
    }

    private suspend fun fetchCurrentWeather(isMetric: Boolean) {
        var units = UnitSystems.IMPERIAL.units
        if (isMetric) {
            units = UnitSystems.METRIC.units
        }

        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            units
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}
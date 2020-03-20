package com.dhimasdewanto.forecastapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhimasdewanto.forecastapp.data.db.entity.CURRENT_WEATHER_ID
import com.dhimasdewanto.forecastapp.data.db.entity.CurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Because only one weather entry
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query(value = "SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<CurrentWeatherEntry>

    /**
     * Not available for now.
     */
    @Query(value = "SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<CurrentWeatherEntry>
}
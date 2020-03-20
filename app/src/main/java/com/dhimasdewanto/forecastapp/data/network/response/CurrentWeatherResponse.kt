package com.dhimasdewanto.forecastapp.data.network.response

import com.dhimasdewanto.forecastapp.data.db.entity.CurrentWeatherEntry
import com.dhimasdewanto.forecastapp.data.db.entity.Location
import com.dhimasdewanto.forecastapp.data.db.entity.Request
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location,
    val request: Request
)
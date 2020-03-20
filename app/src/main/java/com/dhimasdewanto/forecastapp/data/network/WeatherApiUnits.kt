package com.dhimasdewanto.forecastapp.data.network

enum class WeatherApiUnits(val units: Char) {
    METRIC('m'),
    SCIENTIFIC('s'),
    IMPERIAL('f')
}
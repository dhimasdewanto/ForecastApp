package com.dhimasdewanto.forecastapp.data.provider

import com.dhimasdewanto.forecastapp.internal.UnitSystems

interface UnitProvider {
    fun getUnitSystem(): UnitSystems
}
package com.dhimasdewanto.forecastapp.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.dhimasdewanto.forecastapp.internal.UnitSystems

const val UNIT_SYSTEM = "UNIT_SYSTEM"

class UnitProviderImpl(context: Context) : UnitProvider {
    private val appContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    override fun getUnitSystem(): UnitSystems {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystems.METRIC.name)
        return UnitSystems.valueOf(selectedName!!)
    }
}
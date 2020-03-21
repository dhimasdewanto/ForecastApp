package com.dhimasdewanto.forecastapp

import android.app.Application
import com.dhimasdewanto.forecastapp.data.db.ForecastDatabase
import com.dhimasdewanto.forecastapp.data.network.*
import com.dhimasdewanto.forecastapp.data.provider.UnitProvider
import com.dhimasdewanto.forecastapp.data.provider.UnitProviderImpl
import com.dhimasdewanto.forecastapp.data.repositories.ForecastRepository
import com.dhimasdewanto.forecastapp.data.repositories.ForecastRepositoryImpl
import com.dhimasdewanto.forecastapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider {CurrentWeatherViewModelFactory(instance(), instance())}
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
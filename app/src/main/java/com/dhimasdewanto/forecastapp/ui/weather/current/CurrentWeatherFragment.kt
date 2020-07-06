package com.dhimasdewanto.forecastapp.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dhimasdewanto.forecastapp.R
import com.dhimasdewanto.forecastapp.data.db.entity.CurrentWeatherEntry
import com.dhimasdewanto.forecastapp.internal.glide.GlideApp
import com.dhimasdewanto.forecastapp.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopeFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
        updateLocationAppBar("Loading")

        bindUI()
    }

    private fun bindUI() = launch {
        group_loading.visibility = View.VISIBLE
        group_show.visibility = View.GONE

        val currentWeather = viewModel.weather.await()
        val currentLocation = viewModel.weatherLocation.await()

        currentLocation.observe(viewLifecycleOwner, Observer {location ->
            if (location == null) return@Observer
            updateLocationAppBar(location.name)
        })

        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            group_show.visibility = View.VISIBLE
            updateTemperature(it.temperature, it.feelslike)
            updateCondition(it.weatherDescriptions[0])
            updateDetails(it)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(image_weather)
        })
    }

    private fun updateDetails(it: CurrentWeatherEntry) {
        text_view_pressure.text = "${it.pressure} milibar"
        text_view_humidity.text = "${it.humidity}%"
        text_view_wind.text = "${it.windDir} | ${it.windSpeed} kmh"
        text_view_visibility.text = "${it.visibility} km"
    }

    private fun updateTemperature(temperature: Int, feelsLike: Int) {
        val unitAbbreviation = if (viewModel.isMetric) "C" else "F"
        text_view_temp.text = "$temperature°$unitAbbreviation"
        text_view_feels.text = "Feels like $feelsLike°$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        text_view_condition.text = condition
    }

    private fun updateLocationAppBar(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

}

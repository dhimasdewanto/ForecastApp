package com.dhimasdewanto.forecastapp.ui.weather.current

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.dhimasdewanto.forecastapp.R
import com.dhimasdewanto.forecastapp.data.WeatherApiService
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() =
            CurrentWeatherFragment()
    }

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)

        // TODO: Only TEMP
        val apiService = WeatherApiService()
        GlobalScope.launch(Dispatchers.Main) {
            val currentWeatherResponse = apiService.getCurrentWeather("New York").await()
            current_text_view.text = currentWeatherResponse.toString()
        }
    }

}

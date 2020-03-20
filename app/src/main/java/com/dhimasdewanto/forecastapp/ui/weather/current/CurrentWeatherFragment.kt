package com.dhimasdewanto.forecastapp.ui.weather.current

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.dhimasdewanto.forecastapp.R
import com.dhimasdewanto.forecastapp.data.network.ConnectivityInterceptorImpl
import com.dhimasdewanto.forecastapp.data.network.WeatherApiService
import com.dhimasdewanto.forecastapp.data.network.WeatherNetworkDataSource
import com.dhimasdewanto.forecastapp.data.network.WeatherNetworkDataSourceImpl
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

        val apiService =
            WeatherApiService(ConnectivityInterceptorImpl(this.context!!))
        val weatherNetworkDataSource = WeatherNetworkDataSourceImpl(apiService)

        weatherNetworkDataSource.downloadedCurrentWeather.observe(viewLifecycleOwner, Observer {
            current_text_view.text = it.toString()
        })

        GlobalScope.launch(Dispatchers.Main) {
            weatherNetworkDataSource.fetchCurrentWeather("London")
        }
    }

}

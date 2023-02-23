package com.example.khWeather_kt.ui.forecast

import com.example.khWeather_kt.ui.home.WeatherCity

data class WeatherForecast (
    val cod: String,
    val message: String,
    val cnt: String,
    val list: List<WeatherCity>,
    val city: City
) {
    data class City (
        val id: String,
        val name: String,
        val coord: WeatherCity.Coord,
        val country: String,
        val population: String,
        val timezone: String,
        val sunrise: String,
        val sunset: String
    )
}

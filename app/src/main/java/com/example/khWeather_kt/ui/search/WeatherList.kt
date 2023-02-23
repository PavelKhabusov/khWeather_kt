package com.example.khWeather_kt.ui.search

import com.example.khWeather_kt.ui.home.WeatherCity

data class WeatherList (
    val message: String,
    val cod: String,
    val count: Long,
    val list: List<WeatherCity>
)

package com.example.lab_1.ui.search

import com.example.lab_1.ui.home.WeatherCity

data class WeatherList (
    val message: String,
    val cod: String,
    val count: Long,
    val list: List<WeatherCity>
)

package com.example.lab_1

import com.example.lab_1.ui.home.WeatherCity
import com.example.lab_1.ui.forecast.WeatherForecast
import com.example.lab_1.ui.search.WeatherList
import retrofit2.http.*


interface APIService {

    @GET("weather?")
    suspend fun getWeather(
//        @Query("q") q: String?,
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("units") units: String?,
        @Query("appid") appid: String?,
        @Query("lang") lang: String?
    ): WeatherCity

    @GET("find?")
    suspend fun searchWeather(
        @Query("q") q: String?,
        @Query("units") units: String?,
        @Query("type") type: String?,
        @Query("sort") sort: String?,
        @Query("cnt") cnt: String?,
        @Query("appid") appid: String?,
        @Query("lang") lang: String?
    ): WeatherList

    @GET("forecast?")
    suspend fun forecastWeather(
//        @Query("q") q: String?,
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("units") units: String?,
        @Query("cnt") cnt: String?,
        @Query("appid") appid: String?,
        @Query("lang") lang: String?
    ): WeatherForecast
}


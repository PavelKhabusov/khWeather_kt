package com.example.khWeather_kt.ui.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.khWeather_kt.ui.home.WeatherCity
import com.google.gson.Gson

class ForecastAdapter(fragment: ForecastFragment, val list: List<WeatherCity>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5
    private val days = arrayOf(
        list.slice(0..7),
        list.slice(8..15),
        list.slice(16..23),
        list.slice(24..31),
        list.slice(32..39)
    )

    override fun createFragment(position: Int): Fragment {
        val fragment = DayFragment()
        fragment.arguments = Bundle().apply {
            putString(ARG_OBJECT, Gson().toJson(days[position]))
        }
        return fragment
    }
}
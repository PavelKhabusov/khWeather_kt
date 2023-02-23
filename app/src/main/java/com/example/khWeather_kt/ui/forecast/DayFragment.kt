package com.example.khWeather_kt.ui.forecast

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.khWeather_kt.R
import com.example.khWeather_kt.appendAnyDbl
import com.example.khWeather_kt.appendStrHH
import com.example.khWeather_kt.ui.home.WeatherCity
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartSymbolType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

const val ARG_OBJECT = "object"

class DayFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.row_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chartView: AAChartView = view.findViewById(R.id.aa_chart_view)
        val dayWeek: TextView = view.findViewById(R.id.dayWeek)
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val listOfMyClassObject: Type = object : TypeToken<List<WeatherCity>>() {}.type
            val dayOne: List<WeatherCity> = Gson().fromJson(
                getString(ARG_OBJECT),
                listOfMyClassObject
            )
            var temps = arrayOf<Any>()
            var tempsMin = arrayOf<Any>()
            var tempsMax = arrayOf<Any>()
            var time = arrayOf<String>()

            for (weatherCity in dayOne) {
                temps = appendAnyDbl(temps, weatherCity.main.temp)
                tempsMin = appendAnyDbl(temps, weatherCity.main.temp_min)
                tempsMax = appendAnyDbl(temps, weatherCity.main.temp_max)
                time = appendStrHH(time, weatherCity.dt)
            }
            tempsMin = tempsMin.copyOfRange(0, tempsMin.lastIndex)
            tempsMax = tempsMax.copyOfRange(0, tempsMax.lastIndex)

            val aaChartModel : AAChartModel = AAChartModel()
                .chartType(AAChartType.Spline)
                .axesTextColor("#FBC16C")
                .backgroundColor(Color.TRANSPARENT)
                .dataLabelsEnabled(false)
                .yAxisTitle("")
                .colorsTheme(arrayOf("#17241d","#978c52","#FBC16C"))
                .markerSymbol(AAChartSymbolType.Circle)
                .yAxisGridLineWidth(0f)
                .categories(time)
                .series(arrayOf(
                    AASeriesElement()
                        .name("Мин")
                        .data(tempsMin),
                    AASeriesElement()
                        .name("Макс")
                        .data(tempsMax),
                    AASeriesElement()
                        .name("Температура")
                        .data(temps),
                ) )
            chartView.aa_drawChartWithChartModel(aaChartModel)

            dayWeek.text = SimpleDateFormat(
                "EEEE, dd MMMM",
                Locale.getDefault()
            ).format(Date(dayOne[0].dt * 1000))
        }
    }
}
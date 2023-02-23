package com.example.lab_1.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.lab_1.R
import com.example.lab_1.loadImage
import com.example.lab_1.roundToStr
import com.example.lab_1.ui.home.WeatherCity
import org.json.JSONException
import java.util.*

class SearchAdapter(
    private var context: Context,
    private var listLayout: Int,
    field: Int,
    private var list: List<WeatherCity>
) :
    ArrayAdapter<WeatherCity?>(context, listLayout, field, list as List<WeatherCity?>) {
    @SuppressLint("SetTextI18n", "DiscouragedApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        @SuppressLint("ViewHolder") val itemView = inflater.inflate(listLayout, parent, false)
        val address = itemView.findViewById<TextView>(R.id.date)
        val temp = itemView.findViewById<TextView>(R.id.temp)
//        val tempMin = itemView.findViewById<TextView>(R.id.temp_min)
//        val tempMax = itemView.findViewById<TextView>(R.id.temp_max)
        val weatherIcon = itemView.findViewById<ImageView>(R.id.weather_icon)
        val flagIcon = itemView.findViewById<ImageView>(R.id.flag_icon)
        try {
            val res = list[position]

            address.text = list[position].name + ", " + res.sys.country
            temp.text = roundToStr(res.main.temp) + "°C"
//            tempMin.text = "Мин: " + roundToStr(res.main.temp_min) + "°"
//            tempMax.text = "Макс: " + roundToStr(res.main.temp_max) + "°"

//            val icon = res.weather[0].icon.replace('n', 'd')
//            val iconUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
//            loadImage(weatherIcon, iconUrl)
            val mDrawableName = "ic_${res.weather[0].icon.replace('n', 'd')}"
            val resID = context.resources.getIdentifier(mDrawableName, "drawable", context.packageName)
            weatherIcon.setImageResource(resID)
            val flag = "http://openweathermap.org/images/flags/${res.sys.country.lowercase(Locale.ROOT)}.png"
            loadImage(flagIcon, flag)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return itemView
    }
}

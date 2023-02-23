package com.example.khWeather_kt

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.math.round

fun loadImage(view: View, imageUrl: String?) {
    val image: ImageView = view as ImageView
    Picasso.get()
        .load(imageUrl)
        .into(image)
}
fun roundToStr(str: Double) : String {
    return round(str).toInt().toString()
}
fun appendAnyDbl(arr: Array<Any>, element: Double): Array<Any> {
    val list: MutableList<Any> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}
fun appendStrHH(arr: Array<String>, element: Long): Array<String> {
    val list: MutableList<String> = arr.toMutableList()
    list.add(SimpleDateFormat("HH", Locale.getDefault()).format(Date(element * 1000)))
    return list.toTypedArray()
}
fun appendLngLng(arr: Array<Long>, element: Long): Array<Long> {
    val list: MutableList<Long> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}
fun appendStrStr(arr: Array<String>, element: String): Array<String> {
    val list: MutableList<String> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}
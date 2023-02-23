package com.example.khWeather_kt.ui.forecast

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.khWeather_kt.*
import com.example.khWeather_kt.MainActivity.Companion.userLat
import com.example.khWeather_kt.MainActivity.Companion.userLon
import com.example.khWeather_kt.databinding.FragmentForecastBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ForecastFragment : Fragment() {
    private var _binding: FragmentForecastBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForecastBinding.inflate(inflater, container, false )

        val root: View = binding.root

        onPreExecute()
        onPostExecute(execute())

        return root
    }

    private fun execute(): WeatherForecast = runBlocking {
        val deferred: Deferred<WeatherForecast> = async {
            // Use API key
            val api = BuildConfig.WEATHER_API_KEY
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)

            retrofit.forecastWeather(
                lat = userLat,
                lon = userLon,
                units = "metric",
                cnt = "40",
                appid = api,
                lang = "ru"
            )
        }

        runBlocking {
            deferred.await()
        }
    }

    // Runs on the Main(UI) Thread
    @SuppressLint("SetTextI18n")
    private fun onPostExecute(res: WeatherForecast) {
        try {
            val current = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM"))
            binding.titleForecast.text = "Прогноз погоды от $current"

            val viewPager: ViewPager2 = binding.pager
            viewPager.adapter = activity?.let {
                ForecastAdapter(
                    this,
                    res.list
                )
            }
            var day = arrayOf<Long>()

            for (d in 1 until res.list.size step 8) {
                day = appendLngLng(day, res.list[d].dt)
            }
            var tabNames = arrayOf<String>()
//            val tabNumbers = arrayOf<Int>()
            for(d in day) {
                val dayWeek = SimpleDateFormat(
                    "EE,dd",
                    Locale.getDefault()
                ).format(Date(d * 1000)).split(",").toTypedArray()
                tabNames = appendStrStr(tabNames, dayWeek[0]  + "\n" + dayWeek[1])
            }
//            val tabNumbers: Array<Int> = arrayOf(
//                R.drawable.baseline_looks_one_black_48,
//                R.drawable.baseline_looks_two_black_48,
//                R.drawable.baseline_looks_3_black_48,
//                R.drawable.baseline_looks_4_black_48,
//                R.drawable.baseline_looks_5_black_48
//            )
            val tabLayout = binding.tabLayout
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabNames[position]
//                tab.setIcon(tabNumbers[position])
            }.attach()
//            val tabCount: Int = tabLayout.tabCount
//
//            fun setImageButtonStateNew(mContext: Context): StateListDrawable {
//                val states = StateListDrawable()
//                states.addState(intArrayOf(android.R.attr.state_selected), ContextCompat.getDrawable(mContext, R.drawable.tab_active))
//                states.addState(intArrayOf(-android.R.attr.state_selected), ContextCompat.getDrawable(mContext, R.drawable.tab_bg))
//
//                return states
//            }
//            for (i in 0 until tabCount) {
//                val tabView: View = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
//                tabView.requestLayout()
//                ViewCompat.setBackground(tabView,setImageButtonStateNew(requireContext()));
//                ViewCompat.setPaddingRelative(tabView, tabView.paddingStart, tabView.paddingTop, tabView.paddingEnd, tabView.paddingBottom);
//            }


            /* Views populated, Hiding the loader, Showing the main design */
            binding.loader.visibility = View.GONE
            binding.mainContainer.visibility = View.VISIBLE

        } catch (e: Exception) {
            binding.loader.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
            val error = "Error:\n$e"
            binding.errorText.text = error
        }
    }

    private fun onPreExecute() {
        /* Showing the ProgressBar, Making the main design GONE */
        binding.loader.visibility = View.VISIBLE
        binding.mainContainer.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


package com.example.khWeather_kt.ui.home

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.khWeather_kt.*
import com.example.khWeather_kt.MainActivity.Companion.userLat
import com.example.khWeather_kt.MainActivity.Companion.userLon
import com.example.khWeather_kt.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false )

        val root: View = binding.root

        onPreExecute()
        onPostExecute(execute())
        binding.imageButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.navigation_search)
        }

        return root
    }

        private fun execute(): WeatherCity = runBlocking {
            val deferred: Deferred<WeatherCity> = async {
                // Use API key
                val api = BuildConfig.WEATHER_API_KEY
                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(APIService::class.java)

                retrofit.getWeather(lat = userLat, lon = userLon, units = "metric", appid = api, lang = "ru")
            }

            runBlocking {
                deferred.await()
            }
        }

        // Runs on the Main(UI) Thread
        @SuppressLint("SetTextI18n", "DiscouragedApi")
        private fun onPostExecute(res : WeatherCity) {
            try {
                /* Extracting JSON returns from the API */
                val mDrawableName = "ic_${res.weather[0].icon.replace('n', 'd')}"
                val resID = resources.getIdentifier(mDrawableName, "drawable", context?.packageName)
                binding.weatherIcon.setImageResource(resID)

                /* Populating extracted data into our views */
                binding.address.text = res.name //+ ", " + res.sys.country
                binding.updatedAt.text = "Обновлено: " + SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date(res.dt * 1000))
                binding.status.text = res.weather[0].description.replaceFirstChar(Char::titlecase)
                binding.temp.text = roundToStr(res.main.temp) + "°C"
                binding.feelsLike.text = "Ощущается как: " + roundToStr(res.main.feels_like) + "°"
                binding.tempMin.text = "Мин: " + roundToStr(res.main.temp_min) + "°"
                binding.tempMax.text = "Макс: " + roundToStr(res.main.temp_max) + "°"
                binding.sunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(res.sys.sunrise * 1000))
                binding.sunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(res.sys.sunset * 1000))
                binding.wind.text = res.wind.speed.toString() + " м/с"
                binding.pressure.text = res.main.pressure.toString() + " мм рт ст"
                binding.humidity.text = res.main.humidity.toString() + "%"

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

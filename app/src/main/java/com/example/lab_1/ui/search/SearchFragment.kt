package com.example.lab_1.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.lab_1.APIService
import com.example.lab_1.BuildConfig
import com.example.lab_1.MainActivity.Companion.userLat
import com.example.lab_1.MainActivity.Companion.userLon
import com.example.lab_1.R
import com.example.lab_1.databinding.FragmentSearchBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false )

        val root: View = binding.root

        val searchInput = binding.search
        searchInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val city = binding.search.text.toString()
                onPreExecute()
                onPostExecute(searchMethod(city))
                val imm: InputMethodManager =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else false
        }

        return root
    }

    private fun searchMethod(search : String): WeatherList = runBlocking {
        val deferred: Deferred<WeatherList> = async {
            // Use API key
            val api = BuildConfig.WEATHER_API_KEY
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)

            retrofit.searchWeather(
                q = search,
                units = "metric",
                type = "like",
                sort = "population",
                cnt = "30",
                appid = api,
                lang = "ru"
            )
        }

        runBlocking {
            deferred.await()
        }
    }

    // Runs on the Main(UI) Thread
    private fun onPostExecute(res: WeatherList) {
        try {
            binding.searchView.adapter = activity?.let {
                SearchAdapter(
                    it,
                    R.layout.row_search,
                    R.id.date,
                    res.list
                )
            }
            binding.searchView.setOnItemClickListener { _, _, _, id ->
                userLat = res.list[id.toInt()].coord.lat.toString()
                userLon = res.list[id.toInt()].coord.lon.toString()
                view?.findNavController()?.navigate(R.id.navigation_home)
            }

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

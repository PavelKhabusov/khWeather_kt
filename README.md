# khWeather_kt
 Kotlin Weather app for Android, demonstrating the use of Retrofit and implementing MVP.

<!-- <a href="https://play.google.com/store/apps/details?id=app.com.thetechnocafe.kotlinweather">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a> -->

# Screenshots
<p align="center" float="left">
  <img src="https://github.com/PavelKhabusov/khWeather_kt/blob/main/screenshots/screenshot_1.png" width="350" alt="scr_1"/> 
  <img src="https://github.com/PavelKhabusov/khWeather_kt/blob/main/screenshots/screenshot_2.png" width="350" alt="scr_2"/> 
</p>

# Overview
 Weather app created using the [OpenWeatherMap Api](https://openweathermap.org/).
 This is not supposed to be a production scale application, it is meant to demonstrate the implementation of **MVP** architecture in Kotlin using following libraries:
* Retrofit

##### Still if you have any issues or suggestions, please feel free to open an [issue](https://github.com/PavelKhabusov/khWeather_kt/issues/new)

### General flow of data
* Check if there is cached data present in the internal file, if yes then load the cached data.
* Retrieve the latitude and longitude of the user.
* Request data from Weather Api
* If data received, cache it in internal file and show the updated data to user.
* If error then notify user about it.

### Build the Project
To build the project on your own follow these steps:
* Clone the project
* Get an API key from [OpenWeatherMap Api](https://openweathermap.org/)
* Go to `gradle.properties` and change a variable `WEATHER_API_KEY` that contains the key that you got from the OpenWeatherMap Api.
```kotlin
serverUrl="http://api.openweathermap.org/data/2.5/";
WEATHER_API_KEY ="your_api_key";
```
* Build the project!
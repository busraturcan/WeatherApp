
package com.example.weather.data.remote

import com.example.weather.data.model.CitySearchResponse
import com.example.weather.data.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WeatherApi {

    @GET
    suspend fun searchCity(
        @Url url: String = "https://geocoding-api.open-meteo.com/v1/search",
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "tr"
    ): CitySearchResponse

    @GET
    suspend fun getWeather(
        @Url url: String = "https://api.open-meteo.com/v1/forecast",
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min",
        @Query("timezone") timezone: String = "auto"
    ): ForecastResponse
}

package com.example.weather.data.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("current_weather") val currentWeather: CurrentWeatherDto?,
    @SerializedName("daily") val daily: DailyDto?
)

data class CurrentWeatherDto(
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("windspeed") val windSpeed: Double,
    @SerializedName("weathercode") val weatherCode: Int
)

data class DailyDto(
    @SerializedName("temperature_2m_max") val maxTemp: List<Double>?,
    @SerializedName("temperature_2m_min") val minTemp: List<Double>?
)

// UI Katmanında hem liste hem detay için kullanılacak model
data class CityWeatherItem(
    val id: Long,
    val cityName: String,
    val locationDetail: String,
    val temperature: String,
    val conditionDescription: String,
    val weatherCode: Int,
    val windSpeed: String,
    val maxTemp: String,
    val minTemp: String
)
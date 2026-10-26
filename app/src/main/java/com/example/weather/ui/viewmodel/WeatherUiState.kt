package com.example.weather.ui.viewmodel

import com.example.weather.data.model.CityWeatherItem

sealed interface WeatherUiState {
    object Idle : WeatherUiState
    object Loading : WeatherUiState
    data class Success(val items: List<CityWeatherItem>) : WeatherUiState
    object Empty : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}
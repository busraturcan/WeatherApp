
package com.example.weather.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.CityDto
import com.example.weather.data.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val _citySuggestions =
        MutableStateFlow<List<CityDto>>(emptyList())
    val citySuggestions: StateFlow<List<CityDto>> = _citySuggestions

    private var searchJob: Job? = null
    private var weatherJob: Job? = null
    private var lastQuery = ""

    init {
        loadDefaultCities()
    }

    fun loadDefaultCities() {
        lastQuery = ""
        searchJob?.cancel()
        weatherJob?.cancel()
        _citySuggestions.value = emptyList()

        weatherJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val results = repository.getDefaultCitiesWeather()
                _uiState.value = WeatherUiState.Success(results)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    "Varsayılan şehirler yüklenemedi. İnterneti kontrol edin."
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        lastQuery = query.trim()
        searchJob?.cancel()
        _citySuggestions.value = emptyList()

        if (lastQuery.isBlank()) {
            loadDefaultCities()
            return
        }

        if (lastQuery.length < 2) return

        val queryToSearch = lastQuery

        searchJob = viewModelScope.launch {
            delay(350)

            try {
                val results =
                    repository.searchCitySuggestions(queryToSearch)

                if (lastQuery == queryToSearch) {
                    _citySuggestions.value = results
                }
            } catch (e: Exception) {
                if (lastQuery == queryToSearch) {
                    _citySuggestions.value = emptyList()
                }
            }
        }
    }

    fun selectCity(city: CityDto) {
        searchJob?.cancel()
        _citySuggestions.value = emptyList()
        lastQuery = city.name
        weatherJob?.cancel()

        weatherJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val weather = repository.getWeatherForCity(city)
                _uiState.value = WeatherUiState.Success(listOf(weather))
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    "Seçilen şehrin hava durumu alınamadı."
                )
            }
        }
    }


    fun searchCity(query: String) {
        val cleanedQuery = query.trim()

        if (cleanedQuery.isBlank()) {
            loadDefaultCities()
            return
        }

        searchJob?.cancel()
        weatherJob?.cancel()

        weatherJob = viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val cities = repository.searchCitySuggestions(cleanedQuery)

                if (cities.isEmpty()) {
                    _uiState.value = WeatherUiState.Empty
                    return@launch
                }

                val city = cities.first()
                _citySuggestions.value = emptyList()
                lastQuery = city.name

                val weather = repository.getWeatherForCity(city)
                _uiState.value = WeatherUiState.Success(listOf(weather))
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    "Şehir aranamadı. İnternet bağlantınızı kontrol edin."
                )
            }
        }
    }


    fun retry() {
        if (lastQuery.isNotBlank()) {
            onSearchQueryChanged(lastQuery)
        } else {
            loadDefaultCities()
        }
    }
}

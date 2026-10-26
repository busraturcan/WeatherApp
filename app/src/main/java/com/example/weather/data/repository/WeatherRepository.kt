
package com.example.weather.data.repository

import com.example.weather.data.model.CityDto
import com.example.weather.data.model.CityWeatherItem
import com.example.weather.data.remote.RetrofitClient
import com.example.weather.data.remote.WeatherApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class WeatherRepository(
    private val api: WeatherApi = RetrofitClient.api
) {

    private val defaultTurkishCities = listOf(
        Triple("İstanbul", 41.0082, 28.9784),
        Triple("Ankara", 39.9334, 32.8597),
        Triple("İzmir", 38.4192, 27.1287),
        Triple("Bursa", 40.1885, 29.0610),
        Triple("Antalya", 36.8969, 30.7133),
        Triple("Adana", 37.0000, 35.3213),
        Triple("Konya", 37.8667, 32.4833),
        Triple("Gaziantep", 37.0662, 37.3833),
        Triple("Trabzon", 41.0015, 39.7178),
        Triple("Eskişehir", 39.7767, 30.5206)
    )

    suspend fun getDefaultCitiesWeather(): List<CityWeatherItem> =
        coroutineScope {
            defaultTurkishCities.mapIndexed { index, (name, lat, lon) ->
                async {
                    fetchWeatherDetail(
                        id = (index + 1).toLong(),
                        name = name,
                        detail = "Türkiye",
                        lat = lat,
                        lon = lon
                    )
                }
            }.awaitAll()
        }

    suspend fun searchCitySuggestions(query: String): List<CityDto> {
        val cleanedQuery = query.trim()

        if (cleanedQuery.length < 2) {
            return emptyList()
        }

        return try {
            val response = api.searchCity(
                name = cleanedQuery,
                count = 10
            )
            response.results.orEmpty()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWeatherForCity(city: CityDto): CityWeatherItem {
        return fetchWeatherDetail(
            id = city.id,
            name = city.name,
            detail = listOfNotNull(
                city.region,
                city.country
            ).joinToString(", "),
            lat = city.latitude,
            lon = city.longitude
        )
    }

    private suspend fun fetchWeatherDetail(
        id: Long,
        name: String,
        detail: String,
        lat: Double,
        lon: Double
    ): CityWeatherItem {
        val forecast = try {
            api.getWeather(
                latitude = lat,
                longitude = lon
            )
        } catch (e: Exception) {
            null
        }

        val temp = forecast?.currentWeather?.temperature
        val code = forecast?.currentWeather?.weatherCode ?: -1
        val wind = forecast?.currentWeather?.windSpeed
        val maxT = forecast?.daily?.maxTemp?.getOrNull(0)
        val minT = forecast?.daily?.minTemp?.getOrNull(0)

        return CityWeatherItem(
            id = id,
            cityName = name,
            locationDetail = detail,
            temperature = if (temp != null) "${temp.toInt()}°C" else "--",
            conditionDescription = mapWeatherCode(code),
            weatherCode = code,
            windSpeed = if (wind != null) "${wind.toInt()} km/h" else "--",
            maxTemp = if (maxT != null) "${maxT.toInt()}°C" else "--",
            minTemp = if (minT != null) "${minT.toInt()}°C" else "--"
        )
    }

    private fun mapWeatherCode(code: Int): String {
        return when (code) {
            0 -> "Güneşli / Açık ☀️"
            1, 2, 3 -> "Parçalı Bulutlu ⛅"
            45, 48 -> "Sisli 🌫️"
            51, 53, 55 -> "Çiseleme 🌦️"
            61, 63, 65 -> "Yağmurlu 🌧️"
            71, 73, 75 -> "Karlı ❄️"
            95, 96, 99 -> "Fırtına ⛈️"
            else -> "Bulutlu ☁️"
        }
    }
}

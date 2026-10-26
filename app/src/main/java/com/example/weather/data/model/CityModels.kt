package com.example.weather.data.model

import com.google.gson.annotations.SerializedName

data class CitySearchResponse(
    @SerializedName("results") val results: List<CityDto>?
)

data class CityDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("country") val country: String?,
    @SerializedName("admin1") val region: String?
)

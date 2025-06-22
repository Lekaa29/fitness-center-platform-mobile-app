package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class FitnessCenter(
    @Json(name = "idFitnessCentar") val idFitnessCentar: Int,
    @Json(name = "distanceInMeters") val distanceInMeters: Double?,
    @Json(name = "promotion") val promotion: Int?,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "pictureLink") val pictureLink: String?,

    @Json(name = "name") val name: String
)


package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class FitnessCenter(
    @Json(name = "idFitnessCentar") val IdFitnessCentar: Int,
    @Json(name = "name") val Name: String
)
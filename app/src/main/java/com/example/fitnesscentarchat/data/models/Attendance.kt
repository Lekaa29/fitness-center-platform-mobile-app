package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class Attendance(
    @Json(name = "idAttendance") val idAttendance: Int,
    @Json(name = "userId") val userId: Int,
    @Json(name = "fitnessCentarId") val fitnessCentarId: Int,
    @Json(name = "timestamp") val timestamp: String?,
)
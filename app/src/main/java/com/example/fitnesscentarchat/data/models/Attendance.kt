package com.example.fitnesscentarchat.data.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class Attendance(
    @Json(name = "idAttendance") val IdAttendance: Int,
    @Json(name = "userId") val UserId: Int,
    @Json(name = "fitnessCentarId") val FitnessCentarId: Int,
    @Json(name = "timestamp") val Timestamp: String?,
)
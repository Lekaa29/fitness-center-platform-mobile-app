package com.example.fitnesscentarchat.data.models
import com.squareup.moshi.Json

data class User(
    @Json(name = "id") val Id: Int,
    @Json(name = "firstName") val FirstName: String,
    @Json(name = "lastName") val LastName: String,
    @Json(name = "pictureLink") val PictureLink: String? = null
)
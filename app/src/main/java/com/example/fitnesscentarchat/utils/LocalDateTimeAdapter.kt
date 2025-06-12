package com.example.fitnesscentarchat.utils


import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun fromJson(reader: JsonReader): LocalDateTime? {
        val dateString = reader.nextString()
        return LocalDateTime.parse(dateString, formatter)
    }

    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        if (value != null) {
            writer.value(formatter.format(value))
        } else {
            writer.nullValue()
        }
    }
}
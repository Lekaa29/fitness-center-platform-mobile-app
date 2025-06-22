package com.example.fitnesscentarchat.data.models

sealed class UploadProgress {
    object Starting : UploadProgress()
    data class Uploading(val percentage: Int) : UploadProgress()
    object Complete : UploadProgress()
    data class Failed(val error: String) : UploadProgress()
}
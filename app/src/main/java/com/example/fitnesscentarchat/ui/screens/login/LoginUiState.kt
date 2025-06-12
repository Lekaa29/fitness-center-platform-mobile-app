package com.example.fitnesscentarchat.ui.screens.login


import com.example.fitnesscentarchat.data.models.User

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
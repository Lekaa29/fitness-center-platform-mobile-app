package com.example.fitnesscentarchat.ui.screens.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = authRepository.login(username, password)

            _uiState.value = result.fold(
                onSuccess = { loginResponse ->
                    // ✅ Save token

                    LoginUiState.Success(loginResponse)
                },
                onFailure = { LoginUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun register(username: String, password: String) {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = authRepository.register(username, password)

            _uiState.value = result.fold(
                onSuccess = { LoginUiState.Success(it) },
                onFailure = { LoginUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Initial
    }
}
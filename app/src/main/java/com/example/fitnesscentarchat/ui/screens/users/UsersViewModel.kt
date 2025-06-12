package com.example.fitnesscentarchat.ui.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        loadConversations()

        /*
        // Observe changes
        viewModelScope.launch {
            userRepository.getUsersFlow().collect { users ->
                _uiState.update { it.copy(users = users, isLoading = false) }
            }
        }

         */
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            userRepository.getUsers().fold(
                onSuccess = { users ->
                    _uiState.update { it.copy(users = users, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load users",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    fun loadConversations(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null)  }

            userRepository.getUsersConversations().fold(
                onSuccess = {conversations ->
                    _uiState.update { it.copy(conversations = conversations, isLoading = false) }

                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load users",
                            isLoading = false
                        )
            }
                }
            )

        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
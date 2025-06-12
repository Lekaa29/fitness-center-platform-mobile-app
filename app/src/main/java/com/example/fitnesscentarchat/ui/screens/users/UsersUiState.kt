package com.example.fitnesscentarchat.ui.screens.users

import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.models.Conversation

data class UsersUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
    val conversations: List<Conversation> = emptyList()
)
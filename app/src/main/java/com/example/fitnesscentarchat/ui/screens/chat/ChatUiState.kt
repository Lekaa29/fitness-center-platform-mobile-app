package com.example.fitnesscentarchat.ui.screens.chat

import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.models.User

data class ChatUiState(
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val participants: List<User>? = emptyList(),
    val messages: List<Message> = emptyList(),
    val totalUnread: Int? = null,
    val error: String? = null,
    val conversations: List<Conversation>? = emptyList()
){
    // Add explicit toString for debugging
    override fun toString(): String {
        return "ChatUiState(messages.size=${messages.size}, isLoading=$isLoading, isSending=$isSending, error=$error)"
    }
}

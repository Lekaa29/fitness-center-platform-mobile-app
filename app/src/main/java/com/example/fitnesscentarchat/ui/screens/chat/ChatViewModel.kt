package com.example.fitnesscentarchat.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.repository.MessageRepository
import com.example.fitnesscentarchat.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var messagesJob: Job? = null

    fun loadChat(conversationId: Int) {
        Log.d("VIEWMODEL", "loadChat called for conversation $conversationId")
        Log.d("VIEWMODEL", "Current uiState.messages.size = ${_uiState.value.messages.size}")

        if (messagesJob?.isActive == true) {
            Log.d("VIEWMODEL", "Messages job already active, returning")
            return
        }

        // Set loading state
        _uiState.value = _uiState.value.copy(isLoading = true)
        Log.d("VIEWMODEL", "Set loading = true, current state messages.size = ${_uiState.value.messages.size}")

        messagesJob = viewModelScope.launch {
            Log.d("VIEWMODEL", "Starting to collect messages flow")
            try {
                messageRepository.getMessagesFlow(conversationId)
                    .distinctUntilChanged()
                    .collect { messages ->
                        Log.d("VIEWMODEL", "=== RECEIVED ${messages.size} MESSAGES ===")
                        messages.forEachIndexed { index, message ->
                            Log.d("VIEWMODEL", "Message $index: ${message.text} (id=${message.id})")
                        }

                        val oldState = _uiState.value
                        Log.d("VIEWMODEL", "Before update - uiState.messages.size = ${oldState.messages.size}")

                        // Use direct assignment instead of update{}
                        val newState = oldState.copy(
                            messages = messages,
                            isLoading = false
                        )
                        _uiState.value = newState

                        Log.d("VIEWMODEL", "After assignment - newState.messages.size = ${newState.messages.size}")

                        // Verify the assignment worked
                        val verifyState = _uiState.value
                        Log.d("VIEWMODEL", "Final verification - uiState.messages.size = ${verifyState.messages.size}")
                        Log.d("VIEWMODEL", "StateFlow reference check: ${_uiState === _uiState}")

                        // Force emit by creating a completely new state object
                        _uiState.value = ChatUiState(
                            messages = messages,
                            isLoading = false,
                            isSending = oldState.isSending,
                            error = oldState.error,
                        )

                        Log.d("VIEWMODEL", "After force emit - uiState.messages.size = ${_uiState.value.messages.size}")
                    }
            } catch (e: Exception) {
                Log.e("VIEWMODEL", "Error collecting messages", e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun sendMessage(conversationId: Int, content: String) {
        Log.d("VIEWMODEL", "sendMessage called: $content")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)

            messageRepository.sendMessage(conversationId, content).fold(
                onSuccess = {
                    Log.d("VIEWMODEL", "Message sent successfully")
                    _uiState.value = _uiState.value.copy(isSending = false)
                },
                onFailure = { error ->
                    Log.e("VIEWMODEL", "Failed to send message", error)
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        error = "Failed to send: ${error.message}"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
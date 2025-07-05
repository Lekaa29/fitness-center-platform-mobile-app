package com.example.fitnesscentarchat.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.models.ShopItem
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


    private val _userConversations = MutableStateFlow<List<Conversation>>(emptyList())
    val userConversations: StateFlow<List<Conversation>> = _userConversations.asStateFlow()
    private val _isLoadingConversations = MutableStateFlow(false)
    val isLoadingConversations: StateFlow<Boolean> = _isLoadingConversations.asStateFlow()


    private var messagesJob: Job? = null


    fun loadChat(conversationId: Int) {

        Log.d("VIEWMODEL", "loadChat called for conversation $conversationId")
        Log.d("VIEWMODEL", "Current uiState.messages.size = ${_uiState.value.messages.size}")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (messagesJob?.isActive == true) {
                Log.d("VIEWMODEL", "Messages job already active, returning")
            }

            // Set loading state
            _uiState.update { it.copy(isLoading = true, error = null) }
            Log.d("VIEWMODEL", "Set loading = true, current state messages.size = ${_uiState.value.messages.size}")


            // Load participants first
            viewModelScope.launch {
                messageRepository.getConversationParticipants(conversationId).fold(
                    onSuccess = { participants ->
                        Log.d("VIEWMODEL", "Successfully loaded ${participants.size} participants")
                        _uiState.update {
                            it.copy(participants = participants)
                        }
                        // Start loading messages after participants are loaded
                        startLoadingMessages(conversationId)
                    },
                    onFailure = { error ->
                        Log.e("VIEWMODEL", "Failed to load participants", error)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Failed to load participants: ${error.message}"
                            )
                        }
                    }
                )
            }
        }
    }

    private fun startLoadingMessages(conversationId: Int) {
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

                        _uiState.update { currentState ->
                            currentState.copy(
                                messages = messages,
                                isLoading = false
                            )
                        }

                        Log.d("VIEWMODEL", "Updated state - messages.size = ${_uiState.value.messages.size}")
                    }
            } catch (e: Exception) {
                Log.e("VIEWMODEL", "Error collecting messages", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load messages: ${e.message}"
                    )
                }
            }
        }
    }

    fun loadConversation()
    {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            messageRepository.getUsersConversations().fold(
                onSuccess = { conversations ->
                    _uiState.update { it.copy(conversations = conversations, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Failed to load conversations",
                            isLoading = false
                        )
                    }
                }
            )
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

    fun refreshConversations() {
        Log.d("VIEWMODEL", "Getting conversations - start")
        _isLoadingConversations.value = true

        viewModelScope.launch {
            messageRepository.getUsersConversations().fold(
                onSuccess = { conversations ->
                    Log.d("VIEWMODEL", "Success getting ${conversations.size} conversations")
                    conversations.forEachIndexed { index, conv ->
                        Log.d("VIEWMODEL", "Conversation $index: ${conv.Title} (id=${conv.IdConversation})")
                    }
                    _userConversations.value = conversations
                    _isLoadingConversations.value = false
                },
                onFailure = { error ->
                    Log.e("VIEWMODEL", "Failed to get conversations: ${error.message}")
                    _isLoadingConversations.value = false
                    _uiState.value = _uiState.value.copy(error = "Failed to load conversations: ${error.message}")
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
package com.example.fitnesscentarchat.ui.screens.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.repository.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.ui.screens.chat.components.MessageChunkItem
import com.example.fitnesscentarchat.ui.screens.chat.components.groupMessagesIntoChunks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    authRepository: AuthRepository,
    conversationId: Int,
    userId: Int?,
    title: String,
    onNavigateBack: () -> Unit
) {
    Log.d("CHATSCREEN", "=== ChatScreen RECOMPOSITION ===")

    // Add explicit StateFlow collection logging
    val uiState by remember {
        Log.d("CHATSCREEN", "Creating StateFlow collection")
        viewModel.uiState
    }.collectAsStateWithLifecycle()

    Log.d("CHATSCREEN", "Collected uiState: $uiState")

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var messageText by remember { mutableStateOf("") }
    var currentUser by remember { mutableStateOf(101) }

    // Add debugging to LaunchedEffect
    LaunchedEffect(conversationId) {
        Log.d("CHATSCREEN", "LaunchedEffect - calling loadChat($conversationId)")
        currentUser = authRepository.getCurrentUser()?.id ?: 0
        viewModel.loadChat(conversationId)
    }

    // Debug the StateFlow updates
    LaunchedEffect(uiState) {
        Log.d("CHATSCREEN", "LaunchedEffect triggered by uiState change: $uiState")
    }

    // Use messages from uiState
    LaunchedEffect(uiState.messages.size) {
        Log.d("CHATSCREEN", "LaunchedEffect for scroll - messages.size = ${uiState.messages.size}")
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }



    val messageChunks = remember(uiState.messages) {
        groupMessagesIntoChunks(uiState.messages)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title ?: "Chat") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF020C41),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = Brush.linearGradient(
                    listOf(
                        Color(0xFF02092B),
                        Color(0xFF020105),
                    )
                ))
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.weight(1f)) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                uiState.messages.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "No messages yet. Start the conversation!",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(messageChunks) { chunk ->
                            val user = uiState.participants?.find { it.id == chunk.senderId }
                                ?: User(chunk.senderId, "", "", "Unknown", "", "", "")

                            MessageChunkItem(
                                chunk = chunk,
                                isFromCurrentUser = chunk.senderId == currentUser,
                                user = user
                            )
                        }
                    }
                }
            }

            // Input area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type a message") },
                    modifier = Modifier.weight(1f),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.6f)
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            // Handle sending message
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank() && !uiState.isSending
                ) {
                    if (uiState.isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White

                        )
                    }
                }
            }

            if (uiState.error != null) {
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(uiState.error!!)
                }
            }
        }
    }
}
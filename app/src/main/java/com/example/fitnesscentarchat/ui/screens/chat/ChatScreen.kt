package com.example.fitnesscentarchat.ui.screens.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.repository.AuthRepository
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    authRepository: AuthRepository,
    conversationId: Int,
    onNavigateBack: () -> Unit
) {
    Log.d("CHATSCREEN", "=== ChatScreen RECOMPOSITION ===")

    // Add explicit StateFlow collection logging
    val uiState by remember {
        Log.d("CHATSCREEN", "Creating StateFlow collection")
        viewModel.uiState
    }.collectAsStateWithLifecycle()

    Log.d("CHATSCREEN", "Collected uiState: $uiState")

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var messageText by remember { mutableStateOf("") }
    var currentUser by remember { mutableStateOf<Int?>(null) }

    // Add debugging to LaunchedEffect
    LaunchedEffect(conversationId) {
        Log.d("CHATSCREEN", "LaunchedEffect - calling loadChat($conversationId)")
        currentUser = authRepository.getCurrentUser()?.Id
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.chatPartner?.FirstName ?: "Chat") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Log.d("CHATSCREEN", "=== COMPOSE BODY === uiState: $uiState")

            when {
                uiState.isLoading -> {
                    Log.d("CHATSCREEN", "Showing loading indicator")
                    Box(modifier = Modifier.weight(1f)) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                uiState.messages.isEmpty() -> {
                    Log.d("CHATSCREEN", "Showing empty state - messages: ${uiState.messages}")
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
                    Log.d("CHATSCREEN", "Showing LazyColumn with ${uiState.messages.size} messages")
                    uiState.messages.forEachIndexed { index, message ->
                        Log.d("CHATSCREEN", "Message $index: ${message.text} (senderId=${message.senderId})")
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.messages) { message ->
                            Log.d("CHATSCREEN", "Rendering message: ${message.text}")
                            MessageItem(
                                message = message,
                                isFromCurrentUser = message.senderId == currentUser
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
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            Log.d("CHATSCREEN", "Sending message: $messageText")
                            viewModel.sendMessage(conversationId, messageText)
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
                            contentDescription = "Send"
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
@Composable
fun MessageItem(message: Message, isFromCurrentUser: Boolean) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isFromCurrentUser) 16.dp else 4.dp,
                        bottomEnd = if (isFromCurrentUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isFromCurrentUser) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (isFromCurrentUser) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = message.timestamp?.format(timeFormatter) ?: "Unknown time",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = if (isFromCurrentUser) TextAlign.End else TextAlign.Start,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
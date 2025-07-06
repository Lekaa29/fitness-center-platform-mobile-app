package com.example.fitnesscentarchat.ui.screens.chat

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    viewModel: ChatViewModel,
    onChatSelected: (Int, Int, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadConversation()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Chats",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
                uiState.conversations?.isEmpty() == true -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No conversations yet",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start a new conversation to see it here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.conversations!!) { conversation ->
                            ConversationItem(
                                conversation = conversation,
                                onClick = {
                                    onChatSelected(
                                        conversation.IdConversation,
                                        0,
                                        conversation.Title ?: "Unknown title"
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Show error if any
            uiState.error?.let { error ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.9f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Dismiss", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationItem(conversation: Conversation, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture or group icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (conversation.IsGroup) {
                    // Group icon
                    Card(
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF333333)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Group chat",
                            tint = Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        )
                    }
                } else {
                    // Individual chat - try to load image, fallback to person icon
                    if (!conversation.ImageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = conversation.ImageUrl,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Card(
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF333333)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile picture",
                                tint = Color.White,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = conversation.Title ?: "Untitled Conversation",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (conversation.IsGroup) "Group Chat" else "Private Chat",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Unread messages badge
            if (conversation.UnreadCount > 0) {
                Card(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50) // Green color for unread badge
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (conversation.UnreadCount > 99) "99+" else conversation.UnreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
package com.example.fitnesscentarchat.ui.screens.chat.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.models.UserMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class MessageChunk(
    val messages: List<Message>,
    val senderId: Int,
    val startTime: LocalDateTime,
    val showTimestamp: Boolean = false
)

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
                    if (isFromCurrentUser) Color(0xFF202483)
                    else Color(0xFF161616)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (isFromCurrentUser) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onPrimary
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

@Composable
fun MessageChunkItem(
    chunk: MessageChunk,
    isFromCurrentUser: Boolean,
    user: User,
    readMessages: List<UserMessage>? = null,
    currentUserId: Int
) {
    Column {
        if (chunk.showTimestamp) {
            TimestampDivider(chunk.startTime)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isFromCurrentUser) {
                ProfileCircle(
                    user = user,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f, false),
                horizontalAlignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
            ) {
                chunk.messages.forEachIndexed { index, message ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .widthIn(max = 280.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFromCurrentUser) {
                                Color(0xFF3B12A7)
                            } else {
                                Color(0xFF2A2A2A)
                            }
                        )
                    ) {
                        Text(
                            text = message.text,
                            modifier = Modifier.padding(12.dp),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    // Add read receipt indicator below each message
                    ReadReceiptIndicator(
                        messageId = message.id, // Assuming Message has an id field
                        readMessages = readMessages,
                        currentUserId = currentUserId,
                        isFromCurrentUser = isFromCurrentUser
                    )
                }
            }

            if (isFromCurrentUser) {
                ProfileCircle(
                    user = user,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
fun getReadReceiptsForMessage(messageId: Int, readMessages: List<UserMessage>?, currentUserId: Int): List<String> {
    return readMessages?.filter { userMessage ->
        userMessage.messageId >= messageId && // User's last read message ID is >= this message ID
                userMessage.isRead &&
                userMessage.userId != currentUserId // Exclude current user
    }?.map { it.name     } ?: emptyList()
}



fun groupMessagesIntoChunks(messages: List<Message>): List<MessageChunk> {
    if (messages.isEmpty()) return emptyList()

    val sortedMessages = messages.sortedBy {
        LocalDateTime.parse(it.timestamp)
    }

    val chunks = mutableListOf<MessageChunk>()
    var currentChunk = mutableListOf<Message>()
    var currentSenderId: Int? = null
    var lastTimestamp: LocalDateTime? = null

    sortedMessages.forEach { message ->
        val messageTimestamp = LocalDateTime.parse(message.timestamp)

        val shouldStartNewChunk = when {
            currentSenderId == null -> false // First message
            currentSenderId != message.senderId -> true // Different sender
            lastTimestamp != null && ChronoUnit.HOURS.between(lastTimestamp, messageTimestamp) >= 1 -> true // 1+ hour gap
            else -> false
        }

        if (shouldStartNewChunk && currentChunk.isNotEmpty()) {
            val chunkStartTime = LocalDateTime.parse(currentChunk.first().timestamp)

            // Add previous chunk
            chunks.add(MessageChunk(
                messages = currentChunk.toList(),
                senderId = currentSenderId!!,
                startTime = chunkStartTime,
                showTimestamp = chunks.isEmpty() ||
                        ChronoUnit.HOURS.between(chunks.last().startTime, chunkStartTime) >= 1
            ))
            currentChunk.clear()
        }

        currentChunk.add(message)
        currentSenderId = message.senderId
        lastTimestamp = messageTimestamp
    }

    // Add the last chunk
    if (currentChunk.isNotEmpty()) {
        val chunkStartTime = LocalDateTime.parse(currentChunk.first().timestamp)

        chunks.add(MessageChunk(
            messages = currentChunk.toList(),
            senderId = currentSenderId!!,
            startTime = chunkStartTime,
            showTimestamp = chunks.isEmpty() ||
                    ChronoUnit.HOURS.between(chunks.last().startTime, chunkStartTime) >= 1
        ))
    }

    return chunks
}


@Composable
fun ProfileCircle(
    user: User,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        if (!user.pictureLink.isNullOrBlank()) {
            AsyncImage(
                model = user.pictureLink,
                contentDescription = "${user.username}'s profile picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Text(
                text = user.username.take(1).uppercase(),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TimestampDivider(timestamp: LocalDateTime) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E).copy(alpha = 0.8f)
            )
        ) {
            Text(
                text = timestamp.format(formatter),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ReadReceiptIndicator(
    messageId: Int,
    readMessages: List<UserMessage>?,
    currentUserId: Int,
    isFromCurrentUser: Boolean
) {
    // Show read receipts for ALL messages, not just current user's
    val readByUsers = getReadReceiptsForMessage(messageId, readMessages, currentUserId)

    if (readByUsers.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp),
            horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
        ) {
            Text(
                text = "✓ Read by ${readByUsers.joinToString(", ")}",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}



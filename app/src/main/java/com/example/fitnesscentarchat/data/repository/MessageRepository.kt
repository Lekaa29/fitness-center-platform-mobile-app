package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.repository.interfaces.IAuthRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MessageRepository(
    private val apiService: ChatApiService,
    private val authRepository: IAuthRepository
) : IMessageRepository {

    override suspend fun getMessagesByUser(userId: Int): Result<List<Message>> {
        return try {
            Result.success(apiService.getMessagesByConversation(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun sendMessage(conversationId: Int, content: String): Result<Message> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                return Result.failure(IllegalStateException("User not logged in"))
            }

            val message = Message(
                id = 0,
                senderId = currentUser.id,
                idConversation = conversationId,
                text = content,
                timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT),
                isDeleted = false,
                isPinned = false
            )

            // You need to determine the recipientId somehow
            val recipientId = -1 // You'll need to implement this

            val response = apiService.sendMessage(recipientId, message)
            if (response.isSuccessful) {
                Result.success(message) // Or parse the actual response
            } else {
                Result.failure(Exception("Failed to send message: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMessagesFlow(conversationId: Int): Flow<List<Message>> = flow {
        while (true) {
            try {
                val messages = apiService.getMessagesByConversation(conversationId)
                Log.d("messCount", "${messages.size}")
                emit(messages)
            } catch (e: Exception) {
                Log.d("messError", "${e.message}")
                // Handle error or fallback
            }
            delay(3000)
        }
    }.flowOn(Dispatchers.IO)


}

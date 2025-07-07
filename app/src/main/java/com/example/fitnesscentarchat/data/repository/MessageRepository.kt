package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.models.UserMessage
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

    override suspend fun getUsersConversations(): Result<List<Conversation>> {
        return try {
            Result.success(apiService.getUsersConversations())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLastParticipantsReadMessages(conversationId: Int): Result<List<UserMessage>> {
        return try {
            Result.success(apiService.getLastParticipantsReadMessages(conversationId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun getConversationParticipants(conversationId: Int): Result<List<User>> {
        return try {
            Result.success(apiService.getConversationParticipants(conversationId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConversationUnreadMessagesAsync(conversationId: Int): Result<Int> {
        return try {
            Result.success(apiService.getConversationUnreadMessagesAsync(conversationId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getTotalUnreadMessages(): Result<Int> {
        return try {
            Result.success(apiService.getTotalUnreadMessages())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun conversationMarkAllAsRead(conversationId: Int): Result<Int> {
        return try {


            val response = apiService.conversationMarkAllAsRead(conversationId)
            if (response.isSuccessful) {
                Result.success(1) // Or parse the actual response
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
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)


}


/*
 @GET("Conversation/{conversationId}/message/unreadCount")
    suspend fun getConversationUnreadMessagesAsync(@Path("conversationId") conversationId: Int): Int
    @GET("Conversation/unreadCount")
    suspend fun getTotalUnreadMessages(@Path("conversationId") conversationId: Int): Int

    @POST("Conversation/message/markAsRead")
    suspend fun markMessagesAsRead(
        @Body idList: List<Int>
    ): Response<Any>
 */
package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.models.UserMessage
import kotlinx.coroutines.flow.Flow

interface IMessageRepository {
    suspend fun getMessagesByUser(userId: Int): Result<List<Message>>
    suspend fun sendMessage(receiverId: Int, content: String): Result<Message>
    suspend fun getUsersConversations(): Result<List<Conversation>>
    suspend fun getConversationParticipants(conversationId: Int): Result<List<User>>

    suspend fun conversationMarkAllAsRead(conversationId: Int): Result<Int>
    suspend fun getLastParticipantsReadMessages(conversationId: Int): Result<List<UserMessage>>
    suspend fun getTotalUnreadMessages(): Result<Int>
    suspend fun getConversationUnreadMessagesAsync(conversationId: Int): Result<Int>

    fun getMessagesFlow(conversationId: Int): Flow<List<Message>>


}
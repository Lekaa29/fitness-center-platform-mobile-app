package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import kotlinx.coroutines.flow.Flow

interface IMessageRepository {
    suspend fun getMessagesByUser(userId: Int): Result<List<Message>>
    suspend fun sendMessage(receiverId: Int, content: String): Result<Message>
    fun getMessagesFlow(conversationId: Int): Flow<List<Message>>


}
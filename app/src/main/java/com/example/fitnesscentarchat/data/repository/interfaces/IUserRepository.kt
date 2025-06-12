package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUserById(userId: Int): Result<User>
    fun getUsersFlow(): Flow<List<User>>
    suspend fun getUsersConversations(): Result<List<Conversation>>

}
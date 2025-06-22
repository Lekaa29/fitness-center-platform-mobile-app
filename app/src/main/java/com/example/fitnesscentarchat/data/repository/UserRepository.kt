package com.example.fitnesscentarchat.data.repository

import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Membership
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.repository.interfaces.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val apiService: ChatApiService,
    private val authRepository: AuthRepository
) : IUserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            Result.success(apiService.getUsers())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            Result.success(apiService.getUserById(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Periodic polling flow (e.g. every 10 seconds)
    override fun getUsersFlow(): Flow<List<User>> = flow {
        while (true) {
            try {
                val users = apiService.getUsers()
                emit(users)
            } catch (_: Exception) {
                // emit(emptyList()) or handle fallback
            }
            delay(10_000) // Poll every 10 seconds
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun UpdateUser(imageUrl: String): Result<User> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                return Result.failure(IllegalStateException("User not logged in"))
            }

            var user = User(
                Id = currentUser.Id,
                FirstName = currentUser.FirstName,
                LastName = currentUser.LastName,
                PictureLink = imageUrl
            )


            val response = apiService.updateUser(user)
            if (response.isSuccessful) {
                Result.success(user) // Or parse the actual response
            } else {
                Result.failure(Exception("Failed to add membership: ${response.code()}"))
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
}

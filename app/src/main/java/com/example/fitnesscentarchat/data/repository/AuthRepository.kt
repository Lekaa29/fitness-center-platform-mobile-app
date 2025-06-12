package com.example.fitnesscentarchat.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.AuthRequest
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.repository.interfaces.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: ChatApiService,
    private val sharedPreferences: SharedPreferences
) : IAuthRepository {

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
    }

    override suspend fun login(username: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(AuthRequest(username, password))

            // Save auth info
            with(sharedPreferences.edit()) {
                putString(KEY_TOKEN, response.token)
                putInt(KEY_USER_ID, response.user.Id)
                putString(KEY_USERNAME, response.user.FirstName)
                apply()
            }
            Log.d("Token", "Stored token: ${response.token}")


            Result.success(response.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(AuthRequest(username, password))

            // Save auth info
            with(sharedPreferences.edit()) {
                putString(KEY_TOKEN, response.token)
                putInt(KEY_USER_ID, response.user.Id)
                putString(KEY_USERNAME, response.user.FirstName)
                apply()
            }

            Result.success(response.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        if (userId == -1) return null

        return try {
            apiService.getUserById(userId)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}
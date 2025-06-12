package com.example.fitnesscentarchat.data.repository.interfaces

import com.example.fitnesscentarchat.data.models.User


interface IAuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun register(username: String, password: String): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun logout()
}
package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.repository.interfaces.IAttendanceRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IAuthRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMembershipRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AttendanceRepository(
    private val apiService: ChatApiService,
    private val authRepository: IAuthRepository
) : IAttendanceRepository {




    override suspend fun GetCurrentUserAttendances(): Result<List<Attendance>> {
        return try {
            Result.success(apiService.getCurrentUserAttendances())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun GetCurrentUserFitnessCenterAttendances(fitnessCenterId: Int): Result<List<Attendance>> {
        return try {
            Result.success(apiService.getCurrentUserFitnessCenterAttendances(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun GetRecentAttendees(fitnessCenterId: Int): Result<Int> {
        return try {
            Result.success(apiService.getRecentAttendees(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun GetAllAttendences(fitnessCenterId: Int): Result<List<Attendance>> {
        return try {
            Result.success(apiService.getAllAttendences(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun GetLeavingAttendees(fitnessCenterId: Int): Result<Int> {
        return try {
            Result.success(apiService.getLeavingAttendees(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun AddAttendance(fitnessCenterId: Int): Result<Attendance> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                return Result.failure(IllegalStateException("User not logged in"))
            }


            val attendance = Attendance(
                idAttendance = 0,
                userId = currentUser.id,
                fitnessCentarId = fitnessCenterId,
                timestamp = null
            )


            val response = apiService.addAttendance(attendance)
            if (response.isSuccessful) {
                Result.success(attendance) // Or parse the actual response
            } else {
                Result.failure(Exception("Failed to add attendance: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import kotlinx.coroutines.flow.Flow

interface IAttendanceRepository {
    suspend fun GetCurrentUserAttendances(): Result<List<Attendance>>
    suspend fun GetCurrentUserFitnessCenterAttendances(fitnessCenterId: Int): Result<List<Attendance>>
    suspend fun GetRecentAttendees(fitnessCenterId: Int): Result<Int>
    suspend fun GetLeavingAttendees(fitnessCenterId: Int): Result<Int>
    suspend fun GetAllAttendences(fitnessCenterId: Int): Result<List<Attendance>>
    suspend fun AddAttendance(fitnessCenterId: Int): Result<Attendance>

}
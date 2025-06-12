package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.Message
import kotlinx.coroutines.flow.Flow

interface IFitnessCenterRepository {
    suspend fun GetFitnessCenter(fitnessCenterId: Int): Result<FitnessCenter>
    suspend fun GetFitnessCenters(): Result<List<FitnessCenter>>
}
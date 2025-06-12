package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.repository.interfaces.IAuthRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IFitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FitnessCenterRepository(
    private val apiService: ChatApiService,
) : IFitnessCenterRepository {


    override suspend fun GetFitnessCenter(fitnessCenterId: Int) : Result<FitnessCenter> {
        return try{
            Result.success(apiService.getFitnessCenter(fitnessCenterId))

        } catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun GetFitnessCenters() : Result<List<FitnessCenter>> {
        return try{
            Result.success(apiService.getFitnessCenters())

        } catch(e: Exception){
            Result.failure(e)
        }
    }

}

package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Article
import com.example.fitnesscentarchat.data.models.Coach
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
    override suspend fun GetClosestFitnessCentars(userLat:Double, userLng:Double) : Result<List<FitnessCenter>> {
        return try{
            Result.success(apiService.getClosestFitnessCentars(userLat, userLng))

        } catch(e: Exception){
            Result.failure(e)
        }
    }


    override suspend fun GetFitnessCentarsCoaches(fitnessCenterId: Int) : Result<List<Coach>> {
        return try{
            Result.success(apiService.getFitnessCentarsCoaches(fitnessCenterId))

        } catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun GetFitnessCenterArticles(fitnessCenterId: Int) : Result<List<Article>> {
        return try{
            Result.success(apiService.getFitnessCenterArticles(fitnessCenterId))

        } catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun GetConversationIdByRecepient(userId: Int) : Result<Int> {
        return try{
            Result.success(apiService.getConversationIdByRecepient(userId))

        } catch(e: Exception){
            Result.failure(e)
        }
    }



    override suspend fun GetPromoFitnessCenters() : Result<List<FitnessCenter>> {
        return try{
            Result.success(apiService.getPromoFitnessCenters())

        } catch(e: Exception){
            Result.failure(e)
        }
    }


}

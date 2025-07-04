package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Article
import com.example.fitnesscentarchat.data.models.Coach
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.Message
import kotlinx.coroutines.flow.Flow

interface IFitnessCenterRepository {
    suspend fun GetFitnessCenter(fitnessCenterId: Int): Result<FitnessCenter>
    suspend fun GetFitnessCenters(): Result<List<FitnessCenter>>
    suspend fun GetFitnessCentarsCoaches(fitnessCenterId: Int) : Result<List<Coach>>
    suspend fun GetFitnessCenterArticles(fitnessCenterId: Int) : Result<List<Article>>
    suspend fun GetPromoFitnessCenters() : Result<List<FitnessCenter>>
    suspend fun GetConversationIdByRecepient(userId: Int) : Result<Int>
    suspend fun GetClosestFitnessCentars(userLat:Double, userLng:Double) : Result<List<FitnessCenter>>
}
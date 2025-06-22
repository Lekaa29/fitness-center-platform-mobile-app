package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Membership
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.models.ShopItem
import com.example.fitnesscentarchat.data.repository.interfaces.IAuthRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMembershipRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMessageRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IShopRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ShopRepository(
    private val apiService: ChatApiService,
    private val authRepository: IAuthRepository
) : IShopRepository {



    override suspend fun GetFitnessCenterItems(fitnessCenterId: Int): Result<List<ShopItem>> {
        return try {
            Result.success(apiService.getFitnessCenterItems(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun GetUserItems(): Result<List<ShopItem>> {
        return try {
            Result.success(apiService.getUserItems())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun GetShopItem(shopItemId: Int): Result<ShopItem> {
        return try {
            Result.success(apiService.getShopItem(shopItemId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun BuyShopItem(shopItemId: Int): Result<Int> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                return Result.failure(IllegalStateException("User not logged in"))
            }



            val response = apiService.buyShopItem(shopItemId)
            if (response.isSuccessful) {
                Result.success(shopItemId) // Or parse the actual response
            } else {
                Result.failure(Exception("Failed to buy shopItem: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

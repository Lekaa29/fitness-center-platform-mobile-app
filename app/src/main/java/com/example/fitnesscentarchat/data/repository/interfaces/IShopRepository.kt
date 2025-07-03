package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Message
import com.example.fitnesscentarchat.data.models.ShopItem
import kotlinx.coroutines.flow.Flow

interface IShopRepository {
    suspend fun GetFitnessCenterItems(fitnessCenterId: Int): Result<List<ShopItem>>
    suspend fun GetUserItems(): Result<List<ShopItem>>
    suspend fun GetShopItem(shopItemId: Int): Result<ShopItem>
    suspend fun BuyShopItem(shopItemId: Int): Result<Int>
}
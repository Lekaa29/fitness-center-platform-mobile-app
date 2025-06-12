package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Membership
import com.example.fitnesscentarchat.data.models.Message
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

class MembershipRepository(
    private val apiService: ChatApiService,
    private val authRepository: IAuthRepository
) : IMembershipRepository {



    override suspend fun GetCurrentUserMemberships(): Result<List<Membership>> {
        return try {
            Result.success(apiService.getCurrentUserMemberships())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun GetUserMembershipByFitnessCenter(fitnessCenterId: Int): Result<Membership> {
        return try {
            Result.success(apiService.getUserMembershipByFitnessCenter(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun AddMembership(fitnessCenterId: Int): Result<Membership> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                return Result.failure(IllegalStateException("User not logged in"))
            }



            val membership = Membership(
                IdMembership = 0,
                IdUser = currentUser.Id,
                IdFitnessCentar = fitnessCenterId,
                LoyaltyPoints = null,
                StreakRunCount = null,
                MembershipDeadline = null
            )


            val response = apiService.addMembership(membership)
            if (response.isSuccessful) {
                Result.success(membership) // Or parse the actual response
            } else {
                Result.failure(Exception("Failed to add membership: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

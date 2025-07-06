package com.example.fitnesscentarchat.data.repository

import android.util.Log
import com.example.fitnesscentarchat.data.api.ChatApiService
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.MembershipPackage
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



    override suspend fun GetCurrentUserMemberships(): Result<List<MembershipModel>> {
        return try {
            Result.success(apiService.getCurrentUserMemberships())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun GetFitnessCenterMembershipPackages(fitnessCenterId: Int): Result<List<MembershipPackage>> {
        return try {
            Result.success(apiService.getFitnessCenterMembershipPackages(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun GetUserMembershipByFitnessCenter(fitnessCenterId: Int): Result<MembershipModel?> {
        return try {
            val response = apiService.getUserMembershipByFitnessCenter(fitnessCenterId)

            if (response.isSuccessful) {
                Result.success(response.body()) // body could be null, which is fine
            } else {
                // For non-200 responses, you might want to return null instead of failing
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun GetFitnessCenterLeaderboard(fitnessCenterId: Int): Result<List<MembershipModel>> {
        return try {
            Result.success(apiService.getFitnessCenterLeaderboard(fitnessCenterId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun AddMembership(fitnessCenterId: Int, membershipPackageId: Int): Result<MembershipModel> {
        return try {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                return Result.failure(IllegalStateException("User not logged in"))
            }



            val membership = MembershipModel(
                idMembership = 0,
                idUser = currentUser.id,
                idFitnessCentar = fitnessCenterId,
                loyaltyPoints = null,
                streakRunCount = null,
                fitnessCentarBannerUrl = null,
                fitnessCentarName = null,
                membershipDeadline = null,
                points = null,
                fitnessCentarLogoUrl = null,
                username = null,
                idMembershipPackage = membershipPackageId
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

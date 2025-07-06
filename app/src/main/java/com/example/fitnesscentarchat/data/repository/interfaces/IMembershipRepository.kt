package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.MembershipPackage
import com.example.fitnesscentarchat.data.models.Message
import kotlinx.coroutines.flow.Flow

interface IMembershipRepository {
    suspend fun GetCurrentUserMemberships(): Result<List<MembershipModel>>
    suspend fun GetUserMembershipByFitnessCenter(fitnessCenterId: Int): Result<MembershipModel?>
    suspend fun AddMembership(fitnessCenterId: Int, membershipPackageId: Int): Result<MembershipModel>
    suspend fun GetFitnessCenterLeaderboard(fitnessCenterId: Int): Result<List<MembershipModel>>

    suspend fun GetFitnessCenterMembershipPackages(fitnessCenterId: Int): Result<List<MembershipPackage>>

}
package com.example.fitnesscentarchat.data.repository.interfaces


import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.Membership
import com.example.fitnesscentarchat.data.models.Message
import kotlinx.coroutines.flow.Flow

interface IMembershipRepository {
    suspend fun GetCurrentUserMemberships(): Result<List<Membership>>
    suspend fun GetUserMembershipByFitnessCenter(fitnessCenterId: Int): Result<Membership>
    suspend fun AddMembership(fitnessCenterId: Int): Result<Membership>

}
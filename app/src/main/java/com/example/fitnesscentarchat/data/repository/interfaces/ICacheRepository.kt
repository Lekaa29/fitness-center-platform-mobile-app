package com.example.fitnesscentarchat.data.repository.interfaces

import com.example.fitnesscentarchat.data.models.Article
import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.models.Coach
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.MembershipModel

interface ICacheRepository {
    suspend fun cacheFitnessCenters(fitnessCenters: List<FitnessCenter>)
    suspend fun getCachedFitnessCenters(): List<FitnessCenter>

    suspend fun cachePromoFitnessCenters(promoFitnessCenters: List<FitnessCenter>)
    suspend fun getCachedPromoFitnessCenters(): List<FitnessCenter>

    suspend fun cacheNearFitnessCenters(nearFitnessCenters: List<FitnessCenter>)
    suspend fun getCachedNearFitnessCenters(): List<FitnessCenter>

    suspend fun cacheMemberships(memberships: List<MembershipModel>)
    suspend fun getCachedMemberships(): List<MembershipModel>


    // Cache methods
    suspend fun cacheFitnessCenter(fitnessCenterId: Int, fitnessCenter: FitnessCenter)
    suspend fun getCachedFitnessCenter(fitnessCenterId: Int): FitnessCenter?
    suspend fun cacheRecentAttendance(fitnessCenterId: Int, attendance: Int)
    suspend fun getCachedRecentAttendance(fitnessCenterId: Int): Int
    suspend fun cacheAllAttendances(fitnessCenterId: Int, attendances: List<Attendance>)
    suspend fun getCachedAllAttendances(fitnessCenterId: Int): List<Attendance>
    suspend fun cacheCoaches(fitnessCenterId: Int, coaches: List<Coach>)
    suspend fun getCachedCoaches(fitnessCenterId: Int): List<Coach>
    suspend fun cacheLeaderboard(fitnessCenterId: Int, leaderboard: List<MembershipModel>)
    suspend fun getCachedLeaderboard(fitnessCenterId: Int): List<MembershipModel>
    suspend fun cacheNews(fitnessCenterId: Int, news: List<Article>)
    suspend fun getCachedNews(fitnessCenterId: Int): List<Article>
    suspend fun cacheSoonLeaving(fitnessCenterId: Int, soonLeaving: Int)
    suspend fun getCachedSoonLeaving(fitnessCenterId: Int): Int

    // Cache management
    suspend fun clearFitnessCenterCache(fitnessCenterId: Int)
    suspend fun isFitnessCenterCacheValid(fitnessCenterId: Int): Boolean
    suspend fun getFitnessCenterCacheTimestamp(fitnessCenterId: Int): Long

    suspend fun clearCache()
    suspend fun isCacheValid(): Boolean
    suspend fun getCacheTimestamp(): Long
}
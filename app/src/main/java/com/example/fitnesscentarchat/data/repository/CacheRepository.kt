
package com.example.fitnesscentarchat.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.fitnesscentarchat.data.models.Article
import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.models.Coach
import com.example.fitnesscentarchat.data.models.FitnessCenter
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.repository.interfaces.ICacheRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheRepository @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : ICacheRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("fitness_cache", Context.MODE_PRIVATE)

    companion object {
        // Hub cache keys
        private const val KEY_FITNESS_CENTERS = "fitness_centers"
        private const val KEY_PROMO_FITNESS_CENTERS = "promo_fitness_centers"
        private const val KEY_NEAR_FITNESS_CENTERS = "near_fitness_centers"
        private const val KEY_MEMBERSHIPS = "memberships"
        private const val KEY_CACHE_TIMESTAMP = "cache_timestamp"

        // Fitness center specific cache keys
        private const val KEY_FITNESS_CENTER_PREFIX = "fitness_center_"
        private const val KEY_RECENT_ATTENDANCE_PREFIX = "recent_attendance_"
        private const val KEY_ALL_ATTENDANCES_PREFIX = "all_attendances_"
        private const val KEY_COACHES_PREFIX = "coaches_"
        private const val KEY_LEADERBOARD_PREFIX = "leaderboard_"
        private const val KEY_NEWS_PREFIX = "news_"
        private const val KEY_SOON_LEAVING_PREFIX = "soon_leaving_"
        private const val KEY_FC_CACHE_TIMESTAMP_PREFIX = "fc_cache_timestamp_"

        private const val CACHE_VALIDITY_HOURS = 24 // Cache valid for 24 hours
    }

    // ============== HUB CACHING METHODS ==============

    override suspend fun cacheFitnessCenters(fitnessCenters: List<FitnessCenter>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(fitnessCenters)
            sharedPreferences.edit()
                .putString(KEY_FITNESS_CENTERS, json)
                .putLong(KEY_CACHE_TIMESTAMP, System.currentTimeMillis())
                .apply()
        }
    }

    override suspend fun getCachedFitnessCenters(): List<FitnessCenter> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_FITNESS_CENTERS, null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<FitnessCenter>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun cachePromoFitnessCenters(promoFitnessCenters: List<FitnessCenter>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(promoFitnessCenters)
            sharedPreferences.edit()
                .putString(KEY_PROMO_FITNESS_CENTERS, json)
                .apply()
        }
    }

    override suspend fun getCachedPromoFitnessCenters(): List<FitnessCenter> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_PROMO_FITNESS_CENTERS, null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<FitnessCenter>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun cacheNearFitnessCenters(nearFitnessCenters: List<FitnessCenter>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(nearFitnessCenters)
            sharedPreferences.edit()
                .putString(KEY_NEAR_FITNESS_CENTERS, json)
                .apply()
        }
    }

    override suspend fun getCachedNearFitnessCenters(): List<FitnessCenter> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_NEAR_FITNESS_CENTERS, null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<FitnessCenter>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun cacheMemberships(memberships: List<MembershipModel>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(memberships)
            sharedPreferences.edit()
                .putString(KEY_MEMBERSHIPS, json)
                .apply()
        }
    }

    override suspend fun getCachedMemberships(): List<MembershipModel> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_MEMBERSHIPS, null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<MembershipModel>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    // ============== FITNESS CENTER SPECIFIC CACHING METHODS ==============

    override suspend fun cacheFitnessCenter(fitnessCenterId: Int, fitnessCenter: FitnessCenter) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(fitnessCenter)
            sharedPreferences.edit()
                .putString("$KEY_FITNESS_CENTER_PREFIX$fitnessCenterId", json)
                .putLong("$KEY_FC_CACHE_TIMESTAMP_PREFIX$fitnessCenterId", System.currentTimeMillis())
                .apply()
        }
    }

    override suspend fun getCachedFitnessCenter(fitnessCenterId: Int): FitnessCenter? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString("$KEY_FITNESS_CENTER_PREFIX$fitnessCenterId", null)
            if (json != null) {
                try {
                    gson.fromJson(json, FitnessCenter::class.java)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    override suspend fun cacheRecentAttendance(fitnessCenterId: Int, attendance: Int) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(attendance)
            sharedPreferences.edit()
                .putString("$KEY_RECENT_ATTENDANCE_PREFIX$fitnessCenterId", json)
                .apply()
        }
    }

    override suspend fun getCachedRecentAttendance(fitnessCenterId: Int): Int {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getInt("$KEY_RECENT_ATTENDANCE_PREFIX$fitnessCenterId", 0)
        }
    }

    override suspend fun cacheAllAttendances(fitnessCenterId: Int, attendances: List<Attendance>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(attendances)
            sharedPreferences.edit()
                .putString("$KEY_ALL_ATTENDANCES_PREFIX$fitnessCenterId", json)
                .apply()
        }
    }

    override suspend fun getCachedAllAttendances(fitnessCenterId: Int): List<Attendance> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString("$KEY_ALL_ATTENDANCES_PREFIX$fitnessCenterId", null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<Attendance>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun cacheCoaches(fitnessCenterId: Int, coaches: List<Coach>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(coaches)
            sharedPreferences.edit()
                .putString("$KEY_COACHES_PREFIX$fitnessCenterId", json)
                .apply()
        }
    }

    override suspend fun getCachedCoaches(fitnessCenterId: Int): List<Coach> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString("$KEY_COACHES_PREFIX$fitnessCenterId", null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<Coach>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun cacheLeaderboard(fitnessCenterId: Int, leaderboard: List<MembershipModel>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(leaderboard)
            sharedPreferences.edit()
                .putString("$KEY_LEADERBOARD_PREFIX$fitnessCenterId", json)
                .apply()
        }
    }

    override suspend fun getCachedLeaderboard(fitnessCenterId: Int): List<MembershipModel> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString("$KEY_LEADERBOARD_PREFIX$fitnessCenterId", null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<MembershipModel>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun cacheNews(fitnessCenterId: Int, news: List<Article>) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(news)
            sharedPreferences.edit()
                .putString("$KEY_NEWS_PREFIX$fitnessCenterId", json)
                .apply()
        }
    }

    override suspend fun getCachedNews(fitnessCenterId: Int): List<Article> {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString("$KEY_NEWS_PREFIX$fitnessCenterId", null)
            if (json != null) {
                try {
                    val type = object : TypeToken<List<Article>>() {}.type
                    gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun cacheSoonLeaving(fitnessCenterId: Int, soonLeaving: Int) {
        withContext(Dispatchers.IO) {
            val json = gson.toJson(soonLeaving)
            sharedPreferences.edit()
                .putString("$KEY_SOON_LEAVING_PREFIX$fitnessCenterId", json)
                .apply()
        }
    }

    override suspend fun getCachedSoonLeaving(fitnessCenterId: Int): Int {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getInt("$KEY_SOON_LEAVING_PREFIX$fitnessCenterId", 0)
        }
    }

    // ============== CACHE MANAGEMENT METHODS ==============

    override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .clear()
                .apply()
        }
    }

    override suspend fun clearFitnessCenterCache(fitnessCenterId: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .remove("$KEY_FITNESS_CENTER_PREFIX$fitnessCenterId")
                .remove("$KEY_RECENT_ATTENDANCE_PREFIX$fitnessCenterId")
                .remove("$KEY_ALL_ATTENDANCES_PREFIX$fitnessCenterId")
                .remove("$KEY_COACHES_PREFIX$fitnessCenterId")
                .remove("$KEY_LEADERBOARD_PREFIX$fitnessCenterId")
                .remove("$KEY_NEWS_PREFIX$fitnessCenterId")
                .remove("$KEY_SOON_LEAVING_PREFIX$fitnessCenterId")
                .remove("$KEY_FC_CACHE_TIMESTAMP_PREFIX$fitnessCenterId")
                .apply()
        }
    }

    override suspend fun isCacheValid(): Boolean {
        return withContext(Dispatchers.IO) {
            val timestamp = sharedPreferences.getLong(KEY_CACHE_TIMESTAMP, 0)
            val currentTime = System.currentTimeMillis()
            val cacheAge = currentTime - timestamp
            val maxAge = CACHE_VALIDITY_HOURS * 60 * 60 * 1000 // Convert to milliseconds

            cacheAge < maxAge
        }
    }

    override suspend fun isFitnessCenterCacheValid(fitnessCenterId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val timestamp = sharedPreferences.getLong("$KEY_FC_CACHE_TIMESTAMP_PREFIX$fitnessCenterId", 0)
            val currentTime = System.currentTimeMillis()
            val cacheAge = currentTime - timestamp
            val maxAge = CACHE_VALIDITY_HOURS * 60 * 60 * 1000 // Convert to milliseconds

            cacheAge < maxAge
        }
    }

    override suspend fun getCacheTimestamp(): Long {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getLong(KEY_CACHE_TIMESTAMP, 0)
        }
    }

    override suspend fun getFitnessCenterCacheTimestamp(fitnessCenterId: Int): Long {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getLong("$KEY_FC_CACHE_TIMESTAMP_PREFIX$fitnessCenterId", 0)
        }
    }
}
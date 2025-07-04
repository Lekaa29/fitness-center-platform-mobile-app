package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.AttendanceRepository
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import com.example.fitnesscentarchat.data.repository.UserRepository
import com.example.fitnesscentarchat.data.repository.interfaces.ICacheRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FitnessCenterViewModel(
    private val fitnessCenterRepository: FitnessCenterRepository,
    private val membershipRepository: MembershipRepository,
    private val attendanceRepository: AttendanceRepository,
    private val userRepository: UserRepository,
    private val cacheRepository: ICacheRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FitnessCenterUiState())
    val uiState: StateFlow<FitnessCenterUiState> = _uiState.asStateFlow()

    /**
     * Load cached data from SharedPreferences for immediate UI population
     */
    private fun loadFromCache(fitnessCenterId: Int) {
        viewModelScope.launch {
            try {
                val cachedFitnessCenter = cacheRepository.getCachedFitnessCenter(fitnessCenterId)
                val cachedRecentAttendance = cacheRepository.getCachedRecentAttendance(fitnessCenterId)
                val cachedAllAttendances = cacheRepository.getCachedAllAttendances(fitnessCenterId)
                val cachedCoaches = cacheRepository.getCachedCoaches(fitnessCenterId)
                val cachedLeaderboard = cacheRepository.getCachedLeaderboard(fitnessCenterId)
                val cachedNews = cacheRepository.getCachedNews(fitnessCenterId)
                val cachedSoonLeaving = cacheRepository.getCachedSoonLeaving(fitnessCenterId)

                _uiState.update { currentState ->
                    currentState.copy(
                        fitnessCenter = cachedFitnessCenter,
                        recentAttendance = cachedRecentAttendance,
                        allAttendances = cachedAllAttendances,
                        coaches = cachedCoaches,
                        leaderboard = cachedLeaderboard,
                        news = cachedNews,
                        soonLeaving = cachedSoonLeaving,
                        isLoading = cachedFitnessCenter == null, // Only show loading if no cached data
                        isFromCache = true // Flag to indicate this is cached data
                    )
                }

                Log.d("FitnessCenterViewModel", "Loaded cached data for fitness center $fitnessCenterId")
            } catch (e: Exception) {
                Log.e("FitnessCenterViewModel", "Failed to load cached data", e)
                // Continue with normal loading if cache fails
            }
        }
    }

    fun loadFitnessCenterHome(fitnessCenterId: Int, forceRefresh: Boolean = false) {
        // Load from cache first for immediate UI population
        if (!forceRefresh) {
            loadFromCache(fitnessCenterId)
        }

        viewModelScope.launch {
            // Only show loading if we don't have cached data or force refresh
            if (forceRefresh || _uiState.value.fitnessCenter == null) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }

            // Load recent attendees
            attendanceRepository.GetRecentAttendees(fitnessCenterId).fold(
                onSuccess = { recentAttendees ->
                    _uiState.update {
                        it.copy(
                            recentAttendance = recentAttendees,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheRecentAttendance(fitnessCenterId, recentAttendees)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load recent Attendance")
                }
            )

            // Load all attendances
            attendanceRepository.GetAllAttendences(fitnessCenterId).fold(
                onSuccess = { allAttendences ->
                    _uiState.update {
                        it.copy(
                            allAttendances = allAttendences,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheAllAttendances(fitnessCenterId, allAttendences)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load all Attendance")
                }
            )

            // Load coaches
            fitnessCenterRepository.GetFitnessCentarsCoaches(fitnessCenterId).fold(
                onSuccess = { coaches ->
                    _uiState.update {
                        it.copy(
                            coaches = coaches,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheCoaches(fitnessCenterId, coaches)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load coaches")
                }
            )

            // Load leaderboard
            membershipRepository.GetFitnessCenterLeaderboard(fitnessCenterId).fold(
                onSuccess = { memberships ->
                    _uiState.update {
                        it.copy(
                            leaderboard = memberships,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheLeaderboard(fitnessCenterId, memberships)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load leaderboard")
                }
            )

            // Load news articles
            fitnessCenterRepository.GetFitnessCenterArticles(fitnessCenterId).fold(
                onSuccess = { articles ->
                    _uiState.update {
                        it.copy(
                            news = articles,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheNews(fitnessCenterId, articles)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load news articles")
                }
            )

            // Load leaving attendees
            attendanceRepository.GetLeavingAttendees(fitnessCenterId).fold(
                onSuccess = { leavingAttendees ->
                    _uiState.update {
                        it.copy(
                            soonLeaving = leavingAttendees,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheSoonLeaving(fitnessCenterId, leavingAttendees)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load leaving Attendance")
                }
            )

            // Load fitness center details
            fitnessCenterRepository.GetFitnessCenter(fitnessCenterId).fold(
                onSuccess = { fitnessCenter ->
                    _uiState.update {
                        it.copy(
                            fitnessCenter = fitnessCenter,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheFitnessCenter(fitnessCenterId, fitnessCenter)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load fitness center")
                }
            )
        }
    }

    private fun handleError(error: Throwable, defaultMessage: String) {
        val errorMessage = error.message ?: defaultMessage

        // Only show error if we don't have cached data to fall back on
        if (_uiState.value.fitnessCenter == null &&
            _uiState.value.recentAttendance == null &&
            _uiState.value.allAttendances.isEmpty() &&
            _uiState.value.coaches.isEmpty() &&
            _uiState.value.leaderboard.isEmpty() &&
            _uiState.value.news.isEmpty() &&
            _uiState.value.soonLeaving == null) {
            _uiState.update {
                it.copy(
                    error = errorMessage,
                    isLoading = false
                )
            }
        } else {
            // We have cached data, just log the error and continue with cached data
            Log.w("FitnessCenterViewModel", "Network error but cached data available: $errorMessage")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    // Optionally show a subtle indicator that data might be stale
                    isFromCache = true
                )
            }
        }
    }

    /**
     * Refresh data from network
     */
    fun refresh(fitnessCenterId: Int) {
        loadFitnessCenterHome(fitnessCenterId, forceRefresh = true)
    }

    /**
     * Clear cache and reload
     */
    fun clearCacheAndReload(fitnessCenterId: Int) {
        viewModelScope.launch {
            cacheRepository.clearFitnessCenterCache(fitnessCenterId)
            _uiState.update { FitnessCenterUiState() }
            loadFitnessCenterHome(fitnessCenterId)
        }
    }

    suspend fun getConversationIdByRecipient(recipientId: Int): Int {
        return fitnessCenterRepository.GetConversationIdByRecepient(recipientId).fold(
            onSuccess = { conversationId -> conversationId ?: -1 },
            onFailure = { error ->
                Log.d("Error", "Error loading conversationId from recipient: ${error.message}")
                -1
            }
        )
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
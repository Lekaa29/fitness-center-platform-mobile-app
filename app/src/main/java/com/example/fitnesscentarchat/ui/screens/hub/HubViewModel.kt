package com.example.fitnesscentarchat.ui.screens.hub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.interfaces.ICacheRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IFitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.interfaces.IMembershipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HubViewModel(
    private val fitnessCenterRepository: IFitnessCenterRepository,
    private val membershipRepository: IMembershipRepository,
    private val cacheRepository: ICacheRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HubUiState())
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    init {
        // Load from cache first for immediate UI population
        loadFromCache()
        // Then load fresh data from network
        loadFitnessCenters()
    }

    /**
     * Load cached data from SharedPreferences for immediate UI population
     */
    private fun loadFromCache() {
        viewModelScope.launch {
            try {
                val cachedFitnessCenters = cacheRepository.getCachedFitnessCenters()
                val cachedPromoFitnessCenters = cacheRepository.getCachedPromoFitnessCenters()
                val cachedNearFitnessCenters = cacheRepository.getCachedNearFitnessCenters()
                val cachedMemberships = cacheRepository.getCachedMemberships()

                _uiState.update { currentState ->
                    currentState.copy(
                        fitnessCenters = cachedFitnessCenters,
                        promoFitnessCentars = cachedPromoFitnessCenters,
                        nearFitnessCentars = cachedNearFitnessCenters,
                        usersMemberships = cachedMemberships,
                        isLoading = cachedFitnessCenters.isEmpty(), // Only show loading if no cached data
                        isFromCache = true // Flag to indicate this is cached data
                    )
                }

                Log.d("HubViewModel", "Loaded cached data: ${cachedFitnessCenters.size} centers, ${cachedMemberships.size} memberships")
            } catch (e: Exception) {
                Log.e("HubViewModel", "Failed to load cached data", e)
                // Continue with normal loading if cache fails
            }
        }
    }

    fun loadFitnessCenters(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            // Only show loading if we don't have cached data or force refresh
            if (forceRefresh || _uiState.value.fitnessCenters.isEmpty()) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }

            // Load all fitness centers
            fitnessCenterRepository.GetFitnessCenters().fold(
                onSuccess = { fitnessCenters ->
                    _uiState.update {
                        it.copy(
                            fitnessCenters = fitnessCenters,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheFitnessCenters(fitnessCenters)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load fitness centers")
                }
            )

            // Load promo fitness centers
            fitnessCenterRepository.GetPromoFitnessCenters().fold(
                onSuccess = { promoFitnessCentars ->
                    _uiState.update {
                        it.copy(
                            promoFitnessCentars = promoFitnessCentars,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cachePromoFitnessCenters(promoFitnessCentars)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load promo fitness centers")
                }
            )

            // Load closest fitness centers
            fitnessCenterRepository.GetClosestFitnessCentars(45.2796166, 18.7886827).fold(
                onSuccess = { closestFitnessCentars ->
                    _uiState.update {
                        it.copy(
                            nearFitnessCentars = closestFitnessCentars,
                            isLoading = false,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheNearFitnessCenters(closestFitnessCentars)
                },
                onFailure = { error ->
                    handleError(error, "Failed to load closest fitness centers")
                }
            )

            // Load user memberships
            membershipRepository.GetCurrentUserMemberships().fold(
                onSuccess = { memberships ->
                    Log.d("HubViewModel", "Successfully loaded ${memberships.size} memberships")
                    _uiState.update {
                        it.copy(
                            usersMemberships = memberships,
                            isLoading = false,
                            error = null,
                            isFromCache = false
                        )
                    }
                    // Cache the fresh data
                    cacheRepository.cacheMemberships(memberships)
                },
                onFailure = { error ->
                    Log.e("HubViewModel", "Failed to load memberships: ${error.message}", error)
                    handleError(error, "Failed to load memberships")
                }
            )

            val currentUser = authRepository.getCurrentUser()
            _uiState.update {
                it.copy(
                    currentUser = currentUser
                )
            }
        }
    }

    private fun handleError(error: Throwable, defaultMessage: String) {
        val errorMessage = error.message ?: defaultMessage

        // Only show error if we don't have cached data to fall back on
        if (_uiState.value.fitnessCenters.isEmpty() &&
            _uiState.value.promoFitnessCentars.isEmpty() &&
            _uiState.value.usersMemberships.isEmpty()) {
            _uiState.update {
                it.copy(
                    error = errorMessage,
                    isLoading = false
                )
            }
        } else {
            // We have cached data, just log the error and continue with cached data
            Log.w("HubViewModel", "Network error but cached data available: $errorMessage")
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
    fun refresh() {
        loadFitnessCenters(forceRefresh = true)
    }

    /**
     * Clear cache and reload
     */
    fun clearCacheAndReload() {
        viewModelScope.launch {
            cacheRepository.clearCache()
            _uiState.update { HubUiState() }
            loadFitnessCenters()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
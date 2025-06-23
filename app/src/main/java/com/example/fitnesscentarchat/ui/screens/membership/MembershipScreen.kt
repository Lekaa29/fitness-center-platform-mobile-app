package com.example.fitnesscentarchat.ui.screens.hub

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.repository.AuthRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
    fitnessCenterId: Int,
    authRepository: AuthRepository,
    viewModel: MembershipViewModel
) {

    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle()

    var currentUser by remember { mutableStateOf<Int?>(null) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")



    LaunchedEffect(fitnessCenterId) {
        currentUser = authRepository.getCurrentUser()?.Id
        viewModel.loadMembership(fitnessCenterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fitness Center HOME") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.membership == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.membership == null) {
                Text(
                    text = "No membership found",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column {
                    uiState.membership?.let { membership ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            val deadline = LocalDateTime.parse(membership.MembershipDeadline, formatter)
                            val daysRemaining = ChronoUnit.DAYS.between(LocalDateTime.now(), deadline)

                            Text(
                                text = "${daysRemaining} days remaining...",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                            Text(
                                text = "LOYALTY: ${membership.LoyaltyPoints.toString()}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                            Text(
                                text = "STREAK: ${membership.StreakRunCount.toString()}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            if (uiState.error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(uiState.error!!)
                }
            }
        }
    }
}

/*

@Composable
fun Background(
    modifier: Modifier = Modifier,
) {

    var topTextOffsetY by remember { mutableStateOf(0f) }

    var membershipItemOverlay by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(){
            BackgroundScrollableContent(
                onTopTextPositioned = { topTextOffsetY = it },
                onMembershipItemChange = { membershipItemOverlay = it},
                membershipItemOverlay = membershipItemOverlay,
                uiState = MembershipUiState()
            )
        }

    }
}
 */
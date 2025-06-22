package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.repository.AuthRepository


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessCenterScreen(
    fitnessCenterId: Int,
    authRepository: AuthRepository,
    viewModel: FitnessCenterViewModel,
    onNavigateBack: () -> Unit
) {

    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle()

    var currentUser by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(fitnessCenterId) {
        currentUser = authRepository.getCurrentUser()?.Id
        viewModel.loadFitnessCenterHome(fitnessCenterId)
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
            if (uiState.isLoading && uiState.fitnessCenter == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.fitnessCenter == null) {
                Text(
                    text = "No fitness center found",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column {
                    uiState.fitnessCenter?.let { fitnessCenter ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = fitnessCenter.Name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                    uiState.recentAttendance?.let { recentAttendance ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "RECENT: $recentAttendance",
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
    fitnessCenterId: Int,
    //authRepository: AuthRepository,
    //viewModel: FitnessCenterViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    /*
    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle()

     */
    val currentUser = User()
    val uiState = FitnessCenterUiState()

    var topTextOffsetY by remember { mutableStateOf(0f) }
    var showNewsOverlay by remember { mutableStateOf(false) }
    var showLeaderboardOverlay by remember { mutableStateOf(false) }
    var showCoachesOverlay by remember { mutableStateOf(false) }
    var showGraphOverlay by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        Box(){
            BackgroundScrollableContent(
                currentUser,
                uiState=uiState,
                onTopTextPositioned = { topTextOffsetY = it },
                onShowNewsOverlayChange = { showNewsOverlay = it},
                showNewsOverlay,
                onLeaderboardOverlayChange = { showLeaderboardOverlay = it},
                showLeaderboardOverlay,
                onCoachesOverlayChange = { showCoachesOverlay = it},
                showCoachesOverlay,
                showGraphOverlay = showGraphOverlay,
                onshowGraphOverlayChange = { showGraphOverlay = it}
            )
        }

    }
}

 */
package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.components.FitnessCenterContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessCenterScreen(
    fitnessCenterId: Int,
    authRepository: AuthRepository,
    viewModel: FitnessCenterViewModel,
    onNavigateBack: () -> Unit,
    onChatClicked: (Int, Int, String) -> Unit,
) {

    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle()

    var currentUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(fitnessCenterId) {
        currentUser = authRepository.getCurrentUser()
        viewModel.loadFitnessCenterHome(fitnessCenterId)
    }

    var topTextOffsetY by remember { mutableStateOf(0f) }
    var showNewsOverlay by remember { mutableStateOf(false) }
    var showLeaderboardOverlay by remember { mutableStateOf(false) }
    var showCoachesOverlay by remember { mutableStateOf(false) }
    var showGraphOverlay by remember { mutableStateOf(false) }
    var viewQROverlay by remember { mutableStateOf(false) }



    val scrollState = rememberScrollState()


    Scaffold(

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(){
                FitnessCenterContent(
                    currentUser,
                    onChatClicked = onChatClicked,
                    uiState=uiState,
                    scrollState = scrollState,
                    onTopTextPositioned = { topTextOffsetY = it },
                    onShowNewsOverlayChange = { showNewsOverlay = it},
                    showNewsOverlay,
                    onLeaderboardOverlayChange = { showLeaderboardOverlay = it},
                    showLeaderboardOverlay,
                    onCoachesOverlayChange = { showCoachesOverlay = it},
                    showCoachesOverlay,
                    showGraphOverlay = showGraphOverlay,
                    onshowGraphOverlayChange = { showGraphOverlay = it},
                    onViewQRChange = { viewQROverlay = it},
                    viewQROverlay = viewQROverlay,
                    viewModel = viewModel
                )
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

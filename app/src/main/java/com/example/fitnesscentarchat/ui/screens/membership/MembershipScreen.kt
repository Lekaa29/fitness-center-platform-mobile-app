package com.example.fitnesscentarchat.ui.screens.hub

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.ui.screens.membership.components.MembershipContent
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

    var topTextOffsetY by remember { mutableStateOf(0f) }

    var membershipItemOverlay by remember { mutableStateOf(false) }


    LaunchedEffect(fitnessCenterId) {
        currentUser = authRepository.getCurrentUser()?.id
        viewModel.loadMembership(fitnessCenterId)
    }
    Log.d("packages", "${uiState.membershipPackages}")

    Scaffold(

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
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(){
                        MembershipContent(
                            onTopTextPositioned = { topTextOffsetY = it },
                            onMembershipItemChange = { membershipItemOverlay = it},
                            membershipItemOverlay = membershipItemOverlay,
                            uiState = uiState
                        )
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

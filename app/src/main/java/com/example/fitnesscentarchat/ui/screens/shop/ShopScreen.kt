package com.example.fitnesscentarchat.ui.screens.hub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.models.Conversation
import com.example.fitnesscentarchat.data.models.ShopItem
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.ui.screens.membership.components.MembershipContent
import com.example.fitnesscentarchat.ui.screens.shop.components.ShopContent
import com.example.fitnesscentarchat.ui.screens.users.UsersViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    fitnessCenterId: Int,
    viewModel: ShopViewModel,
    authRepository: AuthRepository,
) {

    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle()

    var currentUser by remember { mutableStateOf<Int?>(null) }


    val scrollState = rememberScrollState()

    var buyItemOverlay by remember { mutableStateOf(false) }



    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(fitnessCenterId) {
        coroutineScope.launch {
            launch {
                currentUser = authRepository.getCurrentUser()?.id
            }
            launch {
                viewModel.loadShop(fitnessCenterId)
            }
        }
    }


    Scaffold(

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.shopItems.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(){
                        ShopContent(
                            scrollState = scrollState,
                            onBuyItemChange = { buyItemOverlay = it},
                            buyItemOverlay = buyItemOverlay,
                            uiState=uiState
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


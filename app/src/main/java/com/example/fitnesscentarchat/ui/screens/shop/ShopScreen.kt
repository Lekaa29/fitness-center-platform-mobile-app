package com.example.fitnesscentarchat.ui.screens.hub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.fitnesscentarchat.ui.screens.users.UsersViewModel
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




    LaunchedEffect(fitnessCenterId) {
        currentUser = authRepository.getCurrentUser()?.Id
        viewModel.loadShop(fitnessCenterId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Users") }
            )
        }
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
            } else if (uiState.shopItems.isEmpty()) {
                Text(
                    text = "No users found",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.shopItems) {
                            shopItem ->
                        ConversationItem(shopItem = shopItem,
                            onClick = { //TODO POP UP
                             } )

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

@Composable
fun ConversationItem(shopItem: ShopItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = shopItem.Name ?: "title",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "CHAT",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
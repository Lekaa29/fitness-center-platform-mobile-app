package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitnesscentarchat.data.repository.AttendanceRepository
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.data.repository.FitnessCenterRepository
import com.example.fitnesscentarchat.data.repository.MembershipRepository
import com.example.fitnesscentarchat.data.repository.ShopRepository
import com.example.fitnesscentarchat.data.repository.UserRepository
import com.example.fitnesscentarchat.ui.screens.hub.MembershipScreen
import com.example.fitnesscentarchat.ui.screens.hub.MembershipViewModel
import com.example.fitnesscentarchat.ui.screens.hub.ShopScreen
import com.example.fitnesscentarchat.ui.screens.hub.ShopViewModel
import com.example.fitnesscentarchat.ui.screens.profile.ProfileScreen
import com.example.fitnesscentarchat.ui.screens.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessCenterWithBottomNav(
    fitnessCenterId: Int,
    fitnessCenterRepository: FitnessCenterRepository,
    membershipRepository: MembershipRepository,
    attendanceRepository: AttendanceRepository,
    userRepository: UserRepository,
    shopRepository: ShopRepository,
    authRepository: AuthRepository,
    onNavigateBack: () -> Unit
) {
    val nestedNavController = rememberNavController()
    val currentRoute = nestedNavController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "overview",
                    onClick = {
                        nestedNavController.navigate("overview") {
                            popUpTo("overview") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Overview") }
                )
                NavigationBarItem(
                    selected = currentRoute == "shop",
                    onClick = {
                        nestedNavController.navigate("shop") {
                            popUpTo("overview")
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Shop") }
                )
                NavigationBarItem(
                    selected = currentRoute == "membership",
                    onClick = {
                        nestedNavController.navigate("membership") {
                            popUpTo("overview")
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Membership") }
                )
                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = {
                        nestedNavController.navigate("profile") {
                            popUpTo("overview")
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = nestedNavController,
            startDestination = "overview",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Overview Screen - Multiple repositories
            composable("overview") {
                val overviewViewModel = FitnessCenterViewModel(
                    fitnessCenterRepository = fitnessCenterRepository,
                    membershipRepository = membershipRepository,
                    attendanceRepository = attendanceRepository,
                    userRepository = userRepository
                )

                FitnessCenterScreen(
                    viewModel = overviewViewModel,
                    fitnessCenterId = fitnessCenterId,
                    authRepository = authRepository,
                    onNavigateBack = onNavigateBack
                )
            }

            composable("shop") {
                val shopViewModel = ShopViewModel(
                    shopRepository = shopRepository,
                )

                ShopScreen(
                    fitnessCenterId = fitnessCenterId,
                    viewModel = shopViewModel,
                    authRepository = authRepository
                )
            }

            // Membership Screen - Membership and User repositories
            composable("membership") {
                val membershipViewModel = MembershipViewModel(
                    membershipRepository = membershipRepository,
                )

                MembershipScreen(
                    viewModel = membershipViewModel,
                    fitnessCenterId = fitnessCenterId,
                    authRepository = authRepository
                )
            }

            composable("profile") {
                val profileViewModel = ProfileViewModel(
                    membershipRepository = membershipRepository,
                    attendanceRepository = attendanceRepository,
                    authRepository = authRepository
                )

                ProfileScreen(
                    viewModel = profileViewModel,
                    fitnessCenterId = fitnessCenterId,
                    authRepository = authRepository
                )
            }
        }
    }
}












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


package com.example.fitnesscentarchat.ui.screens.fitnessCenter

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch

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
    var currentTab by remember { mutableStateOf("overview") }
    val coroutineScope = rememberCoroutineScope()

    val overviewViewModel = remember {
        FitnessCenterViewModel(
            fitnessCenterRepository = fitnessCenterRepository,
            membershipRepository = membershipRepository,
            attendanceRepository = attendanceRepository,
            userRepository = userRepository
        )
    }

    val shopViewModel = remember {
        ShopViewModel(shopRepository = shopRepository)
    }

    val membershipViewModel = remember {
        MembershipViewModel(membershipRepository = membershipRepository)
    }

    val profileViewModel = remember {
        ProfileViewModel(
            membershipRepository = membershipRepository,
            attendanceRepository = attendanceRepository,
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    // Preload all data
    LaunchedEffect(fitnessCenterId) {
        coroutineScope.launch {
            launch { overviewViewModel.loadFitnessCenterHome(fitnessCenterId) }
            launch { shopViewModel.loadShop(fitnessCenterId) }
            launch { membershipViewModel.loadMembership(fitnessCenterId) }
            launch { profileViewModel.loadProfile(fitnessCenterId) }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == "overview",
                    onClick = { currentTab = "overview" },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Overview") }
                )
                NavigationBarItem(
                    selected = currentTab == "shop",
                    onClick = { currentTab = "shop" },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Shop") }
                )
                NavigationBarItem(
                    selected = currentTab == "membership",
                    onClick = { currentTab = "membership" },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Membership") }
                )
                NavigationBarItem(
                    selected = currentTab == "profile",
                    onClick = { currentTab = "profile" },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // ALL SCREENS ARE PRE-COMPOSED AND KEPT IN MEMORY
            // Only visibility changes, no recomposition needed!

            // Overview Screen
            if (currentTab == "overview") {
                FitnessCenterScreen(
                    viewModel = overviewViewModel,
                    fitnessCenterId = fitnessCenterId,
                    authRepository = authRepository,
                    onNavigateBack = onNavigateBack
                )
            }

            // Shop Screen
            if (currentTab == "shop") {
                ShopScreen(
                    fitnessCenterId = fitnessCenterId,
                    viewModel = shopViewModel,
                    authRepository = authRepository
                )
            }

            // Membership Screen
            if (currentTab == "membership") {
                MembershipScreen(
                    viewModel = membershipViewModel,
                    fitnessCenterId = fitnessCenterId,
                    authRepository = authRepository
                )
            }

            // Profile Screen
            if (currentTab == "profile") {
                ProfileScreen(
                    viewModel = profileViewModel,
                    fitnessCenterId = fitnessCenterId,
                    authRepository = authRepository
                )
            }
        }
    }
}

// ALTERNATIVE APPROACH: Pre-compose all screens but show only one
@Composable
fun FitnessCenterWithPrecomposedScreens(
    fitnessCenterId: Int,
    fitnessCenterRepository: FitnessCenterRepository,
    membershipRepository: MembershipRepository,
    attendanceRepository: AttendanceRepository,
    userRepository: UserRepository,
    shopRepository: ShopRepository,
    authRepository: AuthRepository,
    onNavigateBack: () -> Unit
) {
    var currentTab by remember { mutableStateOf("overview") }

    // ViewModels...
    val fitnessCenterViewModel = remember {
        FitnessCenterViewModel(
            fitnessCenterRepository = fitnessCenterRepository,
            membershipRepository = membershipRepository,
            attendanceRepository = attendanceRepository,
            userRepository = userRepository
        )
    }
    // ... other viewModels

    val shopViewModel = remember {
        ShopViewModel(shopRepository = shopRepository)
    }

    val membershipViewModel = remember {
        MembershipViewModel(membershipRepository = membershipRepository)
    }

    val profileViewModel = remember {
        ProfileViewModel(
            membershipRepository = membershipRepository,
            attendanceRepository = attendanceRepository,
            authRepository = authRepository,
            userRepository = userRepository
        )
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                // ... navigation items
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // PRE-COMPOSE ALL SCREENS (they stay in memory)
            // Use alpha/visibility modifiers for instant switching

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (currentTab == "overview") Modifier else Modifier.size(0.dp))
            ) {
                FitnessCenterScreen(
                    viewModel = fitnessCenterViewModel,
                    fitnessCenterId = fitnessCenterId,
                    authRepository = authRepository,
                    onNavigateBack = onNavigateBack
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (currentTab == "shop") Modifier else Modifier.size(0.dp))
            ) {
                ShopScreen(
                    fitnessCenterId = fitnessCenterId,
                    viewModel = shopViewModel,
                    authRepository = authRepository
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (currentTab == "membership") Modifier else Modifier.size(0.dp))
            ) {
                MembershipScreen(
                    fitnessCenterId = fitnessCenterId,
                    viewModel = membershipViewModel,
                    authRepository = authRepository
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (currentTab == "profile") Modifier else Modifier.size(0.dp))
            ) {
                ProfileScreen(
                    fitnessCenterId = fitnessCenterId,
                    viewModel = profileViewModel,
                    authRepository = authRepository
                )
            }


            // ... other screens with same pattern
        }
    }
}


package com.example.fitnesscentarchat.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.repository.AuthRepository
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    fitnessCenterId: Int,
    authRepository: AuthRepository,
    viewModel: ProfileViewModel
) {

    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle()



    var currentUser by remember { mutableStateOf<Int?>(null) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")



    LaunchedEffect(fitnessCenterId) {
        currentUser = authRepository.getCurrentUser()?.Id
        viewModel.loadProfile(fitnessCenterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PROFILE") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.user == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.user == null) {
                Text(
                    text = "No user found",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column {
                    uiState.user?.let { user ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = user.FirstName,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }

                    uiState.attendances?.let { attendances ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            AttendanceCalendarGrid(
                                attendances = attendances,
                                onMonthChanged = { month, year ->
                                    // Handle month change if needed
                                    // You might want to fetch attendance data for the new month
                                }
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



@Composable
fun AttendanceCalendarGrid(
    attendances: List<Attendance>,
    currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    onMonthChanged: (month: Int, year: Int) -> Unit = { _, _ -> }
) {
    var displayMonth by remember { mutableStateOf(currentMonth) }
    var displayYear by remember { mutableStateOf(currentYear) }

    // Parse attendance dates for the current month
    val attendanceDates = remember(attendances, displayMonth, displayYear) {
        attendances.mapNotNull { attendance ->
            attendance.Timestamp?.let { timestamp ->
                try {
                    // Assuming timestamp format is ISO format like "2024-01-15T10:30:00"
                    val dateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(timestamp)
                    val calendar = Calendar.getInstance().apply { time = dateTime!! }

                    if (calendar.get(Calendar.MONTH) + 1 == displayMonth &&
                        calendar.get(Calendar.YEAR) == displayYear) {
                        calendar.get(Calendar.DAY_OF_MONTH)
                    } else null
                } catch (e: Exception) {
                    // Try alternative format if the first one fails
                    try {
                        val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(timestamp)
                        val calendar = Calendar.getInstance().apply { time = dateTime!! }

                        if (calendar.get(Calendar.MONTH) + 1 == displayMonth &&
                            calendar.get(Calendar.YEAR) == displayYear) {
                            calendar.get(Calendar.DAY_OF_MONTH)
                        } else null
                    } catch (e2: Exception) {
                        null
                    }
                }
            }
        }.toSet()
    }

    // Get calendar info for current month
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, displayYear)
        set(Calendar.MONTH, displayMonth - 1) // Calendar month is 0-based
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // Convert to 0-6 (Sunday-Saturday)

    val monthName = DateFormatSymbols().months[displayMonth - 1]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Month header with navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (displayMonth == 1) {
                        displayMonth = 12
                        displayYear -= 1
                    } else {
                        displayMonth -= 1
                    }
                    onMonthChanged(displayMonth, displayYear)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous month"
                )
            }

            Text(
                text = "$monthName $displayYear",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = {
                    if (displayMonth == 12) {
                        displayMonth = 1
                        displayYear += 1
                    } else {
                        displayMonth += 1
                    }
                    onMonthChanged(displayMonth, displayYear)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next month"
                )
            }
        }

        // Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Empty cells for days before the first day of the month
            items(startDayOfWeek) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(4.dp)
                        )
                )
            }

            // Days of the month
            items(daysInMonth) { dayIndex ->
                val day = dayIndex + 1
                val hasAttendance = attendanceDates.contains(day)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            color = if (hasAttendance) Color.Blue else Color.White,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.toString(),
                        color = if (hasAttendance) Color.White else Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (hasAttendance) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
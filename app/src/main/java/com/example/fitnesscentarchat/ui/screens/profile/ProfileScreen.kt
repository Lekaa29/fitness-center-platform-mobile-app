package com.example.fitnesscentarchat.ui.screens.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnesscentarchat.data.models.Attendance
import com.example.fitnesscentarchat.data.models.UploadProgress
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.data.repository.AuthRepository
import com.example.fitnesscentarchat.data.repository.CloudflareUploader
import com.example.fitnesscentarchat.data.repository.ImageUploadRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
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
    // Collect the StateFlow
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    // Image upload states
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }

    // Get context at the composable level
    val context = LocalContext.current

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedUri ->
            coroutineScope.launch {
                try {
                    isUploading = true
                    uploadError = null

                    // Convert URI to File
                    val file = uriToFile(context, selectedUri)

                    // Upload to Cloudflare
                    val cloudflareUploader = CloudflareUploader()
                    val result = cloudflareUploader.uploadImage(file)

                    result.fold(
                        onSuccess = { imageResult ->
                            // Get the image URL
                            val imageUrl = cloudflareUploader.getImageUrls(imageResult.id).original

                            // Send to viewModel
                            viewModel.updateUser(imageUrl)

                            isUploading = false
                        },
                        onFailure = { error ->
                            uploadError = error.message
                            isUploading = false
                        }
                    )

                    // Clean up temp file
                    file.delete()

                } catch (e: Exception) {
                    uploadError = e.message
                    isUploading = false
                }
            }
        }
    }

    LaunchedEffect(fitnessCenterId) {
        coroutineScope.launch {
            launch {
                viewModel.loadProfile(fitnessCenterId)
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PROFILE") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Always show current state for debugging
            Spacer(modifier = Modifier.height(16.dp))

// Upload Image Button
            Button(
                onClick = {
                    if (!isUploading) {
                        imagePickerLauncher.launch("image/*")
                    }
                },
                enabled = !isUploading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isUploading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Uploading Image...")
                    }
                } else {
                    Text("Upload Profile Image")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading && uiState.user == null -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Loading profile...")
                        }
                    }
                }

                uiState.error != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Error occurred:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = uiState.error!!,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.clearError()
                                    viewModel.loadProfile(fitnessCenterId)
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                else -> {
                    // User info
                    uiState.user?.let { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "User Profile",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Name: ${user.FirstName}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                // Add more user details if available
                                Text(
                                    text = "ID: ${user.Id}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    } ?: run {
                        Text(
                            text = "No user data available",
                            color = MaterialTheme.colorScheme.error
                        )
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

private fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw Exception("Cannot open input stream")

    // Create temp file in cache directory
    val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)

    FileOutputStream(tempFile).use { output ->
        inputStream.use { input ->
            input.copyTo(output)
        }
    }

    return tempFile
}



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
import androidx.compose.foundation.rememberScrollState
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
import com.example.fitnesscentarchat.ui.screens.profile.components.ProfileContent
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

    val scrollState = rememberScrollState()

    var topTextOffsetY by remember { mutableStateOf(0f) }

    var editProfileOverlay by remember { mutableStateOf(false) }

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



        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {



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


                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(){
                            ProfileContent(
                                uiState,
                                viewModel=viewModel,
                                scrollState = scrollState,
                                onTopTextPositioned = { topTextOffsetY = it },
                                onEditProfileChange = { editProfileOverlay = it},
                                editProfileOverlay = editProfileOverlay,
                                imagePickerLauncher = imagePickerLauncher,
                                isUploading = isUploading,
                                uploadError = uploadError
                            )
                        }

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

/*

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
 */
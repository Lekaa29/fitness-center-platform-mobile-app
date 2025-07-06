package com.example.fitnesscentarchat.ui.screens.login

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.sin

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHub: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isInLoginMode by remember { mutableStateOf(true) }

    // Animated gradient colors
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )

    // Color interpolation for smooth gradient animation
    val gradientColors = listOf(
        Color(0xF015152C), // Dark blue
        Color(0xFF000B3C), // Darker blue
        Color(0xFF213B61), // Navy blue
        Color(0xFF13022E), // Purple
        Color(0xFF0D0D1B)  // Back to start
    )

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onNavigateToHub()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = gradientColors,
                    start = androidx.compose.ui.geometry.Offset(
                        x = animatedOffset * 1000f,
                        y = animatedOffset * 1000f
                    ),
                    end = androidx.compose.ui.geometry.Offset(
                        x = (1f - animatedOffset) * 1000f,
                        y = (1f - animatedOffset) * 1000f
                    )
                )
            )
    ) {




        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Name
            Text(
                text = "Fitness Centar",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )

            Text(
                text = "Transform Your Life",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Glass morphism card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isInLoginMode) "Welcome Back" else "Join Us",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Username field
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = Color.White.copy(alpha = 0.7f)) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = Color.White.copy(alpha = 0.8f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Login/Register Button
                    Button(
                        onClick = {
                            if (isInLoginMode) {
                                viewModel.login(username, password)
                            } else {
                                viewModel.register(username, password)
                            }
                        },
                        enabled = username.isNotBlank() && password.isNotBlank() &&
                                uiState !is LoginUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.9f),
                            disabledContainerColor = Color.White.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (uiState is LoginUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF1A1A2E),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (isInLoginMode) "Login" else "Register",
                                color = Color(0xFF1A1A2E),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Toggle button
                    TextButton(
                        onClick = { isInLoginMode = !isInLoginMode }
                    ) {
                        Text(
                            text = if (isInLoginMode) "Don't have an account? Register"
                            else "Already have an account? Login",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }

                    // Error message
                    if (uiState is LoginUiState.Error) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Red.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = (uiState as LoginUiState.Error).message,
                                color = Color.Red.copy(alpha = 0.9f),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer text
            Text(
                text = "Your fitness journey starts here",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light
            )
        }
    }
}
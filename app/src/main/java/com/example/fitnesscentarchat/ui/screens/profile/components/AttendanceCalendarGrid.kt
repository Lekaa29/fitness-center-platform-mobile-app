package com.example.fitnesscentarchat.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.Attendance
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
            attendance.timestamp?.let { timestamp ->
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
            .fillMaxWidth(0.85F)
            .height(500.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month header with navigation
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
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
                    contentDescription = "Previous month",
                    tint = Color.White

                )
            }

            Text(
                text = "$monthName $displayYear",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(0xFFFFFFFF)
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
                    contentDescription = "Next month",
                    tint = Color.White

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
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
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
                            color = if (hasAttendance) Color(0xFFFFFFFF) else Color(0xD3535353),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                }
            }
        }
    }
}
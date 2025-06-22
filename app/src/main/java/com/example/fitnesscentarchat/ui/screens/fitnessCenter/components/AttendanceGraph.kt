package com.example.fitnesscentarchat.ui.screens.fitnessCenter.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.Attendance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.random.Random


@Composable
fun DateRangeAnalyzer(attendances: List<Attendance>, onBackClick: () -> Unit) {
    var weekOffset by remember { mutableStateOf(0) }


    val now = LocalDateTime.now().minusWeeks(weekOffset.toLong())
    val startOfWeek = now.minusDays((now.dayOfWeek.value - 1).toLong()) // Monday
    val endOfWeek = startOfWeek.plusDays(6) // Sunday
    val startDate = startOfWeek.toLocalDate()
    val endDate = endOfWeek.toLocalDate()

    val filteredData = attendances.filter { attendance ->
        val attendanceDate = LocalDateTime.parse(attendance.timestamp).toLocalDate()
        attendanceDate.isEqual(startDate) ||
                attendanceDate.isEqual(endDate) ||
                (attendanceDate.isAfter(startDate) && attendanceDate.isBefore(endDate))
    }

    val dailyCounts = (0..6).map { dayOffset ->
        val targetDate = startOfWeek.plusDays(dayOffset.toLong()).toLocalDate()
        filteredData.count { attendance ->
            LocalDateTime.parse(attendance.timestamp).toLocalDate() == targetDate
        }
    }


    val average = if (dailyCounts.isNotEmpty()) dailyCounts.average().toFloat() else 0f
    val maxCount = dailyCounts.maxOrNull() ?: 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp)
    ) {
        // Top Bar with Back Button and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Attendance Graph",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(40.dp))
        }



        Spacer(modifier = Modifier.height(32.dp))

        // Header with date range and navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { weekOffset++ },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x00AAAAAA))
            ) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous weeks",
                    tint = Color(0xFFFFFFFF)
                )
            }

            Text(
                text = "${startOfWeek.format(DateTimeFormatter.ofPattern("d.M"))} - ${endOfWeek.format(
                    DateTimeFormatter.ofPattern("d.M"))}",
                fontSize = 20.sp,
                color = Color(0xFFFFFFFF)
            )

            IconButton(
                onClick = { weekOffset-- },
                enabled = weekOffset > 0,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (weekOffset > 0) Color(0x00AAAAAA) else Color(0x00AAAAAA))
            ) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next weeks",
                    tint = if (weekOffset > 0) Color(0xFF000000) else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Chart
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF000000)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {


                Spacer(modifier = Modifier.height(16.dp))

                Canvas(
                    modifier = Modifier.fillMaxSize()
                        .padding(top=16.dp)

                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height - 40.dp.toPx()
                    val barWidth = canvasWidth / 7f * 0.7f
                    val barSpacing = canvasWidth / 7f

                    // Draw bars
                    dailyCounts.forEachIndexed { index, count ->
                        val barHeight = if (maxCount > 0) (count.toFloat() / maxCount) * canvasHeight else 0f
                        val x = index * barSpacing + barSpacing * 0.15f
                        val y = canvasHeight - barHeight

                        drawRect(
                            color = Color(0xFFF3B821),
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight)
                        )

                        // Draw count text on top of bar
                        drawContext.canvas.nativeCanvas.drawText(
                            count.toString(),
                            x + barWidth / 2,
                            y - 10.dp.toPx(),
                            Paint().apply {
                                color = android.graphics.Color.parseColor("#FFFFFF")
                                textSize = 12.sp.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }

                    // Draw average line
                    if (maxCount > 0) {
                        val avgY = canvasHeight - (average / maxCount) * canvasHeight
                        drawLine(
                            color = Color(0xC9D1D1D1),
                            start = Offset(0f, avgY),
                            end = Offset(canvasWidth, avgY),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                        )

                        // Average label
                        drawContext.canvas.nativeCanvas.drawText(
                            "Avg: ${String.format("%.1f", average)}",
                            canvasWidth - 60.dp.toPx(),
                            avgY - 8.dp.toPx(),
                            Paint().apply {
                                color = android.graphics.Color.parseColor("#D1D1D1")
                                textSize = 11.sp.toPx()
                            }
                        )
                    }

                    // Draw day labels
                    val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    dayLabels.forEachIndexed { index, day ->
                        val x = index * barSpacing + barSpacing / 2
                        drawContext.canvas.nativeCanvas.drawText(
                            day,
                            x,
                            canvasHeight + 25.dp.toPx(),
                            Paint().apply {
                                color = android.graphics.Color.parseColor("#666666")
                                textSize = 10.sp.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard("Total", filteredData.size.toString(), Color(0xFFFFFFFF))
            StatCard("Average", String.format("%.1f", average), Color(0xFFFDC282))
            StatCard("Max Day", maxCount.toString(), Color(0xFFFFFFFF))
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color) {
    Card(
        modifier = Modifier.width(100.dp).height(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF000000)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

fun generateDataList(size: Int): List<LocalDateTime> {
    val now = LocalDateTime.now()
    val startDate = now.minusWeeks(4) // Generate data for last 4 weeks

    return (1..size).map {
        val randomDaysBack = Random.nextLong(0, ChronoUnit.DAYS.between(startDate, now))
        val randomHours = Random.nextLong(0, 24)
        val randomMinutes = Random.nextLong(0, 60)

        startDate.plusDays(randomDaysBack).plusHours(randomHours).plusMinutes(randomMinutes)
    }.sortedDescending()
}















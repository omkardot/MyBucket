package com.omkar.mybucket.feature.dashboard.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omkar.mybucket.R
import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents

import com.omkar.mybucket.feature.dashboard.components.MetricCard
import com.omkar.mybucket.feature.responsibility.presentation.detail.calculateTimeSpentInMins
import com.omkar.mybucket.ui.theme.Hankengrotesk
import com.omkar.mybucket.ui.theme.toComposeColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onItemClick: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.White,

        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                modifier = Modifier.shadow(elevation = 4.dp),
                title = {
                    Text(
                        text = "Dashboard",
                        fontFamily = Hankengrotesk
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle icon click */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_my_bucket),
                            contentDescription = "Navigation Menu",
                            tint = Color.Unspecified // Preserves full original image colors
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = "#fbf9f8".toComposeColor() // Sets the TopAppBar background color
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(containerColor = "#005cbb".toComposeColor(), onClick = onAddClick) {
                Icon(Icons.Default.Add, tint = "#c7d9ff".toComposeColor(), contentDescription = "Add Item")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = "#fbf9f8".toComposeColor())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Good morning, Omkar",
                    fontSize = 28.sp,
                    fontFamily = Hankengrotesk,
                    color = "#1B1C1C".toComposeColor()
                )
                Text(
                    text = "Here is a summary of your workspace.",
                    fontSize = 16.sp,
                    fontFamily = Hankengrotesk,
                    color = "#424752".toComposeColor()
                )
                Spacer(modifier = Modifier.padding(0.dp,16.dp,0.dp,0.dp))
                Text(
                    text = "Active Responsibilities",
                    fontSize = 22.sp,
                    fontFamily = Hankengrotesk,
                    color = "#1b1c1c".toComposeColor()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                when (val state = uiState) {

                    is DashboardUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is DashboardUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is DashboardUiState.Success -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Summary Grid
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        MetricCard(
                                            title = "ONGOING",
                                            count = state.metrics.inProgressCount,
                                            modifier = Modifier.weight(1f),
                                            fontFamily = Hankengrotesk
                                        )
                                        MetricCard(
                                            title = "BLOCKED",
                                            count = state.metrics.blockedCount,
                                            modifier = Modifier.weight(1f),
                                            badgeColor = "#ba1a1a".toComposeColor(),
                                            icon = Icons.Default.Block,
                                            decorativeCircleColor = "#0cba1a1a".toComposeColor(),
                                            batchBackgroundColor ="#f5f3f3".toComposeColor()
                                        )
                                    }
                                }
                            }

                            // Active Responsibilities Header
                            item {
                                Text(
                                    text = "Recent Activity",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = 8.dp),
                                    color = "#1b1c1c".toComposeColor(),
                                    fontFamily = Hankengrotesk,
                                    fontSize = 22.sp
                                )
                            }

                            // Active Items List
                            if (state.activeResponsibilities.isEmpty()) {
                                item {
                                    Text(
                                        text = "No Recent Activities.",
                                        fontFamily = Hankengrotesk,
                                        fontStyle = FontStyle.Normal,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            } else {

                                val topResponsibilities = state.activeResponsibilities.take(3)

                                itemsIndexed(
                                    items = topResponsibilities,
                                    key = { _, item -> item.responsibility.id }
                                ) { index, item ->
                                    ElevatedCard(
                                        onClick = { onItemClick(item.responsibility.id) },
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.Transparent,
                                            contentColor = Color(0xFF1B1C1C)
                                        )
                                    ) {
                                        TimelineItem(
                                            item = item,
                                            isLastItem = index == topResponsibilities.lastIndex
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun TimelineItem(
    item: ResponsibilityWithEvents, // Replace with your model type
    isLastItem: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Allows the line to stretch to full row height
    ) {
        // Left Column: Icon + Timeline Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_my_bucket),
                contentDescription = "logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )

            // Timeline line connecting to the next item
            if (!isLastItem) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFE0E0E0)) // Light gray timeline line
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right Column: Title and Subtitle Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 24.dp) // Spacing between timeline nodes
        ) {
            Text(
                text = item.responsibility.title,
                fontFamily = Hankengrotesk,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                fontFamily = Hankengrotesk,
                text = calculateLastUpdateOnTime(item.responsibility.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
fun calculateLastUpdateOnTime(timestamp: Long): String {
    val now = Calendar.getInstance()
    val updatedTime = Calendar.getInstance().apply { timeInMillis = timestamp }

    val diffInMillis = now.timeInMillis - updatedTime.timeInMillis
    val diffInMinutes = diffInMillis / (1000 * 60)
    val diffInHours = diffInMinutes / 60

    // Time Formatter for hours/minutes (e.g., 2:30 PM)
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    // 1. Just now
    if (diffInMinutes < 1) {
        return "Just now"
    }

    // 2. Updated today (Less than 60 mins -> minutes ago)
    if (diffInMinutes < 60) {
        return "$diffInMinutes min${if (diffInMinutes > 1) "s" else ""} ago"
    }

    // 3. Updated today (Same day check)
    val isSameDay = now.get(Calendar.YEAR) == updatedTime.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == updatedTime.get(Calendar.DAY_OF_YEAR)

    if (isSameDay) {
        return "$diffInHours hr${if (diffInHours > 1) "s" else ""} ago"
    }

    // 4. Updated yesterday
    val yesterday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
    val isYesterday = yesterday.get(Calendar.YEAR) == updatedTime.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == updatedTime.get(Calendar.DAY_OF_YEAR)

    if (isYesterday) {
        return "Yesterday, ${timeFormat.format(Date(timestamp))}"
    }

    // 5. Older than yesterday (e.g., 14 Aug, 2:30 PM)
    val dateFormat = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
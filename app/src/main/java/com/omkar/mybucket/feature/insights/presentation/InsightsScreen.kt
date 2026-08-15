package com.omkar.mybucket.feature.insights.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Analytics & Insights") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is InsightsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is InsightsUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is InsightsUiState.Success -> {
                    val data = state.data
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // High-Level Velocity Stats
                        item {
                            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Performance Overview", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("Completion Rate", style = MaterialTheme.typography.labelMedium)
                                            Text("${data.completionRatePercentage}%", style = MaterialTheme.typography.headlineSmall)
                                        }
                                        Column {
                                            Text("Total Time Logged", style = MaterialTheme.typography.labelMedium)
                                            Text("${data.totalTimeSpentMinutes / 60}h ${data.totalTimeSpentMinutes % 60}m", style = MaterialTheme.typography.headlineSmall)
                                        }
                                    }
                                }
                            }
                        }

                        // Project Time Allocation Breakdown
                        item {
                            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Time Distribution by Project", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    if (data.projectDistributions.isEmpty()) {
                                        Text("No logged time data yet.", style = MaterialTheme.typography.bodySmall)
                                    } else {
                                        data.projectDistributions.forEach { dist ->
                                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(dist.projectName, style = MaterialTheme.typography.bodyMedium)
                                                    Text("${dist.totalMinutes} mins (${dist.percentage.toInt()}%)", style = MaterialTheme.typography.labelSmall)
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                LinearProgressIndicator(
                                                    progress = { dist.percentage / 100f },
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Bottlenecks & Friction Points Card
                        item {
                            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Bottlenecks & Blockers", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    if (data.topBottlenecks.isEmpty()) {
                                        Text("No blockers recorded. System running smoothly!", style = MaterialTheme.typography.bodySmall)
                                    } else {
                                        data.topBottlenecks.forEach { bottleneck ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text("Stage: ${bottleneck.stageName}", style = MaterialTheme.typography.bodyMedium)
                                                Text("${bottleneck.count} incidents", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
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
    }
}
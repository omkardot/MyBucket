package com.omkar.mybucket.feature.responsibility.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val availableStages = listOf("To-Do", "In Progress", "Blocked", "Review", "Completed")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResponsibilityDetailScreen(
    viewModel: ResponsibilityDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    (uiState as? DetailUiState.Success)?.let { state ->
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(
                                imageVector = if (state.isEditing) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = if (state.isEditing) "Done" else "Edit"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUiState.Success -> {
                    val task = state.itemWithEvents.responsibility

                    var title by remember(task.title) { mutableStateOf(task.title) }
                    var description by remember(task.description) { mutableStateOf(task.description) }
                    var project by remember(task.project) { mutableStateOf(task.project) }
                    var priority by remember(task.priority) { mutableStateOf(task.priority) }
                    var estimatedHours by remember(task.estimatedHours) { mutableStateOf(task.estimatedHours.toString()) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Status/Stage Selector Card
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Current Status", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    availableStages.forEach { stage ->
                                        FilterChip(
                                            selected = task.currentStage == stage,
                                            onClick = { viewModel.updateStatus(stage) },
                                            label = { Text(stage, style = MaterialTheme.typography.bodySmall) }
                                        )
                                    }
                                }
                            }
                        }

                        // 2. Editable / Viewable Task Details Card
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Task Information", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(12.dp))

                                if (state.isEditing) {
                                    OutlinedTextField(
                                        value = title,
                                        onValueChange = { title = it },
                                        label = { Text("Task Title") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = description,
                                        onValueChange = { description = it },
                                        label = { Text("Description") },
                                        modifier = Modifier.fillMaxWidth(),
                                        minLines = 3
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = project,
                                        onValueChange = { project = it },
                                        label = { Text("Project Name") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = priority,
                                        onValueChange = { priority = it },
                                        label = { Text("Priority (High, Medium, Low)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = estimatedHours,
                                        onValueChange = { estimatedHours = it },
                                        label = { Text("Estimated Hours") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            viewModel.saveTaskDetails(
                                                title = title,
                                                description = description,
                                                project = project,
                                                priority = priority,
                                                estimatedHours = estimatedHours.toIntOrNull() ?: task.estimatedHours
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Save Changes")
                                    }
                                } else {
                                    Text("Title: ${task.title}", style = MaterialTheme.typography.titleLarge)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Project: ${task.project}", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Priority: ${task.priority}", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Est. Time: ${task.estimatedHours} Hours", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Description:", style = MaterialTheme.typography.labelMedium)
                                    Text(
                                        text = task.description.ifBlank { "No description provided." },
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // 3. Activity History Card
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Activity Timeline", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(8.dp))
                                // Inside Activity History Card in ResponsibilityDetailScreen.kt:
                                if (state.itemWithEvents.events.isEmpty()) {
                                    Text("No logged events for this task.", style = MaterialTheme.typography.bodySmall)
                                } else {
                                    state.itemWithEvents.events.forEach { event ->
                                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                            Text(
                                                text = "• [${event.stage}] ${event.notes ?: "No notes"}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = "Time spent: ${event.timeSpentMinutes} mins",
                                                style = MaterialTheme.typography.labelSmall
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
}
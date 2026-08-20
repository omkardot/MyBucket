package com.omkar.mybucket.feature.responsibility.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omkar.mybucket.core.database.entity.LifecycleEventEntity
import com.omkar.mybucket.core.database.entity.ResponsibilityEntity
import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents
import com.omkar.mybucket.feature.dashboard.components.TaskDetailsCard
import com.omkar.mybucket.ui.theme.Hankengrotesk
import com.omkar.mybucket.ui.theme.MyBucketTheme
import com.omkar.mybucket.ui.theme.toComposeColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

val availableStages =
    listOf("Blocked", "Development", "Unit Testing", "SIT", "UAT", "Pre-prod", "Post-prod")

@Composable
fun ResponsibilityDetailScreen(
    viewModel: ResponsibilityDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var stageForDialog by remember { mutableStateOf<String?>(null) }
    ResponsibilityDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        saveUpdatedDiscriptionTodb = { discription, lifeCycle ->
            viewModel.saveUpdatedDiscriptionTodb(discription)
        },
        onUpdateStatus = { stage ->
            viewModel.updateStatus(stage,"",null)
        },
        onToggleEditMode = { viewModel.toggleEditMode() },
    )
}

fun calculateTimeSpentInMins(createdAt: Long): Int {
    val startTime = createdAt
    val endTime = System.currentTimeMillis()

    if (startTime >= endTime) return 0

    return calculateWorkingMinutes(startTime, endTime)
}
private fun calculateWorkingMinutes(startTimestamp: Long, endTimestamp: Long): Int {
    var totalWorkingMinutes = 0

    val currentCalendar = Calendar.getInstance().apply { timeInMillis = startTimestamp }
    val endCalendar = Calendar.getInstance().apply { timeInMillis = endTimestamp }

    // Loop through each minute from start to end
    while (currentCalendar.before(endCalendar)) {
        val hour = currentCalendar.get(Calendar.HOUR_OF_DAY)

        // Working window: 10 AM (10:00) to 7 PM (19:00)
        if (hour in 10..18) {
            totalWorkingMinutes++
        }
        currentCalendar.add(Calendar.MINUTE, 1)
    }

    return totalWorkingMinutes
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResponsibilityDetailContent(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    onToggleEditMode: () -> Unit,
    onUpdateStatus: (String) -> Unit,
    saveUpdatedDiscriptionTodb: (description: String, lifeCycle: String) -> Unit
) {
    // State for Success Dialog & Snackbar
    var showSuccessDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var activeStageDialog by remember { mutableStateOf<String?>(null) }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 4.dp),
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    (uiState as? DetailUiState.Success)?.let { state ->
                        IconButton(onClick = onToggleEditMode) {
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

        // Render Success Dialog when description is updated
        activeStageDialog?.let { stage ->
            val (question, label) = when (stage) {
                "Unit Testing" -> Pair("Is code review done?", "Yes, code review is complete")
                "SIT" -> Pair("Are all dependencies integrated?", "Yes, integration tests passed")
                "UAT" -> Pair("Is stakeholder sign-off ready?", "Yes, sign-off is verified")
                "Pre-prod" -> Pair("Is build package created?", "Yes, release package is ready")
                "Post-prod" -> Pair("Has deployment completed?", "Yes, deployed to production")
                else -> Pair("Are requirements met?", "Yes, proceed to next stage")
            }

            StageConfirmationDialog(
                stageName = stage,
                questionText = question,
                checkboxLabel = label,
                onDismiss = { activeStageDialog = null },
                onConfirm = { onUpdateStatus(stage) }
            )
        }
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Text(
                        text = "Task Updated",
                        fontFamily = Hankengrotesk,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Your description and task details have been saved successfully.",
                        fontFamily = Hankengrotesk
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            onBackClick()
                        }
                    ) {
                        Text("OK", fontWeight = FontWeight.Bold, color = "#00458f".toComposeColor())
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background("#fbf9f8".toComposeColor())
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

                    var description by remember(task.description) { mutableStateOf(task.description) }
                    var selectedLifecycle by remember { mutableStateOf("") }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = task.title,
                            fontFamily = Hankengrotesk,
                            fontSize = 28.sp,
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Lifecycle Stage",
                            fontFamily = Hankengrotesk,
                            fontSize = 22.sp,
                        )
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            availableStages.forEach { stage ->
                                val isSelected = task.currentStage == stage

                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        when (stage) {
                                            "Unit Testing", "SIT", "UAT", "Pre-prod", "Post-prod" -> {
                                                activeStageDialog =
                                                    stage
                                            }

                                            else -> {
                                                onUpdateStatus(stage)
                                            }
                                        }
                                    },
                                    label = {
                                        Text(
                                            stage,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = "#efeded".toComposeColor(),
                                        selectedContainerColor = "#005cbb".toComposeColor(),
                                        labelColor = "#424752".toComposeColor(),
                                        selectedLabelColor = "#c7d9ff".toComposeColor()
                                    ),
                                    border = BorderStroke(0.dp, "#fbf9f8".toComposeColor())
                                )
                            }
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Task Details",
                            fontFamily = Hankengrotesk,
                            fontSize = 22.sp,
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TaskDetailsCard(
                                title = task.codestack,
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.PhoneIphone,
                                icontint = "#005050".toComposeColor(),
                                stack = "Android",
                                textToShow = "Stack",
                                textColor = "#1b1c1c".toComposeColor(),
                                backgroundColor = "#f5f3f3".toComposeColor(),
                                stacktextColor = "#424752".toComposeColor()
                            )
                            TaskDetailsCard(
                                title = task.priority,
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.PriorityHigh,
                                stack = "High",
                                textToShow = "Priority",
                                icontint = "#93000a".toComposeColor(),
                                textColor = "#93000a".toComposeColor(),
                                backgroundColor = "#ffdad6".toComposeColor(),
                                stacktextColor = "#93000a".toComposeColor()
                            )
                        }

                        Card(
                            modifier = Modifier.height(80.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = "#f5f3f3".toComposeColor()
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp)),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 20.dp)
                                ) {
                                    Text(
                                        text = "Assigned By",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = Hankengrotesk,
                                        color = "#424752".toComposeColor()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(
                                                    Color.Transparent,
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }

                                        Text(
                                            text = task.assignedBy,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = Hankengrotesk,
                                            color = "#1b1c1c".toComposeColor()
                                        )
                                    }
                                }
                            }
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Notes & Context",
                            fontFamily = Hankengrotesk,
                            fontSize = 22.sp,
                        )
                        MultilineOutlinedTextField(
                            value = description,
                            onValueChange = { newValue ->
                                description = newValue
                            }
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            content = { Text("Update Task") },
                            onClick = {
                                if (description.isNotEmpty()) {
                                    saveUpdatedDiscriptionTodb(description, selectedLifecycle)
                                    showSuccessDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = "#00458f".toComposeColor(),
                                disabledContentColor = Color.White,
                                disabledContainerColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MultilineOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "Initial Notes & Context..."
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = Color(0xFF8E8E93)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color(0xFF004B8D)
        )
    )
}
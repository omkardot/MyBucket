package com.omkar.mybucket.feature.responsibility.presentation.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omkar.mybucket.R
import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents
import com.omkar.mybucket.feature.responsibility.presentation.add_edit.AddResponsibilityDialog
import com.omkar.mybucket.ui.theme.Hankengrotesk
import com.omkar.mybucket.ui.theme.toComposeColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Composable
fun ResponsibilityListScreen(
    viewModel: ResponsibilityListViewModel,
    onItemClick: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                modifier = Modifier.shadow(elevation = 4.dp),
                title = {
                    Text(
                        text = "Tasks",
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
            FloatingActionButton(
                containerColor = "#005cbb".toComposeColor(),
                onClick = onAddClick
            ) {
                Icon(
                    Icons.Default.Add,
                    tint = "#c7d9ff".toComposeColor(),
                    contentDescription = "Add Item"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = "#fbf9f8".toComposeColor())
        ) {
            val uiState by viewModel.uiState.collectAsState()
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Active Responsibilities",
                    fontSize = 28.sp,
                    fontFamily = Hankengrotesk,
                    color = "#1B1C1C".toComposeColor()
                )
                Spacer(modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp))
                Text(
                    text = "Focus on these tasks for your current sprint.\n" +
                            "Prioritize unblocking items marked as critical.",
                    fontSize = 16.sp,
                    fontFamily = Hankengrotesk,
                    color = "#424752".toComposeColor()
                )
            }

            if (uiState is ResponsibilityListUiState.Success) {
                val state = uiState as ResponsibilityListUiState.Success

                HorizontalChipFilterRow(
                    selectedStage = state.selectedFilterStage ?: "All Tasks",
                    onStageSelected = { selectedStage ->
                        viewModel.onStageFilterSelect(selectedStage)
                    }
                )
            }
            when (val state = uiState) {
                is ResponsibilityListUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ResponsibilityListUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }

                is ResponsibilityListUiState.Success -> {
                    if (state.items.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No responsibilities found.\nTap + to create one!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.items,
                                key = { it.responsibility.id }
                            ) { item ->
                                ResponsibilityItemCard(
                                    item = item,
                                    onItemClick = onItemClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getItemsBySelectedState(
    items: List<ResponsibilityWithEvents>,
    selectedFilter: String
): List<ResponsibilityWithEvents> {
    return if (selectedFilter == "All Tasks") {
        items
    } else {
        items.filter { it.responsibility.currentStage.equals(selectedFilter, ignoreCase = true) }
    }
}

@Composable
fun HorizontalChipFilterRow(
    stages: List<String> = listOf(
        "All Tasks",
        "Blocked",
        "Development",
        "Unit Testing",
        "SIT",
        "UAT",
        "Pre-prod",
        "Post-prod"
    ),
    selectedStage: String,
    onStageSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(stages) { stage ->
            val isSelected = stage == selectedStage

            Surface(
                onClick = { onStageSelected(stage) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) Color(0xFF005CBB) else Color(0xFFF9F9FB),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) Color.Transparent else Color(0xFFDCE2F0)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stage,
                    color = if (isSelected) Color.White else Color(0xFF1B1C1C),
                    fontFamily = Hankengrotesk,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResponsibilityItemCard(
    item: ResponsibilityWithEvents,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = { onItemClick(item.responsibility.id) },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = "#f5f3f3".toComposeColor(), // or
            contentColor = "#1b1c1c".toComposeColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.responsibility.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontFamily = Hankengrotesk,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            if (item.responsibility.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.responsibility.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = Hankengrotesk,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (stageContainerColor, stageTextColor) = when (item.responsibility.currentStage) {
                    "Blocked" -> Pair("#FFDAD6".toComposeColor(), "#93000A".toComposeColor())
                    "Development" -> Pair("#E0F2FE".toComposeColor(), "#0369A1".toComposeColor())
                    "Unit Testing" -> Pair("#FEF3C7".toComposeColor(), "#B45309".toComposeColor())
                    "SIT" -> Pair("#E0E7FF".toComposeColor(), "#3730A3".toComposeColor())
                    "UAT" -> Pair("#F3E8FF".toComposeColor(), "#6B21A8".toComposeColor())
                    "Pre-prod" -> Pair("#FFEDD5".toComposeColor(), "#C2410C".toComposeColor())
                    "Post-prod" -> Pair("#DCFCE7".toComposeColor(), "#15803D".toComposeColor())
                    else -> Pair("#EFEDED".toComposeColor(), "#424752".toComposeColor())
                }
                SuggestionChip(
                    onClick = { },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = "#cde5fc".toComposeColor(),
                    ),
                    shape = CircleShape,
                    border = BorderStroke(0.dp, Color.Transparent),
                    label = {
                        Text(
                            "# " + item.responsibility.codestack,
                            color = "#50677a".toComposeColor(),
                            fontFamily = Hankengrotesk
                        )
                    }
                )
                SuggestionChip(
                    onClick = { },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = stageContainerColor,
                    ),
                    shape = CircleShape,
                    border = BorderStroke(0.dp, Color.Transparent),
                    label = {
                        Text(item.responsibility.currentStage, color = stageTextColor, fontFamily = Hankengrotesk)
                    }
                )
            }
        }
    }
}
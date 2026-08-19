package com.omkar.mybucket.feature.responsibility.presentation.add_edit

import androidx.compose.material.icons.filled.AddTask
import com.omkar.mybucket.R
import com.omkar.mybucket.feature.responsibility.presentation.list.ResponsibilityListViewModel


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omkar.mybucket.ui.theme.Hankengrotesk
import com.omkar.mybucket.ui.theme.toComposeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    fontFamily: FontFamily? = null,
    onTaskCreated: () -> Unit = {},
    viewModel: AddEditResponsibilityViewModel,
    onBackClick: () -> Unit = {}
) {
    // Form States
    var taskTitle by remember { mutableStateOf("") }
    var selectedStack by remember { mutableStateOf("Android") }
    var selectedPriority by remember { mutableStateOf("High") }
    var notes by remember { mutableStateOf("") }

    // Dropdown (Spinner) State for "Assigned By"
    var expanded by remember { mutableStateOf(false) }
    val assignedByOptions = listOf("Select Assignee", "Atish Sir", "Samrat Sir", "Love Sir","Kunal Sir")
    var selectedAssignedBy by remember { mutableStateOf(assignedByOptions[0]) }

    val stackOptions = listOf("Android", "Server", "RND","Self Learn")
    val priorityOptions = listOf("High", "Medium", "Low")
    var showSuccessDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 4.dp),
                title = {
                    Text(
                        text = "Add Tasks",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        color = Color(0xFF1B1C1C)
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_my_bucket),
                        contentDescription = "Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(start = 12.dp, end = 8.dp)
                            .size(36.dp)
                    )
                },
                actions = {
                    // Profile Avatar Placeholder
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF004B8D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JD",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFBF9F8)
                )
            )
        },
        containerColor = Color(0xFFFBF9F8)
    ) { paddingValues ->
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
                        text = "New Task Added Successfully.",
                        fontFamily = Hankengrotesk,
                        textAlign = TextAlign.Center
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Task Title Input
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                placeholder = {
                    Text(
                        text = "Task Title",
                        color = Color(0xFF8E8E93),
                        fontFamily = fontFamily
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF004B8D)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. STACK Section
            Text(
                text = "STACK",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                color = Color(0xFF5E6066),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)
            , verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                stackOptions.forEach { option ->
                    SelectableChip(
                        text = option,
                        isSelected = selectedStack == option,
                        fontFamily = fontFamily,
                        onClick = { selectedStack = option }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. PRIORITY Section
            Text(
                text = "PRIORITY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                color = Color(0xFF5E6066),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                priorityOptions.forEach { option ->
                    val selectedBg = when (option) {
                        "High" -> Color(0xFF004B8D)
                        "Medium" -> Color(0xFFC2E0FF)
                        else -> Color(0xFFE4E5EA)
                    }
                    val selectedText = if (option == "Medium") Color(0xFF004B8D) else Color.White

                    SelectableChip(
                        text = option,
                        isSelected = selectedPriority == option,
                        selectedColor = selectedBg,
                        selectedTextColor = selectedText,
                        fontFamily = fontFamily,
                        onClick = { selectedPriority = option }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. ASSIGNED BY (Spinner / Dropdown)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedAssignedBy,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand dropdown",
                            tint = Color(0xFF5E6066)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFF004B8D),
                        unfocusedTextColor = if (selectedAssignedBy == "Select Assignee") Color(
                            0xFF8E8E93
                        ) else Color(0xFF1B1C1C)
                    )
                )
                // Invisible click interceptor layer
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { expanded = true }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    assignedByOptions.drop(1).forEach { name ->
                        DropdownMenuItem(
                            text = { Text(text = name, fontFamily = fontFamily) },
                            onClick = {
                                selectedAssignedBy = name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Initial Notes & Context Multiline Box
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = {
                    Text(
                        text = "Initial Notes & Context (Starts in Development)",
                        color = Color(0xFF8E8E93),
                        fontFamily = fontFamily
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

            Spacer(modifier = Modifier.height(32.dp))

            // 6. Create Task Main Action Button
            Button(
                onClick = {
                    var selectedAssignie by mutableStateOf("")
                    if (selectedAssignedBy == "Select Assignee") selectedAssignie = "My self" else selectedAssignie = selectedAssignedBy
                    viewModel.createResponsibility(
                        taskTitle,
                        notes,
                        selectedStack,
                        selectedAssignie,
                        selectedPriority,
                    )
                    showSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004B8D)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddTask,
                        contentDescription = "Create Task Icon",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Create Task",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamily,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    selectedColor: Color = Color(0xFF0066FF),
    selectedTextColor: Color = Color.White,
    unselectedColor: Color = Color(0xFFE4E5EA),
    unselectedTextColor: Color = Color(0xFF424752),
    fontFamily: FontFamily? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) selectedColor else unselectedColor)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = fontFamily,
            color = if (isSelected) selectedTextColor else unselectedTextColor
        )
    }
}
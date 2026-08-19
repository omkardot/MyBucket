package com.omkar.mybucket.feature.responsibility.presentation.detail

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.omkar.mybucket.ui.theme.Hankengrotesk

@Composable
fun UnitTestingDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Move to Unit Testing", fontWeight = FontWeight.Bold) },
        text = { Text("is the code reviewed") },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text("Confirm", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
@Composable
fun StageConfirmationDialog(
    stageName: String,
    questionText: String,
    checkboxLabel: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Top Header Row (Title + Close Icon)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Move to $stageName",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B1C1C)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF424752)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Question Text
                Text(
                    text = questionText,
                    fontSize = 14.sp,
                    color = Color(0xFF74777F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox Box Container
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFDCE2F0),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { isChecked = !isChecked }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF005CBB),
                            uncheckedColor = Color(0xFF74777F)
                        )
                    )
                    Text(
                        text = checkboxLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B1C1C),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            fontFamily = Hankengrotesk,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF005CBB)
                        )
                    }

                    Spacer(modifier = Modifier.padding(start = 8.dp))

                    Button(
                        onClick = {
                            if (isChecked) {
                                onConfirm()
                                onDismiss()
                            }
                        },
                        enabled = isChecked,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00458F),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFE0E2EC),
                            disabledContentColor = Color(0xFF8E9099)
                        )
                    ) {
                        Text(
                            text = "Confirm",
                            fontFamily = Hankengrotesk,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SITDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("SIT Stage", fontWeight = FontWeight.Bold) },
        text = { Text("Move task to System Integration Testing?") },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text("Confirm", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun UATDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("UAT Stage", fontWeight = FontWeight.Bold) },
        text = { Text("Ensure user acceptance criteria are verified.") },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text("Confirm", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun PreprodDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pre-prod Stage", fontWeight = FontWeight.Bold) },
        text = { Text("Prepare release candidate for staging deployment?") },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text("Confirm", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun PostprodDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post-prod Stage", fontWeight = FontWeight.Bold) },
        text = { Text("Mark task as successfully deployed to production?") },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text("Confirm", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
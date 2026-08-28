package com.omkar.mybucket.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents a primary goal or task being tracked in My Bucket.
 */
@Entity(tableName = "responsibilities")
data class ResponsibilityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val project: String = "General",
    val priority: String = "Medium",
    val codestack: String = "Medium",
    val assignedBy: String = "TL",
    val currentStage: String = "Developement",
    val complexity: String = "Medium",
    val estimatedTime: String = "1 Day",
    val targetDate: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
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
    val estimatedHours: Int = 0,
    val currentStage: String = "Created",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
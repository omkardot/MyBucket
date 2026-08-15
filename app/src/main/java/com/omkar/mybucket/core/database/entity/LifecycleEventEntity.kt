package com.omkar.mybucket.core.database.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents an individual status change, progress log, or bottleneck event
 * tied to a parent Responsibility entity.
 */
@Entity(
    tableName = "lifecycle_events",
    foreignKeys = [
        ForeignKey(
            entity = ResponsibilityEntity::class,
            parentColumns = ["id"],
            childColumns = ["responsibilityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["responsibilityId"])]
)
data class LifecycleEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val responsibilityId: Long,
    val stage: String,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val timeSpentMinutes: Int = 0,
    val issuesFound: String? = null
)
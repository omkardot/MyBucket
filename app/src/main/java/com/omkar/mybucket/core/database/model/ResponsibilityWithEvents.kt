package com.omkar.mybucket.core.database.model


import androidx.room.Embedded
import androidx.room.Relation
import com.omkar.mybucket.core.database.entity.LifecycleEventEntity
import com.omkar.mybucket.core.database.entity.ResponsibilityEntity

/**
 * Combines a parent Responsibility entity with its relational list of LifecycleEvent entities.
 * Room automatically maps this relationship via the parent-child foreign key pair.
 */
data class ResponsibilityWithEvents(
    @Embedded
    val responsibility: ResponsibilityEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "responsibilityId"
    )
    val events: List<LifecycleEventEntity> = emptyList()
)
package com.omkar.mybucket.feature.responsibility.data


import com.omkar.mybucket.core.database.dao.ResponsibilityDao
import com.omkar.mybucket.core.database.entity.LifecycleEventEntity
import com.omkar.mybucket.core.database.entity.ResponsibilityEntity
import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents
import kotlinx.coroutines.flow.Flow

class ResponsibilityRepository(
    private val dao: ResponsibilityDao
) {
    val allResponsibilities: Flow<List<ResponsibilityWithEvents>> = dao.getAllResponsibilitiesWithEvents()
    val timelineEvents: Flow<List<LifecycleEventEntity>> = dao.getAllLifecycleEventsChronological()

    // 1. Alias/Method for getting responsibility with events
    fun getResponsibilityWithEvents(id: Long): Flow<ResponsibilityWithEvents?> {
        return dao.getResponsibilityById(id)
    }
    fun getAllResponsibilityWithEvents(): Flow<ResponsibilityWithEvents?> {
        return dao.getAllResponsibilityById()
    }

    // 2. Alias/Method for inserting/updating a responsibility entity
    suspend fun updateResponsibility(responsibility: ResponsibilityEntity) {
        dao.insertResponsibility(responsibility)
    }

    // 3. Method for adding a lifecycle/timeline event
    suspend fun insertTimelineEvent(event: LifecycleEventEntity) {
        dao.insertLifecycleEvent(event)
    }
    suspend fun createResponsibility(
        title: String,
        description: String,
        codeStack: String,
        assignedBy: String,
        priority: String): Long {
        val responsibility = ResponsibilityEntity(
            title = title,
            description = description,
            codestack = codeStack,
            priority = priority,
            assignedBy = assignedBy,
            currentStage = "Developement"
        )
        val resId = dao.insertResponsibility(responsibility)

        val initialEvent = LifecycleEventEntity(
            responsibilityId = resId,
            stage = "Created",
            notes = "Responsibility created."
        )
        dao.insertLifecycleEvent(initialEvent)
        return resId
    }
    // Existing helper method for logging events and updating current stage
    suspend fun addLifecycleEvent(
        responsibilityId: Long,
        stage: String,
        notes: String?,
        timeSpentMinutes: Int,
        issuesFound: String?
    ) {
        val event = LifecycleEventEntity(
            responsibilityId = responsibilityId,
            stage = stage,
            notes = notes,
            timeSpentMinutes = timeSpentMinutes,
            issuesFound = issuesFound
        )
        dao.insertLifecycleEvent(event)
        dao.updateCurrentStage(responsibilityId, stage)
    }
}




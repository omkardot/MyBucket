package com.omkar.mybucket.core.database.dao

import com.omkar.mybucket.core.database.entity.LifecycleEventEntity
import com.omkar.mybucket.core.database.entity.ResponsibilityEntity
import com.omkar.mybucket.core.database.model.ResponsibilityWithEvents


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ResponsibilityDao {

    // --- Insert Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponsibility(responsibility: ResponsibilityEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLifecycleEvent(event: LifecycleEventEntity): Long

    // --- Reactive Flow Reads ---
    @Transaction
    @Query("SELECT * FROM responsibilities ORDER BY createdAt DESC")
    fun getAllResponsibilitiesWithEvents(): Flow<List<ResponsibilityWithEvents>>

    @Transaction
    @Query("SELECT * FROM responsibilities WHERE id = :id")
    fun getResponsibilityById(id: Long): Flow<ResponsibilityWithEvents?>

    @Transaction
    @Query("SELECT * FROM responsibilities")
    fun getAllResponsibilityById(): Flow<ResponsibilityWithEvents?>

    @Query("SELECT * FROM lifecycle_events ORDER BY timestamp DESC")
    fun getAllLifecycleEventsChronological(): Flow<List<LifecycleEventEntity>>

    // --- Updates & Deletes ---
    @Query("UPDATE responsibilities SET currentStage = :newStage WHERE id = :id")
    suspend fun updateCurrentStage(id: Long, newStage: String)

    @Query("DELETE FROM responsibilities WHERE id = :id")
    suspend fun deleteResponsibility(id: Long)
}
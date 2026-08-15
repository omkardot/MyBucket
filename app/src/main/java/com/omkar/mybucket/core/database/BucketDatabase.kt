package com.omkar.mybucket.core.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.omkar.mybucket.core.database.dao.ResponsibilityDao
import com.omkar.mybucket.core.database.entity.LifecycleEventEntity
import com.omkar.mybucket.core.database.entity.ResponsibilityEntity

@Database(
    entities = [ResponsibilityEntity::class, LifecycleEventEntity::class],
    version = 2, // Incremented version from 1 to 2
    exportSchema = false
)
abstract class BucketDatabase : RoomDatabase() {

    abstract fun responsibilityDao(): ResponsibilityDao

    companion object {
        @Volatile
        private var INSTANCE: BucketDatabase? = null

        fun getDatabase(context: Context): BucketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BucketDatabase::class.java,
                    "bucket_database"
                )
                    .fallbackToDestructiveMigration() // Drops and rebuilds DB on schema change
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
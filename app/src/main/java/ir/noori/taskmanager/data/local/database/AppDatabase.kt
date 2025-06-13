package ir.noori.taskmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.noori.taskmanager.data.local.dao.TaskDao
import ir.noori.taskmanager.data.model.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
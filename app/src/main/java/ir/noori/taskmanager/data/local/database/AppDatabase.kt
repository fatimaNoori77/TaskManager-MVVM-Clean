package ir.noori.taskmanager.data.local.database;

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.noori.taskmanager.data.dto.TaskEntity
import ir.noori.taskmanager.data.local.dao.TaskDao

@Database(
    entities = [TaskEntity::class],
    version = 2,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

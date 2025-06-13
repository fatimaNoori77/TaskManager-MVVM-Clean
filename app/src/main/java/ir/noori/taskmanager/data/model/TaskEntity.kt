package ir.noori.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val isDone: Boolean = false,
    val dueDate: Long,
    val reminderTime: Long,
)

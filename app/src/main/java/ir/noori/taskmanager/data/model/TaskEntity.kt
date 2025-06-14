package ir.noori.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var description: String,
    var isDone: Boolean = false,
    var dueDate: Long= 0,
    var reminderTime: Long = 0,
)

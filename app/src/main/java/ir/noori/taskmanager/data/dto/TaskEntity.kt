package ir.noori.taskmanager.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var description: String,
    var isDone: Boolean = false,
    var dueDate: Long= 0,
    var reminderTime: Boolean = false,
)

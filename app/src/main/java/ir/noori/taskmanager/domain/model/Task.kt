package ir.noori.taskmanager.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String?,
    var dueDate: Long =0,
    var isDone: Boolean = false,
    var reminderTime: Boolean = false
)

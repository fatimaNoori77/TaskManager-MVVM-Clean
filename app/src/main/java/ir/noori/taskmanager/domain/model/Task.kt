package ir.noori.taskmanager.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val dueDate: Long,
    val isDone: Boolean = false,
    val reminderTime: Long? = null
)

package ir.noori.taskmanager.data.mapper

import ir.noori.taskmanager.data.dto.TaskDto
import ir.noori.taskmanager.data.dto.TaskEntity
import ir.noori.taskmanager.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isDone = isDone,
        dueDate = dueDate,
        reminderTime = reminderTime
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description ?: "",
        isDone = isDone,
        dueDate = dueDate,
        reminderTime = reminderTime
    )
}

fun TaskDto.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = "",
        isDone = isDone,
        dueDate = 0L,
        reminderTime = false
    )
}

fun TaskDto.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = "",
    isDone = isDone,
    dueDate = 0L,
    reminderTime = false
)

package ir.noori.taskmanager.data.mapper

import ir.noori.taskmanager.data.model.TaskDto
import ir.noori.taskmanager.data.model.TaskEntity
import ir.noori.taskmanager.domain.model.Task

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    isDone = isDone,
    dueDate = dueDate,
    reminderTime = reminderTime
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description.toString(),
    isDone = isDone,
    dueDate = dueDate,
    reminderTime = reminderTime ?: 0
)

fun TaskDto.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    isDone = isDone,
    dueDate = dueDate,
    reminderTime = reminderTime
)

package ir.noori.taskmanager.domain.usecase

import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskStatusUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        val updatedTask = task.copy(isDone = !task.isDone)
        repository.upsertTask(updatedTask)
    }
}
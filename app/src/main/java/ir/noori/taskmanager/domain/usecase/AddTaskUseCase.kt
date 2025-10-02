package ir.noori.taskmanager.domain.usecase

import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.upsertTask(task)
    }
}

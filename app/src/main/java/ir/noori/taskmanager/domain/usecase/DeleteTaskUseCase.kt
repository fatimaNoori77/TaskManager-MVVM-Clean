package ir.noori.taskmanager.domain.usecase

import ir.noori.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int) {
        repository.deleteTask(taskId)
    }
}
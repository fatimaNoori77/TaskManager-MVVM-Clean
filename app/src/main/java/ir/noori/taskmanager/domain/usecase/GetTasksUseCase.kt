package ir.noori.taskmanager.domain.usecase

import ir.noori.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke() = repository.getTasks()

    suspend fun fetchFromRemote() {
        repository.fetchRemoteTasks()
    }

}
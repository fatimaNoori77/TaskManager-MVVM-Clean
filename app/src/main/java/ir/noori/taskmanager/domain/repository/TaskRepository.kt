package ir.noori.taskmanager.domain.repository

import ir.noori.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    suspend fun fetchRemoteTasks()

    suspend fun deleteTask(id: Int)

    suspend fun upsertTask(task: Task)
}
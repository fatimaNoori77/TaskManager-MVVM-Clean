package ir.noori.taskmanager.data.repository

import ir.noori.taskmanager.data.local.dao.TaskDao
import ir.noori.taskmanager.data.mapper.toDomain
import ir.noori.taskmanager.data.mapper.toEntity
import ir.noori.taskmanager.data.remote.api.TaskApiService
import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val apiService: TaskApiService
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun fetchRemoteTasks() {
        val remoteTasks = apiService.getTasks()
        // for now, I just want to save the first 5 items
        taskDao.insertTasks(remoteTasks.take(5).map { it.toEntity() })
    }

    override suspend fun deleteTask(id: Int) {
        taskDao.deleteTaskById(id)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

}
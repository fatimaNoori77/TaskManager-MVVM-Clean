package ir.noori.taskmanager.data.remote.api

import ir.noori.taskmanager.data.model.TaskDto
import retrofit2.http.GET

interface TaskApiService {

    @GET("todos")
    suspend fun getTasks(): List<TaskDto>
}
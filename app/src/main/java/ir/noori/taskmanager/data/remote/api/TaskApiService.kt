package ir.noori.taskmanager.data.remote.api

import ir.noori.taskmanager.data.dto.TaskDto
import retrofit2.http.GET

interface TaskApiService {

    @GET("todos")
    suspend fun getTasks(): List<TaskDto>
}
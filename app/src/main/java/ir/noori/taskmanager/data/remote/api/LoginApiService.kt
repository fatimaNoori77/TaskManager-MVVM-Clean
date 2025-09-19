package ir.noori.taskmanager.data.remote.api

import ir.noori.taskmanager.domain.model.LoginRequest
import ir.noori.taskmanager.domain.model.UserSession
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {

    @POST("/auth/login")
    suspend fun login(@Body params: LoginRequest):  retrofit2.Response<UserSession>

}
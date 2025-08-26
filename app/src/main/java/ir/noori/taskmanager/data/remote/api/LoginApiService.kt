package ir.noori.taskmanager.data.remote.api

import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {

    @POST("/auth/login")
    suspend fun login(@Body loginParams: LoginInput): Response<LoginOutput>

}
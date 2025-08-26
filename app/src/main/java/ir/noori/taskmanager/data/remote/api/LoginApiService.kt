package ir.noori.taskmanager.data.remote.api

import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {

    @POST("/auth/login")
    suspend fun login(@Body loginParams: LoginInput): Flow<LoginOutput>

}
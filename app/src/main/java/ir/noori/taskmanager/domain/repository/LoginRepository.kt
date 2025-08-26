package ir.noori.taskmanager.domain.repository

import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import retrofit2.Response

interface LoginRepository {

    suspend fun doLogin(loginParams: LoginInput): Response<LoginOutput>
}
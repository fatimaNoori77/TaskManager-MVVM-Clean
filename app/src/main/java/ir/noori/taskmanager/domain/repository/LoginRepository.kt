package ir.noori.taskmanager.domain.repository

import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun doLogin(loginParams: LoginInput): Flow<LoginOutput>
}
package ir.noori.taskmanager.domain.repository

import ir.noori.taskmanager.domain.model.DomainResult
import ir.noori.taskmanager.domain.model.LoginRequest
import ir.noori.taskmanager.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(params: LoginRequest): DomainResult<UserSession>
    suspend fun saveSession(session: UserSession)
    fun observeSession(): Flow<UserSession?>
    suspend fun logout()
}
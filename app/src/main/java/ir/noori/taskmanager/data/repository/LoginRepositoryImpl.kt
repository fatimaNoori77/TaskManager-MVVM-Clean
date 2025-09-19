package ir.noori.taskmanager.data.repository

import ir.noori.taskmanager.data.remote.api.LoginApiService
import ir.noori.taskmanager.domain.model.DomainResult
import ir.noori.taskmanager.domain.model.LoginRequest
import ir.noori.taskmanager.domain.model.UserSession
import ir.noori.taskmanager.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApiService: LoginApiService
) : LoginRepository {

    override suspend fun login(
        params: LoginRequest
    ): DomainResult<UserSession> = runCatching {
        val response = loginApiService.login(params)
        if(response.isSuccessful){
            response.body() ?: error("empty response body")
        }else{
            val errorMsg = response.readErrorMessageOrDefault()
            throw IllegalArgumentException(errorMsg)
        }
    }

    override suspend fun saveSession(session: UserSession) {
        TODO("Not yet implemented")
    }

    override fun observeSession(): Flow<UserSession?> {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    private fun retrofit2.Response<*>.readErrorMessageOrDefault(): String {
        val raw = try {
            errorBody()?.string()
        } catch (_: Throwable) {
            null
        }
        if (raw.isNullOrBlank()) return "HTTP ${code()}"

        // تلاش برای parse پیام‌های JSON مانند {"message":"Invalid credentials"}
        return try {
            val obj = org.json.JSONObject(raw)
            obj.optString("message").ifBlank { raw }
        } catch (_: Throwable) {
            raw
        }
    }
}
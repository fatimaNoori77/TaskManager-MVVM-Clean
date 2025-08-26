package ir.noori.taskmanager.data.repository

import ir.noori.taskmanager.data.remote.api.LoginApiService
import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import ir.noori.taskmanager.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApiService: LoginApiService
) : LoginRepository {

    override suspend fun doLogin(loginParams: LoginInput): Flow<LoginOutput> {
        val login = loginApiService.login(loginParams)
        return login
    }


}
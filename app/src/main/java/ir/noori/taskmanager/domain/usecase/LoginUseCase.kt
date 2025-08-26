package ir.noori.taskmanager.domain.usecase

import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import ir.noori.taskmanager.domain.repository.LoginRepository
import retrofit2.Response
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(loginParams: LoginInput): Response<LoginOutput> {
        return repository.doLogin(loginParams)
    }
}

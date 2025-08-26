package ir.noori.taskmanager.domain.usecase

import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import ir.noori.taskmanager.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(loginParams: LoginInput): Flow<LoginOutput> {
        return repository.doLogin(loginParams)
    }
}

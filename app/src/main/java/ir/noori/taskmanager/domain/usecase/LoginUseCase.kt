package ir.noori.taskmanager.domain.usecase

import android.R.attr.password
import ir.noori.taskmanager.domain.model.DomainResult
import ir.noori.taskmanager.domain.model.LoginRequest
import ir.noori.taskmanager.domain.model.UserSession
import ir.noori.taskmanager.domain.repository.LoginRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
    private val validate: ValidateCredentialsUseCase
) {
    suspend operator fun invoke(params: LoginRequest): DomainResult<UserSession> {
        validate(params)?.let { return Result.failure(IllegalArgumentException(it.message)) }
        return try {
            val res = repository.login(params)
            res.onSuccess { repository.saveSession(it) }
        }catch (ce: CancellationException){
            throw ce
        }catch (t: Throwable){
            Result.failure(t)
        }
    }
}

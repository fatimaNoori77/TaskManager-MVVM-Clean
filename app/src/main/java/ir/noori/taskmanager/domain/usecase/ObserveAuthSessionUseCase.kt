package ir.noori.taskmanager.domain.usecase

import ir.noori.taskmanager.domain.model.UserSession
import ir.noori.taskmanager.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthSessionUseCase @Inject constructor(
    private val repository: LoginRepository
) {

    operator fun invoke(): Flow<UserSession?> = repository.observeSession()
}
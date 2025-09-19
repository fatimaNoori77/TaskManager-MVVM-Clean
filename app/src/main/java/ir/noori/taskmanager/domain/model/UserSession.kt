package ir.noori.taskmanager.domain.model

data class UserSession(
    val accessToken: String,
    val refreshToken: String
)

data class LoginRequest(val username: String, val password: String)

sealed interface AppError {
    data object Network: AppError
    data object Unauthorized: AppError
    data class Validation(val message: String): AppError
    data class Unknown(val message: String? =  null): AppError
}

typealias DomainResult<T> = Result<T>
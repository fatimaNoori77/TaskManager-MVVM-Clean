package ir.noori.taskmanager.domain.model

data class LoginOutput(
    val accessToken: String,
    val refreshToken: String
)

data class LoginInput(
    val username: String,
    val password: String,
    val expiresInMins: Int = 5
)
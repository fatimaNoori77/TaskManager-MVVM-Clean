package ir.noori.taskmanager.presentation.ui.splash

import ir.noori.taskmanager.domain.model.UserSession

sealed class SplashEvent {
    object NavigateToLogin : SplashEvent()
    data class NavigateToHome(val session: UserSession) : SplashEvent()
}

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data object Unauthenticated : SplashUiState()
    data class Authenticated(val session: UserSession) : SplashUiState()
}

package ir.noori.taskmanager.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.noori.taskmanager.domain.model.UserSession
import ir.noori.taskmanager.domain.usecase.ObserveAuthSessionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val observeAuthSessionUseCase: ObserveAuthSessionUseCase
): ViewModel() {

    private val _events = Channel<SplashEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            val session = observeAuthSessionUseCase().first()
            if(session == null) _events.send(SplashEvent.NavigationToLogin)
            else _events.send(SplashEvent.NavigateToHome(session))
        }
    }

 sealed class SplashEvent {
     object NavigationToLogin: SplashEvent()
     data class NavigateToHome(val session: UserSession) : SplashEvent()
 }
}
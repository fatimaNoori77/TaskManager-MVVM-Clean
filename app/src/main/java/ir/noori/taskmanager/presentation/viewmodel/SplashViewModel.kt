package ir.noori.taskmanager.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.noori.taskmanager.domain.model.UserSession
import ir.noori.taskmanager.domain.usecase.ObserveAuthSessionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashEvents {
    data object NavigateToLogin : SplashEvents()
    data object NavigateToHome : SplashEvents()
}

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data object Unauthenticated : SplashUiState()
    data class Authenticated(val session: UserSession) : SplashUiState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val observeAuthSessionUseCase: ObserveAuthSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _events = Channel<SplashEvents>(Channel.BUFFERED)
    val event : Flow<SplashEvents> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeAuthSessionUseCase()
                .distinctUntilChanged()
                .onEach { session ->
                    Log.i("TAG", "this is the session $session: ")
                }
                .onStart { _uiState.value = SplashUiState.Loading }
                .collect { session ->
                    if(session == null){
                        _uiState.value = SplashUiState.Unauthenticated
                        _events.send(SplashEvents.NavigateToLogin)
                    }else{
                        _uiState.value = SplashUiState.Authenticated(session)
                        _events.send(SplashEvents.NavigateToHome)
                    }
                }
        }
    }
}
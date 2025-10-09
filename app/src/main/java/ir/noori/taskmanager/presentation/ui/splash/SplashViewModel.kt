package ir.noori.taskmanager.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.noori.taskmanager.domain.usecase.ObserveAuthSessionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val observeAuthSessionUseCase: ObserveAuthSessionUseCase
) : ViewModel() {

    private val _events = Channel<SplashEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _uiState = MutableStateFlow<SplashUiState>(
        SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

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
                        _events.send(SplashEvent.NavigateToLogin)
                    }else{
                        _uiState.value = SplashUiState.Authenticated(session)
                        _events.send(SplashEvent.NavigateToHome(session))
                    }
                }
        }
    }

}
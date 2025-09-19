package ir.noori.taskmanager.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.noori.taskmanager.domain.model.LoginRequest
import ir.noori.taskmanager.domain.model.UserSession
import ir.noori.taskmanager.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginState>()
    val loginResponse: LiveData<LoginState> = _loginResponse

    fun login(params: LoginRequest){
        if (_loginResponse.value is LoginState.Loading) return

        viewModelScope.launch {
            _loginResponse.value = LoginState.Loading
            val response = try {
                loginUseCase(params)
            } catch (ce: CancellationException) {
                throw ce
            } catch (t: Throwable) {
                Result.failure(t)
            }
            response
                .onSuccess { session ->
                    _loginResponse.value = LoginState.Success(session)
                }
                .onFailure { exception ->
                    _loginResponse.value = LoginState.Error(exception.message ?: "خطای نامشخص")
                }
        }
    }
}

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val userSession: UserSession) : LoginState()
    data class Error(val message: String) : LoginState()
}
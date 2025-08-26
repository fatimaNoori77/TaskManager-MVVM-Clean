package ir.noori.taskmanager.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.noori.taskmanager.domain.model.LoginInput
import ir.noori.taskmanager.domain.model.LoginOutput
import ir.noori.taskmanager.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginState>()
    val loginResponse: LiveData<LoginState> = _loginResponse

    fun login(loginParams: LoginInput){
        viewModelScope.launch {
            _loginResponse.value = LoginState.Loading
            val response = loginUseCase.invoke(loginParams)
            try {
                _loginResponse.value = LoginState.Success(response.first())
            }catch (e: Exception){
                _loginResponse.value = LoginState.Error(e.message.toString())
            }
        }
    }
}

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val loginOutput: LoginOutput) : LoginState()
    data class Error(val message: String) : LoginState()
}
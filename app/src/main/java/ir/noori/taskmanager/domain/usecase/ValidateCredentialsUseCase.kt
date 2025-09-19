package ir.noori.taskmanager.domain.usecase

import android.R.attr.password
import ir.noori.taskmanager.domain.model.AppError
import ir.noori.taskmanager.domain.model.LoginRequest
import javax.inject.Inject

class ValidateCredentialsUseCase @Inject constructor(){
    operator fun invoke(params: LoginRequest): AppError.Validation? {
        if(params.username.isBlank()) return AppError.Validation("fill the username.")
        if(params.password.length < 6) return AppError.Validation("password must be at least 6 characters.")
        return null
    }

}
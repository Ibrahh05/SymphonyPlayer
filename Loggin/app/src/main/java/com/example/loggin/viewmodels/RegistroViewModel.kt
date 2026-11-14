package com.example.loggin.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loggin.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<RegistroUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUsernameChanged(username: String) {
        _uiState.update { state ->
            val error = if (username.isEmpty()) null else if (username.length < 3) R.string.short_pass_err else null
            state.copy(
                username = username,
                usernameError = error,
                isRegistroEnabled = isFormValid(username, state.email, state.password, state.confirmPassword, error, state.emailError, state.passwordError, state.confirmPasswordError)
            )
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { state ->
            val error = if (email.isEmpty()) null else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) R.string.invalid_email_err else null
            state.copy(
                email = email,
                emailError = error,
                isRegistroEnabled = isFormValid(state.username, email, state.password, state.confirmPassword, state.usernameError, error, state.passwordError, state.confirmPasswordError)
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { state ->
            val error = if (password.isEmpty()) null else if (password.length < 6) R.string.short_pass_err else null
            val confirmError = if (state.confirmPassword.isNotEmpty() && state.confirmPassword != password) R.string.error_passwords_no_coinciden else null
            state.copy(
                password = password,
                passwordError = error,
                confirmPasswordError = confirmError,
                isRegistroEnabled = isFormValid(state.username, state.email, password, state.confirmPassword, state.usernameError, state.emailError, error, confirmError)
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { state ->
            val error = if (confirmPassword.isEmpty()) null else if (confirmPassword != state.password) R.string.error_passwords_no_coinciden else null
            state.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = error,
                isRegistroEnabled = isFormValid(state.username, state.email, state.password, confirmPassword, state.usernameError, state.emailError, state.passwordError, error)
            )
        }
    }

    fun onRegistroClicked() {
        val state = _uiState.value
        if (!state.isRegistroEnabled || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }

            if (state.username.isNotBlank() && state.email.isNotBlank() && state.password.isNotBlank() && state.password == state.confirmPassword) {
                _uiEvent.send(RegistroUiEvent.NavigateBackToLogin)
            } else {
                _uiEvent.send(RegistroUiEvent.ShowError(R.string.invalid_credentials_err))
            }
        }
    }

    private fun isFormValid(
        username: String, email: String, pass: String, confirm: String,
        uErr: Int?, eErr: Int?, pErr: Int?, cErr: Int?
    ): Boolean {
        return username.isNotBlank() && email.isNotBlank() && pass.isNotBlank() && confirm.isNotBlank() &&
                uErr == null && eErr == null && pErr == null && cErr == null && pass == confirm
    }
}
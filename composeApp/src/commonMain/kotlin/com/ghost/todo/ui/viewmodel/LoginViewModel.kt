package com.ghost.todo.ui.viewmodel

// LoginViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.todo.data.service.AuthService
import com.ghost.todo.ui.contract.LoginEffect
import com.ghost.todo.ui.contract.LoginEvent
import com.ghost.todo.ui.contract.LoginState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService // Inject the service we made earlier
) : ViewModel() {

    // --- State ---
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // --- Side Effects ---
    // Channels are perfect for one-off effects because they drop the event once consumed
    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    // --- Intent Handler ---
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(emailInput = event.email, emailError = null) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(passwordInput = event.password, passwordError = null) }
            }
            LoginEvent.ClearErrors -> {
                _state.update { it.copy(emailError = null, passwordError = null) }
            }
            LoginEvent.SubmitEmailLogin -> validateAndLogin()
            is LoginEvent.SubmitGoogleLogin -> handleGoogleLogin(event.idToken)
        }
    }

    // --- Core Logic ---
    private fun validateAndLogin() {
        val currentState = _state.value

        // 1. Validation
        var hasError = false
        if (currentState.emailInput.isBlank() || !currentState.emailInput.contains("@")) {
            _state.update { it.copy(emailError = "Please enter a valid Ethereal identifier (Email).") }
            hasError = true
        }
        if (currentState.passwordInput.length < 6) {
            _state.update { it.copy(passwordError = "Passcode must be at least 6 characters.") }
            hasError = true
        }

        if (hasError) {
            sendEffect(LoginEffect.ShowWarning("Check your inputs and try again."))
            return
        }

        // 2. Execution
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authService.signInWithEmail(currentState.emailInput, currentState.passwordInput)

            result.onSuccess {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.NavigateToDashboard)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError(error.message ?: "Authentication failed. The void rejects your credentials."))
            }
        }
    }

    private fun handleGoogleLogin(idToken: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authService.signInWithGoogle(idToken)

            result.onSuccess {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.NavigateToDashboard)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("Google Sync failed: ${error.message}"))
            }
        }
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
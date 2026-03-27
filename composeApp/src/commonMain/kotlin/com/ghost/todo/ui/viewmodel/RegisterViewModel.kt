package com.ghost.todo.ui.viewmodel

// RegisterViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.todo.data.service.AuthService
import com.ghost.todo.ui.contract.RegisterEffect
import com.ghost.todo.ui.contract.RegisterEvent
import com.ghost.todo.ui.contract.RegisterState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effect = Channel<RegisterEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameChanged -> _state.update { it.copy(usernameInput = event.username, usernameError = null) }
            is RegisterEvent.EmailChanged -> _state.update { it.copy(emailInput = event.email, emailError = null) }
            is RegisterEvent.PasswordChanged -> _state.update { it.copy(passwordInput = event.password, passwordError = null) }
            is RegisterEvent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPasswordInput = event.password, confirmPasswordError = null) }
            RegisterEvent.ClearErrors -> clearAllErrors()
            RegisterEvent.SubmitRegistration -> validateAndRegister()
            is RegisterEvent.SubmitGoogleRegistration -> handleGoogleRegistration(event.idToken)
            RegisterEvent.NavigateToLoginClicked -> sendEffect(RegisterEffect.NavigateBackToLogin)
        }
    }

    private fun validateAndRegister() {
        val currentState = _state.value
        var hasError = false

        // 1. Validation Checks
        if (currentState.usernameInput.isBlank() || currentState.usernameInput.length < 3) {
            _state.update { it.copy(usernameError = "Alias must be at least 3 characters.") }
            hasError = true
        }

        if (currentState.emailInput.isBlank() || !currentState.emailInput.contains("@")) {
            _state.update { it.copy(emailError = "Invalid Ethereal identifier (Email).") }
            hasError = true
        }

        if (currentState.passwordInput.length < 6) {
            _state.update { it.copy(passwordError = "Passcode requires at least 6 characters for security.") }
            hasError = true
        }

        if (currentState.passwordInput != currentState.confirmPasswordInput) {
            _state.update { it.copy(confirmPasswordError = "Passcodes do not align. Try again.") }
            hasError = true
        }

        if (hasError) {
            sendEffect(RegisterEffect.ShowWarning("Please resolve the highlighted fields."))
            return
        }

        // 2. Execution
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authService.signUpWithEmail(currentState.emailInput, currentState.passwordInput)

            result.onSuccess { user ->
                // Note: You might want to save the username to Firestore here later
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.NavigateToDashboard)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError(error.message ?: "Initialization failed."))
            }
        }
    }

    private fun handleGoogleRegistration(idToken: String) {
        // In Firebase, Google Sign-In and Sign-Up use the same credential method
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authService.signInWithGoogle(idToken)

            result.onSuccess {
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.NavigateToDashboard)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError("Google Sync failed: ${error.message}"))
            }
        }
    }

    private fun clearAllErrors() {
        _state.update {
            it.copy(
                usernameError = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null
            )
        }
    }

    private fun sendEffect(effect: RegisterEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
package com.ghost.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.todo.data.model.enum.AuthProvider
import com.ghost.todo.data.service.AuthService
import com.ghost.todo.ui.contract.RegisterEffect
import com.ghost.todo.ui.contract.RegisterEvent
import com.ghost.todo.ui.contract.RegisterState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effect = Channel<RegisterEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            // Form input events
            is RegisterEvent.UsernameChanged -> _state.update {
                it.copy(usernameInput = event.username, usernameError = null)
            }

            is RegisterEvent.EmailChanged -> _state.update {
                it.copy(emailInput = event.email, emailError = null)
            }

            is RegisterEvent.PasswordChanged -> _state.update {
                it.copy(passwordInput = event.password, passwordError = null)
            }

            is RegisterEvent.ConfirmPasswordChanged -> _state.update {
                it.copy(confirmPasswordInput = event.password, confirmPasswordError = null)
            }

            RegisterEvent.ClearErrors -> clearAllErrors()

            // Registration submission events
            RegisterEvent.SubmitRegistration -> validateAndRegister()
            is RegisterEvent.SubmitGoogleRegistration -> handleGoogleRegistration(event.idToken)
            RegisterEvent.SubmitAppleRegistration -> handleAppleRegistration()
            RegisterEvent.SubmitFacebookRegistration -> handleFacebookRegistration()
            RegisterEvent.SubmitTwitterRegistration -> handleTwitterRegistration()
            RegisterEvent.SubmitGitHubRegistration -> handleGitHubRegistration()

            // Navigation events
            RegisterEvent.NavigateToLoginClicked -> sendEffect(RegisterEffect.NavigateBackToLogin)
        }
    }

    // --- Email/Password Registration ---
    private fun validateAndRegister() {
        val currentState = _state.value
        var hasError = false

        // Validation Checks
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

        // Execute registration
        _state.update { it.copy(isLoading = true, selectedProvider = AuthProvider.EMAIL) }
        viewModelScope.launch {
            val result = authService.signUpWithEmail(
                email = currentState.emailInput,
                password = currentState.passwordInput,
                username = currentState.usernameInput,
                displayName = currentState.usernameInput
            )

            result.onSuccess { userProfile ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.NavigateToDashboard)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError(error.message ?: "Registration failed."))
            }
        }
    }

    // --- Social Registration Handlers ---
    private fun handleGoogleRegistration(idToken: String) {
        _state.update { it.copy(isLoading = true, selectedProvider = AuthProvider.GOOGLE) }
        viewModelScope.launch {
            val result = authService.signInWithGoogle(idToken)

            result.onSuccess { userProfile ->
                _state.update { it.copy(isLoading = false) }

                // Check if this is a new Google user who needs to set a username
                // If username was auto-generated (starts with "user_" or contains random numbers)
                if (userProfile.username.startsWith("user_") ||
                    userProfile.username.matches(Regex(".*\\d.*"))
                ) {
                    // Navigate to a "Set Username" screen
                    sendEffect(RegisterEffect.NavigateToSetUsername)
                } else {
                    sendEffect(RegisterEffect.NavigateToDashboard)
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError("Google Sign-Up failed: ${error.message}"))
            }
        }
    }

    private fun handleAppleRegistration() {
        _state.update { it.copy(isLoading = true, selectedProvider = AuthProvider.APPLE) }
        viewModelScope.launch {
            try {
                // You'll need to implement Apple Sign-In in your AuthService
                // For now, show coming soon message
                sendEffect(RegisterEffect.ShowWarning("Apple Sign-Up coming soon!"))
                _state.update { it.copy(isLoading = false) }

                // When implemented:
                // val result = authService.signInWithApple()
                // result.onSuccess { userProfile ->
                //     _state.update { it.copy(isLoading = false) }
                //     // Check if username needs to be set
                //     if (userProfile.username.isBlank()) {
                //         sendEffect(RegisterEffect.NavigateToSetUsername)
                //     } else {
                //         sendEffect(RegisterEffect.NavigateToDashboard)
                //     }
                // }.onFailure { error ->
                //     _state.update { it.copy(isLoading = false) }
                //     sendEffect(RegisterEffect.ShowError("Apple Sign-Up failed: ${error.message}"))
                // }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError("Apple Sign-Up failed: ${e.message}"))
            }
        }
    }

    private fun handleFacebookRegistration() {
        _state.update { it.copy(isLoading = true, selectedProvider = AuthProvider.FACEBOOK) }
        viewModelScope.launch {
            try {
                sendEffect(RegisterEffect.ShowWarning("Facebook Sign-Up coming soon!"))
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError("Facebook Sign-Up failed: ${e.message}"))
            }
        }
    }

    private fun handleTwitterRegistration() {
        _state.update { it.copy(isLoading = true, selectedProvider = AuthProvider.TWITTER) }
        viewModelScope.launch {
            try {
                sendEffect(RegisterEffect.ShowWarning("Twitter/X Sign-Up coming soon!"))
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError("Twitter/X Sign-Up failed: ${e.message}"))
            }
        }
    }

    private fun handleGitHubRegistration() {
        _state.update { it.copy(isLoading = true, selectedProvider = AuthProvider.GITHUB) }
        viewModelScope.launch {
            try {
                sendEffect(RegisterEffect.ShowWarning("GitHub Sign-Up coming soon!"))
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(RegisterEffect.ShowError("GitHub Sign-Up failed: ${e.message}"))
            }
        }
    }

    // --- Helper Functions ---
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
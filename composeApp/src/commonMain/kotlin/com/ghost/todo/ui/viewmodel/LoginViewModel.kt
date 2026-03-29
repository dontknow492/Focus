package com.ghost.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.todo.data.service.AuthService
import com.ghost.todo.ui.contract.LoginEffect
import com.ghost.todo.ui.contract.LoginEvent
import com.ghost.todo.ui.contract.LoginState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            // Email/Password events
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

            // Social login events
            is LoginEvent.SubmitGoogleLogin -> handleGoogleLogin(event.idToken)
            LoginEvent.SubmitAppleLogin -> handleAppleLogin()
            LoginEvent.SubmitFacebookLogin -> handleFacebookLogin()
            LoginEvent.SubmitTwitterLogin -> handleTwitterLogin()
            LoginEvent.SubmitGitHubLogin -> handleGitHubLogin()

            // Phone login events (if you add them)
            is LoginEvent.PhoneNumberChanged -> handlePhoneNumberChanged(event.phoneNumber)
            is LoginEvent.VerificationCodeChanged -> handleVerificationCodeChanged(event.code)
            LoginEvent.SubmitPhoneLogin -> handlePhoneLogin()
            LoginEvent.RequestVerificationCode -> requestVerificationCode()
        }
    }

    // --- Email/Password Logic ---
    private fun validateAndLogin() {
        val currentState = _state.value

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

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authService.signInWithEmail(
                currentState.emailInput,
                currentState.passwordInput
            )

            result.onSuccess { userProfile ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.NavigateToDashboard)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(
                    LoginEffect.ShowError(
                        error.message ?: "Authentication failed. The void rejects your credentials."
                    )
                )
            }
        }
    }

    // --- Social Login Handlers ---
    private fun handleGoogleLogin(idToken: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authService.signInWithGoogle(idToken)

            result.onSuccess { userProfile ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.NavigateToDashboard)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("Google Sign-In failed: ${error.message}"))
            }
        }
    }

    private fun handleAppleLogin() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // You'll need to implement Apple Sign-In in your AuthService
                // For now, show that it's not implemented
                sendEffect(LoginEffect.ShowWarning("Apple Sign-In coming soon!"))
                _state.update { it.copy(isLoading = false) }

                // When implemented:
                // val result = authService.signInWithApple()
                // result.onSuccess { userProfile ->
                //     _state.update { it.copy(isLoading = false) }
                //     sendEffect(LoginEffect.NavigateToDashboard)
                // }.onFailure { error ->
                //     _state.update { it.copy(isLoading = false) }
                //     sendEffect(LoginEffect.ShowError("Apple Sign-In failed: ${error.message}"))
                // }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("Apple Sign-In failed: ${e.message}"))
            }
        }
    }

    private fun handleFacebookLogin() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // You'll need to implement Facebook Sign-In in your AuthService
                sendEffect(LoginEffect.ShowWarning("Facebook Sign-In coming soon!"))
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("Facebook Sign-In failed: ${e.message}"))
            }
        }
    }

    private fun handleTwitterLogin() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                sendEffect(LoginEffect.ShowWarning("Twitter/X Sign-In coming soon!"))
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("Twitter/X Sign-In failed: ${e.message}"))
            }
        }
    }

    private fun handleGitHubLogin() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                sendEffect(LoginEffect.ShowWarning("GitHub Sign-In coming soon!"))
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("GitHub Sign-In failed: ${e.message}"))
            }
        }
    }

    // --- Phone Login Logic (Optional) ---
    private var phoneNumber: String = ""
    private var verificationCode: String = ""

    private fun handlePhoneNumberChanged(phone: String) {
        phoneNumber = phone
    }

    private fun handleVerificationCodeChanged(code: String) {
        verificationCode = code
    }

    private fun requestVerificationCode() {
        if (phoneNumber.isBlank()) {
            sendEffect(LoginEffect.ShowWarning("Please enter your phone number"))
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // You'll need to implement phone auth in AuthService
                // authService.sendVerificationCode(phoneNumber)
                sendEffect(LoginEffect.VerificationCodeSent(phoneNumber))
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.NavigateToVerification)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("Failed to send code: ${e.message}"))
            }
        }
    }

    private fun handlePhoneLogin() {
        if (verificationCode.isBlank()) {
            sendEffect(LoginEffect.ShowWarning("Please enter the verification code"))
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // You'll need to implement phone sign-in in AuthService
                // val result = authService.signInWithPhoneNumber(verificationCode)
                // result.onSuccess { userProfile ->
                //     _state.update { it.copy(isLoading = false) }
                //     sendEffect(LoginEffect.NavigateToDashboard)
                // }
                sendEffect(LoginEffect.ShowWarning("Phone sign-in coming soon!"))
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                sendEffect(LoginEffect.ShowError("Phone sign-in failed: ${e.message}"))
            }
        }
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
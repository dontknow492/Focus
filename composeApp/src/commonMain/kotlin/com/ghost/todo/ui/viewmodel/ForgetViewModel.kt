package com.ghost.todo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghost.todo.data.service.AuthService
import com.ghost.todo.ui.contract.ForgotPasswordEffect
import com.ghost.todo.ui.contract.ForgotPasswordEvent
import com.ghost.todo.ui.contract.ForgotPasswordState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ForgotPasswordViewModel.kt
class ForgotPasswordViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()

    private val _effect = Channel<ForgotPasswordEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                _state.update {
                    it.copy(emailInput = event.email, emailError = null)
                }
            }

            ForgotPasswordEvent.SubmitResetRequest -> sendResetEmail()
            ForgotPasswordEvent.ClearErrors -> clearErrors()
            ForgotPasswordEvent.NavigateBackToLogin -> {
                sendEffect(ForgotPasswordEffect.NavigateBackToLogin)
            }
        }
    }

    private fun sendResetEmail() {
        val email = _state.value.emailInput

        // Validate email
        if (email.isBlank() || !email.contains("@")) {
            _state.update {
                it.copy(emailError = "Please enter a valid email address.")
            }
            sendEffect(ForgotPasswordEffect.ShowError("Invalid email address"))
            return
        }

        // Send reset email
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authService.sendPasswordResetEmail(email)

            result.onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isEmailSent = true
                    )
                }
                sendEffect(
                    ForgotPasswordEffect.ShowSuccess(
                        "Reset link sent to $email. Please check your inbox."
                    )
                )
                // Optional: Auto-navigate back after delay
                delay(2000)
                sendEffect(ForgotPasswordEffect.NavigateBackToLogin)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                val errorMessage = when {
                    error.message?.contains("user-not-found") == true ->
                        "No account found with this email address."

                    error.message?.contains("too-many-requests") == true ->
                        "Too many attempts. Please try again later."

                    else -> error.message ?: "Failed to send reset email."
                }
                sendEffect(ForgotPasswordEffect.ShowError(errorMessage))
            }
        }
    }

    private fun clearErrors() {
        _state.update { it.copy(emailError = null) }
    }

    private fun sendEffect(effect: ForgotPasswordEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
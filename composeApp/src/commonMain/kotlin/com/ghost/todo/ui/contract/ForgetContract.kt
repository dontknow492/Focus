package com.ghost.todo.ui.contract

// ForgotPasswordContract.kt
data class ForgotPasswordState(
    val emailInput: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val isEmailSent: Boolean = false  // Track if email was sent
)

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val email: String) : ForgotPasswordEvent
    data object SubmitResetRequest : ForgotPasswordEvent
    data object ClearErrors : ForgotPasswordEvent
    data object NavigateBackToLogin : ForgotPasswordEvent
}

sealed interface ForgotPasswordEffect {
    data object NavigateBackToLogin : ForgotPasswordEffect
    data class ShowError(val message: String) : ForgotPasswordEffect
    data class ShowSuccess(val message: String) : ForgotPasswordEffect  // "Check your email"
}
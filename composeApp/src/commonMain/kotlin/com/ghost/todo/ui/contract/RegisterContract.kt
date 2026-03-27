package com.ghost.todo.ui.contract

// RegisterContract.kt
// 1. STATE
data class RegisterState(
    val usernameInput: String = "",
    val emailInput: String = "",
    val passwordInput: String = "",
    val confirmPasswordInput: String = "",
    val isLoading: Boolean = false,

    // Field-specific errors
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

// 2. INTENT (EVENT)
sealed interface RegisterEvent {
    data class UsernameChanged(val username: String) : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class ConfirmPasswordChanged(val password: String) : RegisterEvent

    data object SubmitRegistration : RegisterEvent
    data class SubmitGoogleRegistration(val idToken: String) : RegisterEvent
    data object ClearErrors : RegisterEvent
    data object NavigateToLoginClicked : RegisterEvent
}

// 3. SIDE EFFECT
sealed interface RegisterEffect {
    data object NavigateToDashboard : RegisterEffect
    data object NavigateBackToLogin : RegisterEffect
    data class ShowError(val message: String) : RegisterEffect
    data class ShowWarning(val message: String) : RegisterEffect
}
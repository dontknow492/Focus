package com.ghost.todo.ui.contract


import dev.gitlive.firebase.auth.FirebaseUser

// 1. STATE: The immutable data the UI will render
data class LoginState(
    val emailInput: String = "",
    val passwordInput: String = "",
    val isLoading: Boolean = false,
    // Professional grade: Field-specific errors rather than just a generic message
    val emailError: String? = null,
    val passwordError: String? = null
)

// 2. INTENT (EVENT): Actions coming FROM the UI
sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent

    data object SubmitEmailLogin : LoginEvent
    data class SubmitGoogleLogin(val idToken: String) : LoginEvent
    data object ClearErrors : LoginEvent
}

// 3. SIDE EFFECT: One-off UI actions (Navigation, Toasts, Snackbars)
sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
    data class ShowError(val message: String) : LoginEffect
    data class ShowWarning(val message: String) : LoginEffect
}
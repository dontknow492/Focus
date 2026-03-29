package com.ghost.todo.ui.contract

import com.ghost.todo.data.model.enum.AuthProvider

// 1. STATE: The immutable data the UI will render
data class LoginState(
    val emailInput: String = "",
    val passwordInput: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val selectedProvider: AuthProvider? = null  // Track which provider is being used
)

// 2. INTENT (EVENT): Actions coming FROM the UI
sealed interface LoginEvent {
    // Email/Password events
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object SubmitEmailLogin : LoginEvent
    data object ClearErrors : LoginEvent

    // Social login events
    data class SubmitGoogleLogin(val idToken: String) : LoginEvent
    data object SubmitAppleLogin : LoginEvent  // Apple Sign-In
    data object SubmitFacebookLogin : LoginEvent  // Facebook Sign-In
    data object SubmitTwitterLogin : LoginEvent  // Twitter/X Sign-In
    data object SubmitGitHubLogin : LoginEvent  // GitHub Sign-In

    // Optional: For "Login with Phone"
    data class PhoneNumberChanged(val phoneNumber: String) : LoginEvent
    data class VerificationCodeChanged(val code: String) : LoginEvent
    data object SubmitPhoneLogin : LoginEvent
    data object RequestVerificationCode : LoginEvent
}

// 3. SIDE EFFECT: One-off UI actions
sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
    data class ShowError(val message: String) : LoginEffect
    data class ShowWarning(val message: String) : LoginEffect

    // Optional: For phone verification
    data object NavigateToVerification : LoginEffect
    data class VerificationCodeSent(val phoneNumber: String) : LoginEffect
}
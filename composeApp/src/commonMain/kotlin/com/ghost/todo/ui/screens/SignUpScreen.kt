package com.ghost.todo.ui.screens


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ghost.todo.resources.Res
import com.ghost.todo.resources.graphic_eq
import com.ghost.todo.ui.components.AdvancedGlassmorphismCard
import com.ghost.todo.ui.components.CyberTextField
import com.ghost.todo.ui.components.GradientText
import com.ghost.todo.ui.components.PrimaryGlowButton
import com.ghost.todo.ui.components.SocialAuthButton
import com.ghost.todo.ui.contract.RegisterEvent
import com.ghost.todo.ui.contract.RegisterState
import org.jetbrains.compose.resources.painterResource

// ============================================================================
// 1. STRING RESOURCES
// ============================================================================
object RegisterStrings {
    val appName = "Ether Focus"
    val heroTitleLine1 = "Reclaim your"
    val heroTitleLine2 = "cognitive flow."
    val heroSubtitle =
        "Enter the digital sanctuary designed for deep work. Where every pixel is tuned to your focus, and every interaction serves your intent."
    val badgeText = "AESTHETIC PRECISION"

    val formTitle = "Create Account"
    val formSubtitle = "Join the ecosystem of focused minds."

    val nameLabel = "Full Name"
    val namePlaceholder = "Enter your alias"
    val emailLabel = "Email Address"
    val emailPlaceholder = "name@focus.com"
    val passwordLabel = "Security Key"
    val passwordPlaceholder = "••••••••"
    val confirmPasswordLabel = "Confirm Key"

    val registerButton = "Begin Your Journey"
    val alreadyHaveAccount = "Already have a sanctuary? "
    val loginText = "Login"
    val socialSync = "Social Sync"
    val google = "Google"
    val apple = "Apple"
}

// ============================================================================
// 2. MAIN RESPONSIVE SCREEN
// ============================================================================
@Composable
fun RegisterScreen(
    state: RegisterState, // From the RegisterViewModel
    onEvent: (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isDesktop = maxWidth > 800.dp


        if (isDesktop) {
            DesktopRegisterLayout(state, onEvent, onNavigateToLogin)
        } else {
            MobileRegisterLayout(state, onEvent, onNavigateToLogin)
        }
    }
}

// ============================================================================
// 3. DESKTOP LAYOUT (Split Screen)
// ============================================================================
@Composable
private fun DesktopRegisterLayout(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        // Left Side: Hero / Atmospheric Branding
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.graphic_eq),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

            // Simulated Atmospheric Gradient Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            // Content Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(80.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Hero Text
                Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                    Text(
                        text = RegisterStrings.heroTitleLine1,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    GradientText(
                        text = RegisterStrings.appName,
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        ),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic
                    )


                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = RegisterStrings.heroSubtitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Light,
                        lineHeight = MaterialTheme.typography.titleLarge.lineHeight * 1.2f
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // "Aesthetic Precision" Badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome, // Substitute for auto_awesome
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = RegisterStrings.badgeText,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing * 1.5f
                        )
                    }
                }
            }
        }

        // Right Side: Registration Form
        Box(
            modifier = Modifier
                .weight(0.85f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            RegisterForm(
                state = state,
                onEvent = onEvent,
                onNavigateToLogin = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(vertical = 64.dp)
            )
        }
    }
}

// ============================================================================
// 4. MOBILE LAYOUT (Single Column)
// ============================================================================
@Composable
private fun MobileRegisterLayout(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Decorative Blob
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(320.dp)
                .blur(120.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    CircleShape
                )
        )

        // 🔥 Center container
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(min = 280.dp, max = 500.dp) // ✅ FIXED WIDTH
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState()), // ✅ prevents overflow
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Form
                RegisterForm(
                    state = state,
                    onEvent = onEvent,
                    onNavigateToLogin = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ============================================================================
// 5. REUSABLE FORM COMPONENT
// ============================================================================
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RegisterForm(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Local state for password visibility toggle
    var passwordVisible by remember { mutableStateOf(false) }

    AdvancedGlassmorphismCard(
        modifier = modifier,
        borderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        gradientColors = listOf(
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),

        ),
        blurRadius = 16.dp,
        shadowElevation = 16.dp,
        innerPadding = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 42.dp,
                vertical = 42.dp
            )
        ) {
            // Headers
            Text(
                text = RegisterStrings.formTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = RegisterStrings.formSubtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(40.dp))

            // Input Fields (Reusing CyberTextField from LoginScreen package)
            CyberTextField(
                value = state.usernameInput,
                onValueChange = { onEvent(RegisterEvent.UsernameChanged(it)) },
                label = RegisterStrings.nameLabel,
                placeholder = RegisterStrings.namePlaceholder,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (state.usernameError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = state.usernameError != null,
                errorMessage = state.usernameError
            )

            Spacer(modifier = Modifier.height(20.dp))

            CyberTextField(
                value = state.emailInput,
                onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                label = RegisterStrings.emailLabel,
                placeholder = RegisterStrings.emailPlaceholder,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = if (state.emailError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = state.emailError != null,
                errorMessage = state.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CyberTextField(
                value = state.passwordInput,
                onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                label = RegisterStrings.passwordLabel,
                placeholder = RegisterStrings.passwordPlaceholder,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (state.passwordError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = state.passwordError != null,
                errorMessage = state.passwordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingContent = {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                }
            )

            // Added Confirm Password to match the RegisterViewModel logic we built earlier
            Spacer(modifier = Modifier.height(20.dp))

            CyberTextField(
                value = state.confirmPasswordInput,
                onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                label = RegisterStrings.confirmPasswordLabel,
                placeholder = RegisterStrings.passwordPlaceholder,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (state.confirmPasswordError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = state.confirmPasswordError != null,
                errorMessage = state.confirmPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Main Action (Reusing PrimaryGlowButton)
            PrimaryGlowButton(
                text = RegisterStrings.registerButton,
                isLoading = state.isLoading,
                onClick = { onEvent(RegisterEvent.SubmitRegistration) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Footer & Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = RegisterStrings.alreadyHaveAccount,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = RegisterStrings.loginText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Alternative Access Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
                Text(
                    text = RegisterStrings.socialSync.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing * 1.5f
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Replaced abstract HTML buttons with the requested Google & Apple buttons (Reusing SocialAuthButton)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialAuthButton(
                    text = RegisterStrings.google,
                    onClick = { /* Trigger Google Auth Effect */ },
                    modifier = Modifier.weight(1f)
                )
                SocialAuthButton(
                    text = RegisterStrings.apple,
                    onClick = { /* Trigger Apple Auth Effect */ },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }


}

// ============================================================================
// STUBS FOR VIEWMODEL STATE (To make code compile independently)
// ============================================================================

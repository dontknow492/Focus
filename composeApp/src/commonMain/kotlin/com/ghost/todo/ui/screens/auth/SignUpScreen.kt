package com.ghost.todo.ui.screens.auth


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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ghost.todo.resources.*
import com.ghost.todo.ui.components.*
import com.ghost.todo.ui.contract.RegisterEvent
import com.ghost.todo.ui.contract.RegisterState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// ============================================================================
// 2. MAIN RESPONSIVE SCREEN
// ============================================================================
@Composable
fun RegisterScreen(
    state: RegisterState, // From the RegisterViewModel
    onEvent: (RegisterEvent) -> Unit, onNavigateToLogin: () -> Unit
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
    state: RegisterState, onEvent: (RegisterEvent) -> Unit, onNavigateToLogin: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme



    Row(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {

        // 🔥 LEFT SIDE (FIXED)
        Box(
            modifier = Modifier.weight(1.2f).fillMaxSize().drawWithContent {
                drawContent()

                // Fade out toward right side
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            colorScheme.surface
                        ),
                        startX = size.width * 0.6f, // where fade starts
                        endX = size.width
                    )
                )
            }
        )
        {
            // 🌄 Background Image
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.graphic_eq),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
            )


            // ✨ Content
            Column(
                modifier = Modifier.fillMaxSize().padding(80.dp), verticalArrangement = Arrangement.Center
            ) {

                Column(modifier = Modifier.fillMaxWidth(0.9f)) {

                    Text(
                        text = stringResource(Res.string.heroTitleLine1),
                        style = MaterialTheme.typography.displayLarge,
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold
                    )

                    GradientText(
                        text = stringResource(Res.string.appName),
                        colors = listOf(
                            colorScheme.primary, colorScheme.secondary
                        ),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(Res.string.heroSubtitle),
                        style = MaterialTheme.typography.titleLarge,
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Light,
                        lineHeight = MaterialTheme.typography.titleLarge.lineHeight * 1.2f
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stringResource(Res.string.badgeText),
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing * 1.5f
                        )
                    }
                }
            }
        }

        // 👉 RIGHT SIDE (FORM)
        Box(
            modifier = Modifier.weight(0.85f).fillMaxSize()
//                .background(colorScheme.surface)
                .verticalScroll(rememberScrollState()), contentAlignment = Alignment.Center
        ) {
            // Decorative Blob
            Box(
                modifier = Modifier.align(Alignment.TopEnd).size(320.dp)
                    .blur(120.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded).background(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape
                    )
            )
            // ✅ FIX 3: Add subtle divider glow
            Box(
                modifier = Modifier.fillMaxHeight().width(1.dp).align(Alignment.CenterStart).background(
                    colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            )

            RegisterForm(
                state = state,
                onEvent = onEvent,
                onNavigateToLogin = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(0.7f).padding(vertical = 64.dp)
            )
        }
    }
}

// ============================================================================
// 4. MOBILE LAYOUT (Single Column)
// ============================================================================
@Composable
private fun MobileRegisterLayout(
    state: RegisterState, onEvent: (RegisterEvent) -> Unit, onNavigateToLogin: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        // Decorative Blob
        Box(
            modifier = Modifier.align(Alignment.TopEnd).size(320.dp)
                .blur(120.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded).background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape
                )
        )

        // 🔥 Center container
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.widthIn(min = 280.dp, max = 500.dp) // ✅ FIXED WIDTH
                    .fillMaxWidth().padding(horizontal = 12.dp, vertical = 24.dp)
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
    state: RegisterState, onEvent: (RegisterEvent) -> Unit, onNavigateToLogin: () -> Unit, modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    val colorScheme = MaterialTheme.colorScheme

    AdvancedGlassmorphismCard(
        modifier = modifier,

        // ✅ FIXED: proper surface-based glass
        backgroundColor = if (isDark) colorScheme.surface.copy(alpha = 0.35f)
        else colorScheme.surface.copy(alpha = 0.85f),

        borderColor = colorScheme.outlineVariant.copy(
            alpha = if (isDark) 0.3f else 0.5f
        ),

        innerGlowColor = if (isDark) Color.White.copy(alpha = 0.08f)
        else Color.White.copy(alpha = 0.3f),

        shadowElevation = if (isDark) 12.dp else 6.dp, blurRadius = 20.dp, innerPadding = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 36.dp)
        ) {

            // 🔥 Header (improved hierarchy)
            Text(
                text = stringResource(Res.string.createAccountTitle),
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(Res.string.createAccountSubtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 👇 Inputs (unchanged but spacing refined)

            CyberTextField(
                value = state.usernameInput,
                onValueChange = { onEvent(RegisterEvent.UsernameChanged(it)) },
                label = stringResource(Res.string.nameLabel),
                placeholder = stringResource(Res.string.namePlaceholder),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (state.usernameError != null) colorScheme.error
                        else colorScheme.onSurfaceVariant
                    )
                },
                isError = state.usernameError != null,
                errorMessage = state.usernameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CyberTextField(
                value = state.emailInput,
                onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                label = stringResource(Res.string.emailLabel),
                placeholder = stringResource(Res.string.emailPlaceholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = if (state.emailError != null) colorScheme.error
                        else colorScheme.onSurfaceVariant
                    )
                },
                isError = state.emailError != null,
                errorMessage = state.emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CyberTextField(
                value = state.passwordInput,
                onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                label = stringResource(Res.string.passwordLabel),
                placeholder = stringResource(Res.string.passwordPlaceholder),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (state.passwordError != null) colorScheme.error
                        else colorScheme.onSurfaceVariant
                    )
                },
                trailingContent = {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.labelMedium,
                        color = colorScheme.primary,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        })
                },
                isError = state.passwordError != null,
                errorMessage = state.passwordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CyberTextField(
                value = state.confirmPasswordInput,
                onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                label = stringResource(Res.string.confirmPasswordLabel),
                placeholder = stringResource(Res.string.passwordPlaceholder),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (state.confirmPasswordError != null) colorScheme.error
                        else colorScheme.onSurfaceVariant
                    )
                },
                isError = state.confirmPasswordError != null,
                errorMessage = state.confirmPasswordError
            )

            Spacer(modifier = Modifier.height(28.dp))

            PrimaryGlowButton(
                text = stringResource(Res.string.registerButton),
                isLoading = state.isLoading,
                onClick = { onEvent(RegisterEvent.SubmitRegistration) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.alreadyHaveAccount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.loginText),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() })
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    modifier = Modifier.weight(1f), color = colorScheme.outlineVariant.copy(alpha = 0.3f)
                )

                Text(
                    text = stringResource(Res.string.socialSync).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Divider(
                    modifier = Modifier.weight(1f), color = colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialAuthButton(
                    text = stringResource(Res.string.google), onClick = {}, modifier = Modifier.weight(1f)
                )
                SocialAuthButton(
                    text = stringResource(Res.string.apple), onClick = {}, modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ============================================================================
// STUBS FOR VIEWMODEL STATE (To make code compile independently)
// ============================================================================

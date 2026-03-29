package com.ghost.todo.ui.screens.auth


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ghost.todo.resources.*
import com.ghost.todo.ui.components.CyberTextField
import com.ghost.todo.ui.components.GradientText
import com.ghost.todo.ui.components.PrimaryGlowButton
import com.ghost.todo.ui.components.SocialAuthButton
import com.ghost.todo.ui.contract.LoginEvent
import com.ghost.todo.ui.contract.LoginState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


// ============================================================================
// 2. MAIN RESPONSIVE SCREEN
// ============================================================================
@Composable
fun LoginScreen(
    state: LoginState, // From the LoginViewModel we built earlier
    onEvent: (LoginEvent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForget: () -> Unit,
) {

    // Responsive breakpoint detection
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isDesktop = maxWidth > 800.dp

        if (isDesktop) {
            DesktopLoginLayout(state, onEvent, onNavigateToRegister, onNavigateToForget)
        } else {
            MobileLoginLayout(state, onEvent, onNavigateToRegister, onNavigateToForget)
        }
    }
}

// ============================================================================
// 3. DESKTOP LAYOUT (Split Screen)
// ============================================================================
@Composable
private fun DesktopLoginLayout(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForget: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        // Left Side: Atmospheric Branding (7/12 width approximation via weight)
        Box(
            modifier = Modifier
                .weight(1.6f)
                .fillMaxSize()
                .drawWithContent {
                    drawContent()

                    // Fade out toward right side
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colorScheme.surface
                            ),
                            startX = size.width * 0.7f, // where fade starts
                            endX = size.width
                        )
                    )
                }
        ) {
            // Simulated Atmospheric Image/Gradient Background
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.ripple),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                            )
                        )
                    )
            )

            // Gradient Overlay for depth
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

            // Content Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(80.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Logo
                Text(
                    text = stringResource(Res.string.appName),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )

                // Center Hero Text
                Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                    Text(
                        text = stringResource(Res.string.desktopHeroTitleOne),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = MaterialTheme.typography.displayLarge.lineHeight * 1.1f
                    )
                    GradientText(
                        text = stringResource(Res.string.desktopHeroTitleTwo),
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        ),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = MaterialTheme.typography.displayLarge.lineHeight * 1.1f

                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(Res.string.desktopHeroSubtitle),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Light
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 80.dp, vertical = 52.dp)
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Overlapping avatar images
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(40.dp)
                ) {
                    Image(
                        painterResource(Res.drawable.person_1),
                        contentDescription = stringResource(Res.string.user),
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterStart)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .zIndex(1f),
                        contentScale = ContentScale.Crop
                    )

                    Image(
                        painterResource(Res.drawable.person_2),
                        contentDescription = stringResource(Res.string.user),
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = 30.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .zIndex(2f),
                        contentScale = ContentScale.Crop
                    )

                    Image(
                        painterResource(Res.drawable.person_3),
                        contentDescription = stringResource(Res.string.user),
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = 60.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .zIndex(3f),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = stringResource(Res.string.activeInVoid).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                )
            }

        }


        // Right Side: Login Form (5/12 width approximation via weight)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {

            // Decorative Blob
            Box(
                modifier = Modifier.align(Alignment.TopEnd).size(320.dp)
                    .blur(120.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded).background(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape
                    )
            )

            LoginForm(
                state = state,
                onEvent = onEvent,
                onNavigateToRegister = onNavigateToRegister,
                onNavigateToForget = onNavigateToForget,
                modifier = Modifier
                    .fillMaxWidth(0.70f) // Don't let it stretch completely to the edges
                    .padding(vertical = 32.dp)
            )

            // Desktop Footer
            Text(
                text = stringResource(Res.string.copyright),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            )
        }
    }
}


// ============================================================================
// 4. MOBILE LAYOUT (Single Column)
// ============================================================================
@Composable
private fun MobileLoginLayout(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForget: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
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
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp) // ✅ FIXED WIDTH
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Branding
                Text(
                    text = stringResource(Res.string.appName),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Form
                LoginForm(
                    state = state,
                    onEvent = onEvent,
                    onNavigateToRegister = onNavigateToRegister,
                    onNavigateToForget = onNavigateToForget,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

// ============================================================================
// 5. REUSABLE FORM COMPONENT (Shared by both Mobile and Desktop)
// ============================================================================
@Composable
private fun LoginForm(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForget: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Headers
        Text(
            text = stringResource(Res.string.loginTitle),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.loginSubtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))

        // Input Fields
        CyberTextField(
            value = state.emailInput,
            onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
            label = stringResource(Res.string.emailLabel),
            placeholder = stringResource(Res.string.emailPlaceholder),
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

        Spacer(modifier = Modifier.height(24.dp))

        CyberTextField(
            value = state.passwordInput,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            label = stringResource(Res.string.passwordLabel),
            placeholder = stringResource(Res.string.passwordPlaceholder),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (state.passwordError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            isError = state.passwordError != null,
            errorMessage = state.passwordError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingContent = {
                Text(
                    text = stringResource(Res.string.forgotPassword),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onNavigateToForget()
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Main Action
        PrimaryGlowButton(
            text = stringResource(Res.string.continueButton),
            isLoading = state.isLoading,
            onClick = { onEvent(LoginEvent.SubmitEmailLogin) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Alternative Access Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            Text(
                text = stringResource(Res.string.orAccessWith).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 16.dp),
                letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing * 1.5f
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Social Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialAuthButton(
                text = stringResource(Res.string.google),
                onClick = { /* Trigger Google Auth Effect */ },
                modifier = Modifier.weight(1f)
            )
            SocialAuthButton(
                text = stringResource(Res.string.apple),
                onClick = { /* Trigger Apple Auth Effect */ },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Bottom Nav Link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.newToVoid),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(Res.string.createAccount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.DESKTOP)
@Composable
private fun DesktopLoginPreview() {
    MaterialTheme {
        LoginScreen(state = LoginState(), onEvent = {}, onNavigateToRegister = {}, {})
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun MobileLoginPreview() {
    MaterialTheme {
        LoginScreen(state = LoginState(), onEvent = {}, onNavigateToRegister = {}, onNavigateToForget = {})
    }
}



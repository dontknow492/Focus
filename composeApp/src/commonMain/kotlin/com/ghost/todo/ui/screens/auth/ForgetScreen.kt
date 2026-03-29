package com.ghost.todo.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ghost.todo.resources.*
import com.ghost.todo.ui.components.CyberTextField
import com.ghost.todo.ui.components.GradientText
import com.ghost.todo.ui.components.PrimaryGlowButton
import com.ghost.todo.ui.contract.ForgotPasswordEvent
import com.ghost.todo.ui.contract.ForgotPasswordState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


// ============================================================================
// 1. MAIN RESPONSIVE SCREEN
// ============================================================================
@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onNavigateToLogin: () -> Unit,
    onEvent: (ForgotPasswordEvent) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isDesktop = maxWidth > 800.dp

        if (isDesktop) {
            DesktopForgotPasswordLayout(state, onNavigateToLogin, onEvent)
        } else {
            MobileForgotPasswordLayout(state, onNavigateToLogin, onEvent)
        }
    }
}


fun shuffle(nums: IntArray, n: Int): IntArray {
    intArrayOf()
    val shuffled = mutableListOf<Int>()
    for (i in 0..n) {
        shuffled.add(nums[i])
        shuffled.add(nums[i + n])
    }
    return shuffled.toIntArray()


}

// ============================================================================
// 3. DESKTOP LAYOUT (Split Screen)
// ============================================================================
@Composable
private fun DesktopForgotPasswordLayout(
    state: ForgotPasswordState,
    onNavigateToLogin: () -> Unit,
    onEvent: (ForgotPasswordEvent) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        // Left Side: Hero Visual Anchor
        Box(
            modifier = Modifier
                .weight(1.2f)
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

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.wave),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
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
                // Branding Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(Res.string.appName),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Black
                    )
                }

                // Center Hero Text
                Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                    Text(
                        text = stringResource(Res.string.restoreDesktopHeroTitleLine1),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    GradientText(
                        text = stringResource(Res.string.restoreDesktopHeroTitleLine2),
                        style = MaterialTheme.typography.displayMedium,
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic
                    )


                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(Res.string.restoreDesktopHeroSubtitle),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Light,
                        lineHeight = MaterialTheme.typography.titleMedium.lineHeight * 1.3f
                    )
                }

                // Footer details
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "PRECISION",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(
                        modifier = Modifier.size(4.dp).background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    )
                    Text(
                        "FLOW",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(
                        modifier = Modifier.size(4.dp).background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    )
                    Text(
                        "SANCTUARY",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Right Side: Recovery Form
        Box(
            modifier = Modifier
                .weight(0.85f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            // Subtle Background Glow
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 100.dp, end = 100.dp)
                    .size(300.dp)
                    .blur(120.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), CircleShape)
            )

            Column(
                modifier = Modifier.fillMaxWidth(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ForgotPasswordForm(
                    state = state,
                    onEvent = onEvent,
                    onNavigateToLogin = onNavigateToLogin,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Desktop Footer Links
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.backToLogin),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = stringResource(Res.string.versionText).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing * 1.5f
                )
            }
        }
    }
}

// ============================================================================
// 4. MOBILE LAYOUT (Single Column)
// ============================================================================
@Composable
private fun MobileForgotPasswordLayout(
    state: ForgotPasswordState,
    onNavigateToLogin: () -> Unit,
    onEvent: (ForgotPasswordEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        // Abstract Decorative Elements
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(200.dp)
                .blur(100.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
        )

        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Top App Bar
            Row(
                modifier = Modifier

                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onNavigateToLogin() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = stringResource(Res.string.appName),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )

                // Spacer for symmetry
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Form Content
            ForgotPasswordForm(
                state = state,
                onEvent = onEvent,
                onNavigateToLogin = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
            )

            // Mobile Graphic (Organic Asymmetry from HTML)
            if (!state.isEmailSent) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .size(96.dp)
                            .rotate(12f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.LockReset,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// 5. REUSABLE FORM COMPONENT
// ============================================================================
@Composable
private fun ForgotPasswordForm(
    state: ForgotPasswordState,
    onNavigateToLogin: () -> Unit,
    onEvent: (ForgotPasswordEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = state.isEmailSent,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        modifier = modifier
    ) { emailSent ->
        if (emailSent) {
            // SUCCESS STATE
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(64.dp).padding(bottom = 16.dp)
                )
                Text(
                    text = stringResource(Res.string.successTitle),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.successMessage),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                PrimaryGlowButton(
                    text = stringResource(Res.string.backToLogin),
                    onClick = { onNavigateToLogin() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            // INPUT STATE
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.restoreTitle),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.restoreSubtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f
                )
                Spacer(modifier = Modifier.height(48.dp))

                CyberTextField(
                    value = state.emailInput,
                    onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
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

                Spacer(modifier = Modifier.height(32.dp))

                PrimaryGlowButton(
                    text = stringResource(Res.string.sendRecoveryButton),
                    isLoading = state.isLoading,
                    onClick = { onEvent(ForgotPasswordEvent.SubmitResetRequest) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

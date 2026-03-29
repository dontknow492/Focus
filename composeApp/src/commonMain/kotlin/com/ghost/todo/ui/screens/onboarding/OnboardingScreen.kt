package com.ghost.todo.ui.screens.onboarding


import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghost.todo.resources.*
import com.ghost.todo.ui.components.AdvancedGlassmorphismCard
import com.ghost.todo.ui.components.GradientText
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// ============================================================================
// 1. STRING RESOURCES (Simulating KMP Res.strings)
// ============================================================================
object OnboardingStrings {
    val onboarding_app_name = "Ether Focus"
    val onboarding_headline_part1 = "Find Your"
    val onboarding_headline_part2 = "Focus."
    val onboarding_description =
        "Reclaim your attention in a world of noise. Experience a digital sanctuary designed for deep work and intentional living."
    val onboarding_action_start = "Get Started"
    val onboarding_action_demo = "Watch Demo"

    val onboarding_social_proof_prefix = "Joined by"
    val onboarding_social_proof_highlight = "12k+ creators"
    val onboarding_social_proof_suffix = "globally."

    // Preview Card Strings
    val preview_card_session_label = "Deep Work Session"
    val preview_card_session_task = "Design System Architecture"
    val preview_card_session_time_left = "45:00 Remaining"

    val preview_card_insights_label = "Insights"
    val preview_card_insights_metric = "+24%"
    val preview_card_insights_status = "Efficiency up"

    val preview_card_focus_active = "Focus Mode Active"
}

// ============================================================================
// 2. MAIN RESPONSIVE SCREEN
// ============================================================================
@Composable
fun OnboardingScreen(
    onWatchDemo: () -> Unit,
    onOnBoardingComplete: () -> Unit,
) {
    // Responsive breakpoint detection
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isDesktop = maxWidth > 800.dp

        if (isDesktop) {
            DesktopOnboardingScreen(onWatchDemo, onOnBoardingComplete)
        } else {
            MobileOnboardingScreen(onWatchDemo, onOnBoardingComplete)
        }
    }
}

// ============================================================================
// 3. DESKTOP LAYOUT (Split Screen)
// ============================================================================
@Composable
fun DesktopOnboardingScreen(
    onWatchDemo: () -> Unit,
    onOnBoardingComplete: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Left Section: Info Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 64.dp, vertical = 48.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            OnboardingInfoContent(onWatchDemo, onOnBoardingComplete)
        }

        // Right Section: Visual Preview
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            // Background glow
            Box(
                modifier = Modifier
                    .size(500.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            )
            OnboardingPreviewContent(onOnBoardingComplete)
        }
    }
}

// ============================================================================
// 4. MOBILE LAYOUT (2-Page Pager)
// ============================================================================
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MobileOnboardingScreen(
    onWatchDemo: () -> Unit,
    onOnBoardingComplete: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Main Pager Area
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) { page ->
            when (page) {
                0 -> OnboardingPreviewContent(onOnBoardingComplete)
                1 -> OnboardingInfoContent(onWatchDemo, onOnBoardingComplete)
            }
        }

        // Bottom Navigation Controls
        MobileBottomNavigation(
            currentPage = pagerState.currentPage,
            totalPages = 2,
            onNext = {
                coroutineScope.launch {
                    if (pagerState.currentPage + 1 == pagerState.pageCount) {
                        onOnBoardingComplete()
                    } else {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            onBack = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        )
    }
}


@Composable
private fun MobileBottomNavigation(
    currentPage: Int,
    totalPages: Int,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress Indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(totalPages) { iteration ->
                val isActive = currentPage == iteration

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(if (isActive) 24.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                        .animateContentSize()
                )
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            AnimatedVisibility(
                visible = currentPage > 0,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Spacer
            Spacer(modifier = Modifier.weight(1f))

            // Next/Done Button
            Button(
                onClick = onNext,
                modifier = Modifier
                    .height(48.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = if (currentPage == 1) stringResource(Res.string.done) else stringResource(Res.string.next),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// ============================================================================
// 5. INFO CONTENT (Headlines, Text, Buttons)
// ============================================================================
@Composable
private fun OnboardingInfoContent(
    onWatchDemo: () -> Unit,
    onOnBoardingComplete: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Branding
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star, // Placeholder for your branding icon
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = OnboardingStrings.onboarding_app_name.uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Headline
        Text(
            text = OnboardingStrings.onboarding_headline_part1,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 64.sp
        )

        GradientText(
            text = OnboardingStrings.onboarding_headline_part2,
            style = MaterialTheme.typography.displayLarge,
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            ),
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            fontWeight = FontWeight.ExtraBold,
        )


        Spacer(modifier = Modifier.height(24.dp))

        // Subtitle
        Text(
            text = OnboardingStrings.onboarding_description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Call to Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Primary Action
            Button(
                onClick = onOnBoardingComplete,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.height(56.dp).weight(1f)
            ) {
                Text(
                    text = OnboardingStrings.onboarding_action_start,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Secondary Action (Watch Demo)
            Row(
                modifier = Modifier
                    .clickable { onWatchDemo() }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = OnboardingStrings.onboarding_action_demo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Social Proof
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
                // Placeholder Avatars
                Image(
                    painter = painterResource(Res.drawable.female_1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                )
                Image(
                    painter = painterResource(Res.drawable.female_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                )
                Image(
                    painter = painterResource(Res.drawable.person_4),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${OnboardingStrings.onboarding_social_proof_prefix} ${OnboardingStrings.onboarding_social_proof_highlight} ${OnboardingStrings.onboarding_social_proof_suffix}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ============================================================================
// 6. PREVIEW CONTENT (Abstract Graphics & Floating Glass Cards)
// ============================================================================
@Composable
private fun OnboardingPreviewContent(
    onOnBoardingComplete: () -> Unit,
) {
    MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {

        // 🌊 Background Glow (Improved)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(140.dp)
        )

        // 🧱 Main Image Container
        Box(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    )
                )
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.abstract),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

        // 🧊 Card 1 — Focus Session (Secondary / Calm)
        AdvancedGlassmorphismCard(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(0.6f)
                .scale(0.95f)
                .rotate(-4f),

            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
            borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
            innerGlowColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = OnboardingStrings.preview_card_session_label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = OnboardingStrings.preview_card_session_task,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = OnboardingStrings.preview_card_session_time_left,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                CircleShape
                            )
                    )
                }
            }
        }

        // 🚀 Card 2 — Insights (Primary / Highlight)
        AdvancedGlassmorphismCard(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.55f)
                .scale(1.05f)
                .rotate(3f),

            backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            innerGlowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
            shadowElevation = 16.dp,
            innerPadding = 0.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = OnboardingStrings.preview_card_insights_label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )

                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val heights = listOf(0.4f, 0.6f, 0.3f, 0.9f, 0.5f, 0.4f)

                    heights.forEachIndexed { index, fraction ->
                        val color =
                            if (index == 3) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(fraction)
                                .padding(horizontal = 2.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 4.dp,
                                        topEnd = 4.dp
                                    )
                                )
                                .background(color)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = OnboardingStrings.preview_card_insights_metric,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = OnboardingStrings.preview_card_insights_status,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        // ⚡ Card 3 — Pill (Tertiary / Accent)
        AdvancedGlassmorphismCard(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),

            shape = RoundedCornerShape(50),
            innerPadding = 0.dp,

            backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
            borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
            innerGlowColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = OnboardingStrings.preview_card_focus_active,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


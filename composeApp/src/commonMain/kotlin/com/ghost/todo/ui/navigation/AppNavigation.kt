package com.ghost.todo.ui.navigation

//import androidx.navigation3.ui.NavDisplay
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.ghost.todo.ui.screens.onboarding.OnboardingScreen

@Composable
fun AppNavigation(modifier: Modifier) {
    var isOnboarded by remember { mutableStateOf(false) }
    var isAuthenticated by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = when {
            !isOnboarded -> "onboarding"
            !isAuthenticated -> "auth"
            else -> "main"
        },
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(600, easing = EaseInOutCubic)
            ) { width -> width } togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(600, easing = EaseInOutCubic)
                    ) { width -> -width }
        },
        label = "AppNavigation"
    ) { targetState ->
        when (targetState) {
            "onboarding" -> OnboardingScreen(
                onWatchDemo = {},
                onOnBoardingComplete = {
                    isOnboarded = true
                }
            )

            "auth" -> AuthNavigation(modifier)
            else -> TODO("Main app navigation goes here")
        }
    }
}






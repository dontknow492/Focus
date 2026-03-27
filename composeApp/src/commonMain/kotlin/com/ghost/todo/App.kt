package com.ghost.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ghost.todo.ui.contract.LoginState
import com.ghost.todo.ui.contract.RegisterState
import com.ghost.todo.ui.screens.LoginScreen
import com.ghost.todo.ui.screens.RegisterScreen
import com.ghost.todo.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource



@Composable
@Preview
fun App() {
    AppTheme(
        isDarkTheme = true
    ) {
        AppContent()
    }
}


@Composable
fun AppContent() {
    RegisterScreen(
        state = RegisterState(),
        onEvent = {},
        onNavigateToLogin = {}
    )
}